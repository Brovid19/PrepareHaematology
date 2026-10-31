#!/usr/bin/env python3
"""
Build the PWA's study content.

Source of truth (edit these):
    content/guidelines.json   guideline digests (summary, cutoffs, staging, recommendations, pitfalls)
    content/questions.json    MCQ / EMQ / essay questions
    content/flashcards.json   spaced-repetition flashcards

Generated (do not edit by hand):
    public/content.js         the data the web app loads
    public/sw.js              BUILD_ID line is stamped so installed apps pick up new versions

Usage (from the repository root):
    python3 scripts/build_web_content.py           # validate + write generated files
    python3 scripts/build_web_content.py --check   # validate + fail if generated files are stale

Validation fails loudly on: duplicate ids, unknown domains/categories, questions or cards that
point at a guideline that doesn't exist, out-of-range answer indexes, duplicate options, essays
without a model answer, non-https links, and PDFs that aren't in public/assets.
"""
import hashlib
import json
import pathlib
import re
import sys

ROOT = pathlib.Path(__file__).resolve().parent.parent
CONTENT = ROOT / "content"
PUBLIC = ROOT / "public"
OUT = PUBLIC / "content.js"
SW = PUBLIC / "sw.js"
PDF_DIR = PUBLIC / "assets" / "aistudio"

PRACTICE_DOMAINS = {"Haemato-Oncology", "Haemostasis & Thrombosis", "Transfusion Medicine", "General Haematology"}
GUIDELINE_CATEGORIES = {"Malignant Haematology", "Haemostasis & Thrombosis", "Transfusion Medicine", "General Haematology"}
QUESTION_TYPES = {"MCQ", "EMQ", "ESSAY"}
FALLBACK_URL = "https://b-s-h.org.uk/guidelines"
PRACTICE_DOMAIN_FOR_CATEGORY = {"Malignant Haematology": "Haemato-Oncology"}


def load(name):
    return json.loads((CONTENT / f"{name}.json").read_text(encoding="utf-8"))


def validate(guidelines, questions, flashcards):
    errors = []

    def need(item, kind, keys):
        for k in keys:
            if k not in item or item[k] in (None, "", []):
                errors.append(f"{kind} {item.get('id', '?')}: missing {k}")

    for kind, items in (("guideline", guidelines), ("question", questions), ("flashcard", flashcards)):
        ids = [x.get("id") for x in items]
        dupes = sorted({i for i in ids if ids.count(i) > 1})
        if dupes:
            errors.append(f"duplicate {kind} ids: {dupes}")

    guideline_ids = {g["id"] for g in guidelines}
    for g in guidelines:
        need(g, "guideline", ["id", "title", "issuingBody", "year", "category", "citation", "summary", "recommendations"])
        if g.get("category") not in GUIDELINE_CATEGORIES:
            errors.append(f"guideline {g['id']}: unknown category {g.get('category')!r}")
        if g.get("url") and not g["url"].startswith("https://"):
            errors.append(f"guideline {g['id']}: url must start with https://")
        if g.get("pdf") and not (PDF_DIR / g["pdf"]).exists():
            errors.append(f"guideline {g['id']}: pdf not found in public/assets/aistudio: {g['pdf']}")
        if not isinstance(g.get("isVerified"), bool):
            errors.append(f"guideline {g['id']}: isVerified must be true or false")

    for q in questions:
        need(q, "question", ["id", "domain", "type", "title", "stem", "sourceResourceId"])
        if q.get("type") not in QUESTION_TYPES:
            errors.append(f"question {q['id']}: unknown type {q.get('type')!r}")
        if q.get("domain") not in PRACTICE_DOMAINS:
            errors.append(f"question {q['id']}: unknown domain {q.get('domain')!r}")
        if q.get("sourceResourceId") not in guideline_ids:
            errors.append(f"question {q['id']}: unknown sourceResourceId {q.get('sourceResourceId')!r}")
        if q.get("type") == "ESSAY":
            if not q.get("essayModelAnswer"):
                errors.append(f"question {q['id']}: essay needs essayModelAnswer")
        else:
            opts = q.get("options") or []
            if len(opts) < 2:
                errors.append(f"question {q['id']}: needs at least 2 options")
            if len(set(opts)) != len(opts):
                errors.append(f"question {q['id']}: duplicate options")
            if not isinstance(q.get("correctIndex"), int) or not (0 <= q["correctIndex"] < len(opts)):
                errors.append(f"question {q['id']}: correctIndex out of range")
            if not q.get("rationale"):
                errors.append(f"question {q['id']}: missing rationale")

    for c in flashcards:
        need(c, "flashcard", ["id", "domain", "front", "back", "sourceResourceId"])
        if c.get("domain") not in PRACTICE_DOMAINS:
            errors.append(f"flashcard {c['id']}: unknown domain {c.get('domain')!r}")
        if c.get("sourceResourceId") not in guideline_ids:
            errors.append(f"flashcard {c['id']}: unknown sourceResourceId {c.get('sourceResourceId')!r}")

    if errors:
        raise SystemExit("Content validation FAILED:\n  " + "\n  ".join(errors))


def lines(value):
    return "\n".join(value) if isinstance(value, list) else (value or "")


def to_web(guidelines, questions, flashcards):
    web_guidelines = [{
        "id": g["id"],
        "title": g["title"],
        "issuingBody": g["issuingBody"],
        "year": g["year"],
        "domain": g["category"],
        "practiceDomain": PRACTICE_DOMAIN_FOR_CATEGORY.get(g["category"], g["category"]),
        "isVerified": g["isVerified"],
        "grade": "Checked against source PDF" if g["isVerified"] else "Not yet verified",
        "citation": g["citation"],
        "pdfUrl": f"/assets/aistudio/{g['pdf']}" if g.get("pdf") else "",
        "onlineUrl": g.get("url") or FALLBACK_URL,
        "summary": g["summary"],
        "cutoffs": lines(g.get("keyCutoffs")),
        "staging": lines(g.get("stagingCriteria")),
        "recommendations": g["recommendations"],
        "pitfalls": "\n".join("• " + p for p in g.get("pitfalls", [])),
    } for g in guidelines]

    web_questions = []
    for q in questions:
        stem = q["stem"]
        if q.get("labData"):
            stem += "\n\nInvestigations:\n" + lines(q["labData"])
        if q["type"] == "ESSAY":
            explanation = lines(q["essayModelAnswer"])
            if q.get("markingRubric"):
                explanation += "\n\nSuggested marking:\n" + lines(q["markingRubric"])
        else:
            explanation = q["rationale"]
        web_questions.append({
            "id": q["id"],
            "domain": q["domain"],
            "questionType": q["type"],
            "title": q["title"],
            "stem": stem,
            "options": q.get("options", []),
            "correct": q.get("correctIndex", -1),
            "explanation": explanation,
            "ref": " • ".join(p for p in (q.get("yearPaper"), q.get("guidelineSection")) if p),
            "evidenceGrade": q.get("evidenceGrade", ""),
            "sourceResourceId": q["sourceResourceId"],
        })

    web_cards = [{
        "id": c["id"],
        "domain": c["domain"],
        "topic": c.get("topic", ""),
        "ref": c.get("guidelineRef", ""),
        "front": c["front"],
        "back": c["back"],
        "sourceResourceId": c["sourceResourceId"],
    } for c in flashcards]

    return {"guidelines": web_guidelines, "questions": web_questions, "flashcards": web_cards}


def build():
    guidelines, questions, flashcards = load("guidelines"), load("questions"), load("flashcards")
    validate(guidelines, questions, flashcards)
    payload = to_web(guidelines, questions, flashcards)
    js = (
        "// GENERATED FILE - DO NOT EDIT BY HAND.\n"
        "// Source of truth: content/*.json. Regenerate with: python3 scripts/build_web_content.py\n"
        "window.PREPARE_CONTENT = " + json.dumps(payload, ensure_ascii=False, indent=1) + ";\n"
    )

    # BUILD_ID changes whenever the app shell or content changes, so installed PWAs update.
    digest = hashlib.sha256()
    for path in sorted(list(PUBLIC.glob("*.html")) + list(PUBLIC.glob("*.webmanifest")) + list((PUBLIC / "icons").glob("*"))):
        digest.update(path.read_bytes())
    digest.update(js.encode("utf-8"))
    build_id = digest.hexdigest()[:12]
    sw_src = SW.read_text(encoding="utf-8")
    sw_new, n = re.subn(r"const BUILD_ID = '[^']*';", f"const BUILD_ID = '{build_id}';", sw_src)
    if n != 1:
        raise SystemExit("public/sw.js must contain exactly one line: const BUILD_ID = '...';")
    return js, sw_new, (len(guidelines), len(questions), len(flashcards)), build_id


def main():
    js, sw_new, counts, build_id = build()
    summary = "{} guidelines, {} questions, {} flashcards".format(*counts)
    if "--check" in sys.argv:
        stale = []
        if (OUT.read_text(encoding="utf-8") if OUT.exists() else "") != js:
            stale.append("public/content.js")
        if SW.read_text(encoding="utf-8") != sw_new:
            stale.append("public/sw.js (BUILD_ID)")
        if stale:
            print("Out of date: " + ", ".join(stale) + ". Run: python3 scripts/build_web_content.py")
            sys.exit(1)
        print(f"Content valid and generated files up to date ({summary}, build {build_id})")
        return
    OUT.write_text(js, encoding="utf-8")
    SW.write_text(sw_new, encoding="utf-8")
    print(f"Wrote public/content.js and stamped public/sw.js ({summary}, build {build_id})")


if __name__ == "__main__":
    main()

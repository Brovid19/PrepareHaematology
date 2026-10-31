#!/usr/bin/env python3
"""
Build the PWA's study content.

Source of truth (edit these):
    content/papers/*.json     one file per paper: guideline metadata, its full recommendations
                              (verbatim, graded, with PDF page) and its topic summary pages
    content/questions.json    MCQ / EMQ / essay questions (each linked to a topic, optionally a recommendation)
    content/flashcards.json   spaced-repetition flashcards (each linked to a topic, optionally a recommendation)

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
PAPERS = CONTENT / "papers"

PRACTICE_DOMAINS = {"Haemato-Oncology", "Haemostasis & Thrombosis", "Transfusion Medicine", "General Haematology"}
GUIDELINE_CATEGORIES = {"Malignant Haematology", "Haemostasis & Thrombosis", "Transfusion Medicine", "General Haematology"}
QUESTION_TYPES = {"MCQ", "EMQ", "ESSAY"}
FALLBACK_URL = "https://b-s-h.org.uk/guidelines"
PRACTICE_DOMAIN_FOR_CATEGORY = {"Malignant Haematology": "Haemato-Oncology"}


def load(name):
    return json.loads((CONTENT / f"{name}.json").read_text(encoding="utf-8"))


def load_papers():
    """content/papers/*.json -> (guidelines, recommendations by paper, topics) in file order."""
    guidelines, topics = [], []
    for path in sorted(PAPERS.glob("*.json")):
        data = json.loads(path.read_text(encoding="utf-8"))
        g = dict(data["guideline"])
        g["recommendations"] = data.get("recommendations", [])
        g["_file"] = path.name
        guidelines.append(g)
        domain = PRACTICE_DOMAIN_FOR_CATEGORY.get(g.get("category"), g.get("category"))
        for i, t in enumerate(data.get("topics", [])):
            t = dict(t)
            t.setdefault("domain", domain)
            t["sourceResourceId"] = g["id"]
            t["order"] = i
            topics.append(t)
    return guidelines, topics


KEBAB = re.compile(r"^[a-z0-9]+(-[a-z0-9]+)*$")
GRADE = re.compile(r"^[12][A-D]$")
TOPIC_LIST_FIELDS = ("keyPoints", "numbers", "tables", "recs", "pitfalls", "selfTest", "pages")


def strings_in(value):
    if isinstance(value, str):
        yield value
    elif isinstance(value, list):
        for v in value:
            yield from strings_in(v)
    elif isinstance(value, dict):
        for v in value.values():
            yield from strings_in(v)


def validate(guidelines, topics, questions, flashcards):
    errors = []

    def need(item, kind, keys):
        for k in keys:
            if k not in item or item[k] in (None, "", []):
                errors.append(f"{kind} {item.get('id', '?')}: missing {k}")

    for kind, items in (("guideline", guidelines), ("question", questions), ("flashcard", flashcards), ("topic", topics)):
        ids = [x.get("id") for x in items]
        dupes = sorted({i for i in ids if ids.count(i) > 1})
        if dupes:
            errors.append(f"duplicate {kind} ids: {dupes}")

    guidelines_by_id = {g["id"]: g for g in guidelines}
    rec_ids = {g["id"]: {r.get("id") for r in g["recommendations"]} for g in guidelines}
    for g in guidelines:
        need(g, "guideline", ["id", "title", "issuingBody", "year", "category", "citation", "overview", "recommendations"])
        if g.get("category") not in GUIDELINE_CATEGORIES:
            errors.append(f"guideline {g['id']}: unknown category {g.get('category')!r}")
        if g.get("url") and not g["url"].startswith("https://"):
            errors.append(f"guideline {g['id']}: url must start with https://")
        if g.get("pdf") and not (PDF_DIR / g["pdf"]).exists():
            errors.append(f"guideline {g['id']}: pdf not found in public/assets/aistudio: {g['pdf']}")
        if not isinstance(g.get("isVerified"), bool):
            errors.append(f"guideline {g['id']}: isVerified must be true or false")
        seen = []
        for r in g["recommendations"]:
            need(r, f"recommendation {g['id']}/", ["id", "group", "text", "page"])
            if r.get("id") in seen:
                errors.append(f"guideline {g['id']}: duplicate recommendation id {r.get('id')!r}")
            seen.append(r.get("id"))
            if r.get("grade") is not None and not GRADE.match(str(r["grade"])):
                errors.append(f"guideline {g['id']}: recommendation {r.get('id')}: grade must look like 1A..2D or null")
            if not isinstance(r.get("page"), int) or r["page"] < 1:
                errors.append(f"guideline {g['id']}: recommendation {r.get('id')}: page must be a positive integer")

    topics_by_id = {t["id"]: t for t in topics}
    aliases = {}
    for t in topics:
        need(t, "topic", ["id", "name", "domain", "sourceResourceId", "pages", "why", "keyPoints"])
        if not KEBAB.match(t.get("id", "")):
            errors.append(f"topic {t.get('id')!r}: id must be lowercase-kebab-case")
        if t.get("domain") not in PRACTICE_DOMAINS:
            errors.append(f"topic {t['id']}: unknown domain {t.get('domain')!r}")
        for f in TOPIC_LIST_FIELDS:
            if f in t and not isinstance(t[f], list):
                errors.append(f"topic {t['id']}: {f} must be a list")
        for p in t.get("pages", []):
            if not isinstance(p, int) or p < 1:
                errors.append(f"topic {t['id']}: pages must be positive integers")
        for r in t.get("recs", []):
            if r not in rec_ids.get(t["sourceResourceId"], set()):
                errors.append(f"topic {t['id']}: unknown recommendation {r!r} for {t['sourceResourceId']}")
        for n in t.get("numbers", []):
            if not (isinstance(n, list) and len(n) == 2):
                errors.append(f"topic {t['id']}: each number must be [value, meaning]")
        for tb in t.get("tables", []):
            need(tb, f"topic {t['id']} table", ["title", "columns", "rows"])
            for row in tb.get("rows", []):
                if len(row) != len(tb.get("columns", [])):
                    errors.append(f"topic {t['id']}: table {tb.get('title')!r} has a row with the wrong number of cells")
        for st in t.get("selfTest", []):
            if not (st.get("q") and st.get("a")):
                errors.append(f"topic {t['id']}: selfTest items need q and a")
        for s in strings_in({k: t.get(k) for k in ("why", "keyPoints", "pitfalls", "hook")}):
            if s.count("**") % 2:
                errors.append(f"topic {t['id']}: unbalanced ** in {s[:50]!r}")
        for a in t.get("aliases", []):
            if a in topics_by_id or a in aliases:
                errors.append(f"topic {t['id']}: alias {a!r} clashes with another topic or alias")
            aliases[a] = t["id"]

    # Every recommendation must be shown on at least one topic page (nothing from the paper is dropped).
    for g in guidelines:
        shown = {r for t in topics if t["sourceResourceId"] == g["id"] for r in t.get("recs", [])}
        for r in g["recommendations"]:
            if r["id"] not in shown:
                errors.append(f"guideline {g['id']}: recommendation {r['id']} is not on any topic page")

    def check_link(kind, item, same_domain=True):
        t = topics_by_id.get(item.get("topicId"))
        if not t:
            errors.append(f"{kind} {item['id']}: unknown topicId {item.get('topicId')!r}")
            return
        if t["sourceResourceId"] != item.get("sourceResourceId"):
            errors.append(f"{kind} {item['id']}: paper differs from its topic's paper ({t['sourceResourceId']})")
        if same_domain and t["domain"] != item.get("domain"):
            errors.append(f"{kind} {item['id']}: domain differs from its topic's domain ({t['domain']})")
        rec = item.get("recId")
        if rec is not None and rec not in rec_ids.get(t["sourceResourceId"], set()):
            errors.append(f"{kind} {item['id']}: unknown recId {rec!r}")

    for q in questions:
        need(q, "question", ["id", "domain", "type", "title", "stem", "sourceResourceId", "topicId"])
        if q.get("type") not in QUESTION_TYPES:
            errors.append(f"question {q['id']}: unknown type {q.get('type')!r}")
        if q.get("domain") not in PRACTICE_DOMAINS:
            errors.append(f"question {q['id']}: unknown domain {q.get('domain')!r}")
        if q.get("sourceResourceId") not in guidelines_by_id:
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
        check_link("question", q, same_domain=False)  # a question may be practised under another domain

    for c in flashcards:
        need(c, "flashcard", ["id", "domain", "front", "back", "sourceResourceId", "topicId"])
        if c.get("domain") not in PRACTICE_DOMAINS:
            errors.append(f"flashcard {c['id']}: unknown domain {c.get('domain')!r}")
        if c.get("sourceResourceId") not in guidelines_by_id:
            errors.append(f"flashcard {c['id']}: unknown sourceResourceId {c.get('sourceResourceId')!r}")
        check_link("flashcard", c)

    if errors:
        raise SystemExit("Content validation FAILED:\n  " + "\n  ".join(errors))


def lines(value):
    return "\n".join(value) if isinstance(value, list) else (value or "")


def short_ref(g):
    """'Garg M, Choudhuri S, ...' + year -> 'Garg et al. 2026'."""
    first = g["citation"].split(",")[0].strip()
    surname = first.split(" ")[0] if first else g["issuingBody"]
    return f"{surname} et al. {g['year']}"


TOPIC_WEB_FIELDS = ("id", "domain", "name", "sourceResourceId", "order", "pages", "why", "keyPoints", "numbers",
                    "tables", "recs", "pitfalls", "hook", "selfTest", "aliases")


def to_web(guidelines, topics, questions, flashcards):
    web_guidelines = [{
        "id": g["id"],
        "title": g["title"],
        "issuingBody": g["issuingBody"],
        "year": g["year"],
        "domain": g["category"],
        "practiceDomain": PRACTICE_DOMAIN_FOR_CATEGORY.get(g["category"], g["category"]),
        "isVerified": g["isVerified"],
        "citation": g["citation"],
        "pdfUrl": f"/assets/aistudio/{g['pdf']}" if g.get("pdf") else "",
        "onlineUrl": g.get("url") or FALLBACK_URL,
        "overview": g["overview"],
        "recommendations": [{k: r.get(k) for k in ("id", "group", "grade", "text", "page")} for r in g["recommendations"]],
        "topicIds": [t["id"] for t in topics if t["sourceResourceId"] == g["id"]],
        "shortRef": short_ref(g),
    } for g in guidelines]

    web_topics = [{k: t[k] for k in TOPIC_WEB_FIELDS if k in t} for t in topics]

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
            "topicId": q["topicId"],
            "recId": q.get("recId"),
        })

    topic_name = {t["id"]: t["name"] for t in topics}
    web_cards = [{
        "id": c["id"],
        "domain": c["domain"],
        "topicId": c["topicId"],
        "topic": topic_name[c["topicId"]],
        "ref": c.get("guidelineRef", ""),
        "front": c["front"],
        "back": c["back"],
        "sourceResourceId": c["sourceResourceId"],
        "recId": c.get("recId"),
    } for c in flashcards]

    return {"guidelines": web_guidelines, "topics": web_topics, "questions": web_questions, "flashcards": web_cards}


def build():
    guidelines, topics = load_papers()
    questions, flashcards = load("questions"), load("flashcards")
    validate(guidelines, topics, questions, flashcards)
    payload = to_web(guidelines, topics, questions, flashcards)
    js = (
        "// GENERATED FILE - DO NOT EDIT BY HAND.\n"
        "// Source of truth: content/*.json. Regenerate with: python3 scripts/build_web_content.py\n"
        "window.PREPARE_CONTENT = " + json.dumps(payload, ensure_ascii=False, indent=1) + ";\n"
    )

    # BUILD_ID changes whenever the app shell or content changes, so installed PWAs update.
    digest = hashlib.sha256()
    for path in sorted(list(PUBLIC.glob("*.html")) + list(PUBLIC.glob("*.webmanifest")) + list((PUBLIC / "icons").glob("*")) + list((PUBLIC / "fonts").glob("*"))):
        digest.update(path.read_bytes())
    digest.update(js.encode("utf-8"))
    build_id = digest.hexdigest()[:12]
    sw_src = SW.read_text(encoding="utf-8")
    sw_new, n = re.subn(r"const BUILD_ID = '[^']*';", f"const BUILD_ID = '{build_id}';", sw_src)
    if n != 1:
        raise SystemExit("public/sw.js must contain exactly one line: const BUILD_ID = '...';")
    return js, sw_new, (len(guidelines), len(questions), len(flashcards), len(topics)), build_id


def main():
    js, sw_new, counts, build_id = build()
    summary = "{} guidelines, {} questions, {} flashcards, {} topics".format(*counts)
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

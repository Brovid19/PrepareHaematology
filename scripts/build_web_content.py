#!/usr/bin/env python3
"""
Generate the website's study content (public/content.js) from the Android app's
single source of truth:

  * app/src/main/java/com/example/data/db/SeedData.kt          (resources, questions, flashcards)
  * app/src/main/java/com/example/ui/screens/SummarisedGuidelinesScreen.kt  (exam pitfalls)

Run from the repository root:

    python3 scripts/build_web_content.py           # writes public/content.js
    python3 scripts/build_web_content.py --check   # exits 1 if public/content.js is stale

The script also runs integrity checks and fails loudly if content is inconsistent
(unknown sourceResourceId, out-of-range answer index, unknown domain, duplicate ids...).
"""
import json
import pathlib
import re
import sys

ROOT = pathlib.Path(__file__).resolve().parent.parent
SEED = ROOT / "app/src/main/java/com/example/data/db/SeedData.kt"
SUMMARIES = ROOT / "app/src/main/java/com/example/ui/screens/SummarisedGuidelinesScreen.kt"
OUT = ROOT / "public/content.js"

PRACTICE_DOMAINS = {"Haemato-Oncology", "Haemostasis & Thrombosis", "Transfusion Medicine", "General Haematology"}
RESOURCE_CATEGORIES = {"Malignant Haematology", "Haemostasis & Thrombosis", "Transfusion Medicine", "General Haematology"}

# Bundled source PDFs that may be linked from the website (one per verified guideline).
PDF_BY_RESOURCE = {
    "bsh_amyloidosis_2026": "Br_J_Haematol_-_2026_-_Garg_-_Early_diagnosis_of_AL_amyloidosis_in_haematology_cardiology_neurology_renal_and_general_b50657c2.pdf",
    "bsh_haem_gynae_2026": "Br_J_Haematol_-_2026_-_Biss_-_Implementation_and_value_of_providing_a_multidisciplinary_haematology_and_gynaecology_service_4743a72b.pdf",
    "bsh_poct_haematology_2026": "Br_J_Haematol_-_2026_-_Tam_-_Point_of_care_testing_in_general_haematology_An_update_to_the_British_Society_for_Haematology_bb4e4051.pdf",
    "bsh_ttp_laboratory_2026": "Br_J_Haematol_-_2026_-_MacDonald_-_Diagnosis_and_monitoring_of_thrombotic_thrombocytopenic_purpura_Guidance_for_c719170c.pdf",
    "bsh_pmbcl_lymphoma_2026": "Br_J_Haematol_-_2026_-_Osborne_-_The_diagnosis_and_management_of_primary_mediastinal_B_cell_lymphoma_A_British_Society_for_3361c436.pdf",
}

FALLBACK_URL = "https://b-s-h.org.uk/guidelines"


# --------------------------------------------------------------------------------------
# Minimal Kotlin literal parser (only what SeedData.kt uses)
# --------------------------------------------------------------------------------------
class Parser:
    def __init__(self, text, pos=0):
        self.t = text
        self.i = pos

    def ws(self):
        while self.i < len(self.t):
            if self.t.startswith("//", self.i):
                self.i = self.t.index("\n", self.i)
            elif self.t[self.i].isspace():
                self.i += 1
            else:
                break

    def peek(self, s):
        self.ws()
        return self.t.startswith(s, self.i)

    def expect(self, s):
        self.ws()
        if not self.t.startswith(s, self.i):
            raise ValueError(f"expected {s!r} at {self.i}: {self.t[self.i:self.i + 60]!r}")
        self.i += len(s)

    def ident(self):
        self.ws()
        m = re.compile(r"[A-Za-z_][A-Za-z0-9_]*").match(self.t, self.i)
        if not m:
            raise ValueError(f"identifier expected at {self.i}: {self.t[self.i:self.i + 60]!r}")
        self.i = m.end()
        return m.group(0)

    def string(self):
        self.ws()
        if self.t.startswith('"""', self.i):
            end = self.t.index('"""', self.i + 3)
            raw = self.t[self.i + 3:end]
            self.i = end + 3
            if self.t.startswith(".trimIndent()", self.i):
                self.i += len(".trimIndent()")
                raw = trim_indent(raw)
            return raw
        self.expect('"')
        out = []
        while True:
            c = self.t[self.i]
            if c == "\\":
                n = self.t[self.i + 1]
                out.append({"n": "\n", "t": "\t", '"': '"', "\\": "\\", "$": "$", "'": "'"}.get(n, n))
                self.i += 2
            elif c == '"':
                self.i += 1
                break
            elif c == "$":
                raise ValueError(f"string templates are not supported in seed data (at {self.i})")
            else:
                out.append(c)
                self.i += 1
        return "".join(out)

    def value(self):
        self.ws()
        if self.peek('"'):
            return self.string()
        if self.peek("listOf("):
            self.expect("listOf(")
            items = []
            while not self.peek(")"):
                items.append(self.value())
                if self.peek(","):
                    self.expect(",")
            self.expect(")")
            return items
        if self.peek("emptyList()"):
            self.expect("emptyList()")
            return []
        m = re.compile(r"-?\d+(\.\d+)?[fFL]?|true|false|null").match(self.t, self.i)
        if m:
            self.i = m.end()
            tok = m.group(0)
            if tok in ("true", "false"):
                return tok == "true"
            if tok == "null":
                return None
            tok = tok.rstrip("fFL")
            return float(tok) if "." in tok else int(tok)
        raise ValueError(f"unsupported value at {self.i}: {self.t[self.i:self.i + 60]!r}")

    def call(self, name):
        self.expect(name + "(")
        args = {}
        while not self.peek(")"):
            key = self.ident()
            self.expect("=")
            args[key] = self.value()
            if self.peek(","):
                self.expect(",")
        self.expect(")")
        return args


def trim_indent(raw):
    lines = raw.split("\n")
    if lines and not lines[0].strip():
        lines = lines[1:]
    if lines and not lines[-1].strip():
        lines = lines[:-1]
    indents = [len(l) - len(l.lstrip()) for l in lines if l.strip()]
    cut = min(indents) if indents else 0
    return "\n".join(l[cut:] for l in lines)


def parse_list(text, fun_name, ctor):
    start = text.index(f"fun {fun_name}()")
    p = Parser(text, text.index("listOf(", start) + len("listOf("))
    items = []
    while not p.peek(")"):
        items.append(p.call(ctor))
        if p.peek(","):
            p.expect(",")
    return items


def parse_pitfalls(text):
    start = text.index("fun getGuidelineExamPitfall(")
    end = text.index("else -> null", start)
    body = text[start:end]
    out = {}
    for m in re.finditer(r'"([a-z0-9_]+)" -> """(.*?)"""\.trimIndent\(\)', body, re.S):
        out[m.group(1)] = trim_indent(m.group(2))
    return out


# --------------------------------------------------------------------------------------
def build():
    seed = SEED.read_text(encoding="utf-8")
    resources = parse_list(seed, "getApprovedResources", "ApprovedResource")
    questions = parse_list(seed, "getQuestions", "Question")
    flashcards = parse_list(seed, "getFlashcards", "Flashcard")
    pitfalls = parse_pitfalls(SUMMARIES.read_text(encoding="utf-8"))

    errors = []
    res_ids = [r["id"] for r in resources]
    for kind, items in (("resource", resources), ("question", questions), ("flashcard", flashcards)):
        ids = [x["id"] for x in items]
        dupes = {i for i in ids if ids.count(i) > 1}
        if dupes:
            errors.append(f"duplicate {kind} ids: {sorted(dupes)}")
    for r in resources:
        if r["category"] not in RESOURCE_CATEGORIES:
            errors.append(f"resource {r['id']}: unknown category {r['category']!r}")
    for q in questions:
        if q["sourceResourceId"] not in res_ids:
            errors.append(f"question {q['id']}: unknown sourceResourceId {q['sourceResourceId']}")
        if q["domain"] not in PRACTICE_DOMAINS:
            errors.append(f"question {q['id']}: unknown domain {q['domain']!r}")
        opts = q.get("options", [])
        if q["questionType"] == "ESSAY":
            if q["correctIndex"] != -1 or not q.get("essayModelAnswer"):
                errors.append(f"question {q['id']}: essay needs correctIndex -1 and a model answer")
        elif not (0 <= q["correctIndex"] < len(opts)):
            errors.append(f"question {q['id']}: correctIndex {q['correctIndex']} out of range ({len(opts)} options)")
        if len(set(opts)) != len(opts):
            errors.append(f"question {q['id']}: duplicate options")
    for c in flashcards:
        if c["sourceResourceId"] not in res_ids:
            errors.append(f"flashcard {c['id']}: unknown sourceResourceId {c['sourceResourceId']}")
        if c["domain"] not in PRACTICE_DOMAINS:
            errors.append(f"flashcard {c['id']}: unknown domain {c['domain']!r}")
    for r in resources:
        url = r.get("url", "")
        if url and not url.startswith("https://"):
            errors.append(f"resource {r['id']}: url must be https")
    if errors:
        raise SystemExit("Content integrity check FAILED:\n  " + "\n  ".join(errors))

    practice_domain_for = {"Malignant Haematology": "Haemato-Oncology"}

    guidelines = []
    for r in resources:
        recs = [line.strip() for line in r["recommendations"].split("\n") if line.strip()]
        pdf = PDF_BY_RESOURCE.get(r["id"])
        guidelines.append({
            "id": r["id"],
            "title": r["title"],
            "issuingBody": r["issuingBody"],
            "year": r["year"],
            "domain": r["category"],
            "practiceDomain": practice_domain_for.get(r["category"], r["category"]),
            "isVerified": r.get("isVerified", True),
            "grade": "Checked against source PDF" if r.get("isVerified", True) else "Not yet verified",
            "citation": r["citation"],
            "pdfUrl": f"/assets/aistudio/{pdf}" if pdf else "",
            "onlineUrl": r.get("url") or FALLBACK_URL,
            "summary": r["summary"],
            "cutoffs": r["keyCutoffs"],
            "staging": r["stagingCriteria"],
            "recommendations": recs,
            "pitfalls": pitfalls.get(r["id"], ""),
        })

    web_questions = []
    for q in questions:
        stem = q["stem"]
        if q.get("labData"):
            stem += "\n\nInvestigations:\n" + q["labData"]
        if q["questionType"] == "ESSAY":
            explanation = q["essayModelAnswer"]
            if q.get("markingRubric"):
                explanation += "\n\nSuggested marking:\n" + q["markingRubric"]
        else:
            explanation = q["rationale"]
        ref_parts = [p for p in (q.get("yearPaper"), q.get("guidelineSection")) if p]
        web_questions.append({
            "id": q["id"],
            "domain": q["domain"],
            "questionType": q["questionType"],
            "title": q["title"],
            "stem": stem,
            "options": q.get("options", []),
            "correct": q["correctIndex"],
            "explanation": explanation,
            "ref": " • ".join(ref_parts),
            "evidenceGrade": q.get("evidenceGrade", ""),
            "sourceResourceId": q["sourceResourceId"],
        })

    web_cards = [{
        "id": c["id"],
        "domain": c["domain"],
        "topic": c["topic"],
        "ref": c["guidelineRef"],
        "front": c["front"],
        "back": c["back"],
        "sourceResourceId": c["sourceResourceId"],
    } for c in flashcards]

    payload = {"guidelines": guidelines, "questions": web_questions, "flashcards": web_cards}
    js = (
        "// GENERATED FILE - DO NOT EDIT BY HAND.\n"
        "// Source of truth: app/src/main/java/com/example/data/db/SeedData.kt\n"
        "// Regenerate with: python3 scripts/build_web_content.py\n"
        "window.PREPARE_CONTENT = " + json.dumps(payload, ensure_ascii=False, indent=1) + ";\n"
    )
    return js, (len(guidelines), len(web_questions), len(web_cards))


def main():
    js, counts = build()
    if "--check" in sys.argv:
        current = OUT.read_text(encoding="utf-8") if OUT.exists() else ""
        if current != js:
            print("public/content.js is out of date. Run: python3 scripts/build_web_content.py")
            sys.exit(1)
        print("public/content.js is up to date ({} guidelines, {} questions, {} flashcards)".format(*counts))
        return
    OUT.write_text(js, encoding="utf-8")
    print("Wrote {} ({} guidelines, {} questions, {} flashcards)".format(OUT.relative_to(ROOT), *counts))


if __name__ == "__main__":
    main()

#!/usr/bin/env python3
"""
Create a usability-test profile (6 weeks of answer history, a few mock results and an exam
date 30 days ahead), dated relative to today, in the app's backup format.

    python3 scripts/make_test_profile.py > test-profile.json

Load it in the app with Settings -> Restore from backup.
"""
import datetime as dt
import json
import pathlib
import random

ROOT = pathlib.Path(__file__).resolve().parent.parent
questions = [q for q in json.loads((ROOT / "content/questions.json").read_text()) if q["type"] != "ESSAY"]
rng = random.Random(42)
now = dt.datetime.now()
ms = lambda d: int(d.timestamp() * 1000)

# Different per-topic accuracy so the trends and strongest/weakest insight are meaningful.
accuracy = {"Haemostasis & Thrombosis": 0.7, "Haemato-Oncology": 0.55, "General Haematology": 0.35}
answers = []
for week in range(6, 0, -1):
    for q in rng.sample(questions, k=min(8, len(questions))):
        when = now - dt.timedelta(days=7 * week, hours=rng.randint(0, 72))
        p = accuracy.get(q["domain"], 0.5) + 0.04 * (6 - week)  # gentle improvement over time
        answers.append({"id": q["id"], "correct": rng.random() < p, "ts": ms(when)})
answers.sort(key=lambda a: a["ts"])
mocks = [{"ts": ms(now - dt.timedelta(days=d)), "correct": c, "n": 10, "pct": c * 10} for d, c in ((30, 5), (16, 6))]
data = {
    "prepare_haem_answers": json.dumps(answers),
    "prepare_haem_mocks": json.dumps(mocks),
    "prepare_haem_examDate": (now + dt.timedelta(days=30)).date().isoformat(),
}
print(json.dumps({"app": "prepare-haematology", "version": 1, "exportedAt": now.isoformat(), "data": data}, indent=2))

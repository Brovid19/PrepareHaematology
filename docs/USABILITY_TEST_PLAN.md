# Usability test plan: Prepare Haematology

## Goal
Confirm that FRCPath Haematology Part 1 candidates can find and complete their daily study quickly and without errors, and that the guideline deep links and exam planner help rather than distract.

## Participants
- **5–8 haematology trainees** (ST3–ST5) preparing for FRCPath Part 1, including at least 2 who mainly study on a phone and 1 resit candidate.
- Exclude anyone involved in building the app.
- Sessions are 30–40 minutes, remote or in person, think-aloud, recorded with consent.

## Set-up
- The deployed PWA on the participant's own device (phone or laptop), opened fresh (no saved progress).
- For tasks 4–5, generate a fresh profile with `python3 scripts/make_test_profile.py > test-profile.json` and load it with *Settings → Restore from backup*. It contains 6 weeks of answers, two past mocks and an exam date 30 days away, all relative to the test day.

## Tasks (read aloud; do not show the UI wording)

| # | Task | Success criteria | Measures |
|---|---|---|---|
| 1 | "You have 10 minutes before a clinic. Do today's most useful revision." | Starts the flashcard review from Today within 15 s and rates at least 5 cards | Time to start, cards rated, errors (e.g. "Again" chosen by mistake) |
| 2 | "Practise 5 questions on haemostasis you haven't tried before." | Uses the session builder (topic, Not yet attempted, 5) and finishes the session | Time on task, wrong turns, mis-taps before submit |
| 3 | "For the question you just got wrong, find what the guideline actually says." | Uses "Read recommendation N in the guideline", lands on the highlighted passage and can say what it says | Success, time, whether they notice the highlight |
| 4 | "Your exam is in a month. What should you do today, and how many days are left?" | Reads the planner: states days left and at least 2 targets | Success, confidence (1–5) |
| 5 | "Which topic are you weakest in, and how has it changed recently?" | Names the weakest topic from the insight line and describes the trend (or uses the table) | Success, correct interpretation |
| 6 | "Take a timed mock but stop after 3 questions, then look at your answers." | Starts the mock, uses Finish (accepts the unanswered warning), opens a review item | Success, understanding of the 65% benchmark wording |
| 7 | "Make the text easier to read at night." | Changes the theme in More → Settings | Time, path taken |

## After the tasks
- **System Usability Scale (SUS)**, 10 items. Target: mean of 75 or more.
- Three questions:
  1. What would you change first?
  2. Was anything misleading or clinically doubtful?
  3. Would you use this daily, and on which device?
- **Typography check:** is the serif text comfortable for long reading on your device? (1–5)

## Analysis
- Task success rate, time on task (median) and error count per task. Severity of each issue: 1 = cosmetic, 4 = blocks the task.
- Fix anything severity 3–4 that affects at least 2 participants before the next release, then retest tasks 1–3 with 3 new participants.

## Accessibility check (in addition)
- One session with a screen-reader user (VoiceOver on iOS or TalkBack on Android) for tasks 1–3.
- One keyboard-only session on desktop for tasks 2–3: A–E to choose, Enter to submit or go to the next question, Space and 1–4 for flashcards.

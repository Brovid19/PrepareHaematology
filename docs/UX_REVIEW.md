# UX review: Prepare Haematology (31 Oct 2026)

**Scope:** colour, typography, information architecture, study workflows and accessibility of the PWA (`public/index.html`), on desktop (1366×860) and phone (Pixel 7). Accessibility was measured with axe-core (WCAG 2 A/AA) and WCAG contrast calculations.

**About the reference sites.** `www.blood-academy.com` and `www.passmedicine.com` could not be opened from this environment because the network policy blocks them. The "inspired by" notes below come from widely known patterns of these products, not a live review. Once those domains are allowed, the comparison should be re-checked.

---

## 1. Findings (before) and fixes (after)

### Colour

| # | Issue | Why it matters | Fix shipped |
|---|---|---|---|
| C1 | **Dark-only** UI (`#0B0C10` background) with no light theme and no respect for the device setting | Long reading sessions of dense clinical text are easier on a light background for most people. Passmedicine and Blood Academy both default to light, clean pages. | Design tokens with a **light default that follows the device**, plus Dark and pure-black (OLED) options in Settings. The theme is applied before first paint, so there's no flash. |
| C2 | **Pink-red primary (`#D66371`) used for actions, while red also meant "wrong"**; white text on that pink was 3.6:1 | Learners can't tell "press me" from "you got it wrong". The contrast fails WCAG AA. | Actions use **teal from the logo's DNA strand** (5.5:1 on white). Brand red is used for identity only. Green and red are **reserved for correct and incorrect** and always come with ✓/✗ text. |
| C3 | Arbitrary status colours: "Not started" shown in green, blue and red; the "19 cards due" figure in alarm red | Colour implied success or failure where there was none. | Neutral stat tiles and progress labels. The progress fill uses one colour. |
| C4 | 70+ hard-coded hex colours and 227 inline styles | Theming was impossible and every screen drifted. | A token system (`--surface`, `--text-muted`, `--success-soft`…). Inline colours were mapped to tokens. |
| C5 | Contrast failures: red 3.8:1, badge text, the due badge 2.7:1 in dark mode | Hard to read, and fails WCAG AA. | Every text/background pairing is ≥4.5:1 in both themes, and axe now reports **0 violations**. |

### Typography

| # | Issue | Fix shipped |
|---|---|---|
| T1 | 17 different font sizes, with body text at 13–14 px (`0.8–0.88rem`) for dense clinical content | A 16 px base and a 6-step scale. Question stems, explanations and summaries are 16 px with 1.65–1.7 line height. |
| T2 | SHOUTY UPPERCASE labels with letter-spacing everywhere ("EXECUTIVE CLINICAL DIGEST & FRCPATH EXAM FOCUS:") | Sentence case throughout ("Summary", "Key cutoffs", "Exam pitfalls"). Uppercase is kept only for the small "Question/Answer" flashcard label. |
| T3 | Unlimited line length (~150 characters on desktop) | Reading width is capped at 72ch. Practice and mock screens use a centred 820 px column. |
| T4 | Flashcard answers were centre-aligned bullet lists, hard to scan | Left-aligned, 19 px text with clear Question/Answer labels. |
| T5 | Monospace timer, mixed numerals | Tabular figures for timers and stats. |

### Information architecture and navigation

| # | Issue | Fix shipped |
|---|---|---|
| N1 | **10 top-level tabs** in a horizontally scrolling strip, cut off on phones ("Spaced Re…"). Jargon ("SM-2"). Duplicates: *Guideline Library* vs *2026 PDF Library* vs *Guideline Summaries*. | **5 destinations: Today · Learn · Questions · Flashcards · More**, with section tabs inside (Learn: Summaries / Source PDFs; Questions: Practice / Timed mock / Weak spots; More: Settings / Install). The redundant "Guideline Library" panel was removed. |
| N2 | On phones the header took about 25% of the screen and the nav sat at the top, out of thumb reach | A compact header, and a **bottom tab bar on phones** with a due-cards badge on Flashcards. |
| N3 | A permanent audio bar ("Speech Synthesizer Ready • Click Listen or Play") used about 15% of the screen on every page, and the queue showed "5" before the learner had done anything | The **audio mini-player appears only when something is playing or queued**, has a close button, and the queue starts empty. |
| N4 | The page title never changed, and no item in the nav was marked as the current page | The page title follows the screen. Nav and section tabs set `aria-current`. |

### Workflow and learning experience

| # | Issue | Inspiration | Fix shipped |
|---|---|---|---|
| W1 | The dashboard was a marketing banner ("Ground your revision in official BSH guidelines…") with **"Start Timed Mock Exam" as the main action**. Mocks are the least useful daily activity. | Both platforms open on "what to do now" and progress. | A **Today** screen with one **"Your next step"** card. It picks the highest-value action: due flashcards, then weak spots, then unattempted questions, then a mock. Shortcut rows and real stats follow. |
| W2 | Practice showed **every question on one long page**, and **tapping an option instantly submitted it**, so one mis-tap marked you wrong. There was no sense of a session. | Passmedicine: pick topics and number of questions, answer one at a time, submit, read the explanation, go to the next, see a summary. | A **session builder** (topic, all/unattempted/got-wrong, 5/10/all), then **one question at a time**. You select, then **Submit**, then get feedback (✓ correct option, ✗ your option, explanation, evidence grade, **"Read the guideline summary"** link), then Next. There's a progress bar and an end-of-session summary with **"Retry the ones I got wrong"**. |
| W3 | The mock started the instant the tab opened, and the results showed only a score | Exam-style mocks with a start screen and a full review. | A **start screen** (number of questions, time, rules). Options are proper buttons and flagging is shown on the palette. There's a 5-minute warning, a confirmation before finishing with unanswered questions, and **per-question review** (your answer, correct answer, explanation). The 65% figure is labelled as a practice benchmark, not the RCPath pass mark. |
| W4 | Weak spots only linked to a long list | — | **"Retry this question"** and **"Retry all"** open a focused practice session. |
| W5 | Flashcard rating labels gave no sense of consequence, and there was no keyboard support | Spaced-repetition apps show the next interval on each button. | The buttons show the real next interval ("Good · 6 days"). The counter shows "N cards left today", and empty states explain what to do next. |
| W6 | `alert()` pop-ups for routine actions ("Flashcard added to audio queue!") | — | Non-blocking **toasts**. |
| W7 | No keyboard workflow for heavy users | Passmedicine supports fast keyboard answering. | **Practice:** A–E or 1–5 to choose, Enter to submit or go to the next question. **Flashcards:** Space to flip, 1–4 to rate, → to skip. Esc closes dialogs. Hints are shown on screen. |

### Accessibility (WCAG 2.1 AA)

| # | Issue | Fix shipped |
|---|---|---|
| A1 | Answer options were `<div onclick>`: not focusable and not announced | `<button>` with `aria-pressed`, letter badges and ✓/✗ text. |
| A2 | No focus indicator (inputs used `outline: none`) | A 3 px `:focus-visible` ring on everything. Focus moves to the content after navigation, and there's a skip link. |
| A3 | Accordion headers clickable but not keyboard-operable; buttons nested inside clickable regions | A real **Show/Hide** toggle button with `aria-expanded`/`aria-controls`. Titles are headings. |
| A4 | Settings inputs had no associated labels | `<label for>` on every control. |
| A5 | Touch targets of 28–36 px | ≥44 px buttons, 40 px palette cells, 64 px bottom-bar items. |
| A6 | Motion everywhere, ignoring the user's setting | `prefers-reduced-motion` respected. |
| **Result** | axe-core: multiple violation types on several screens | **0 WCAG A/AA violations on all 9 screens in light and dark.** |

---

## 2. Recommended next steps (not shipped: need data, content or a backend)

1. **Peer statistics on each answer** (Passmedicine's "x% of users chose this"). This needs an anonymous answer-aggregation endpoint, for example a Vercel/Cloudflare function plus KV storage, and a privacy notice.
2. **Image-based questions**, such as blood films, bone-marrow morphology and flow plots. Blood Academy's strength is visual, case-based haematology. Add `image` and `alt` fields to `content/questions.json` and render them with zoom.
3. **"Notes" linked to every question.** Partly done with "Read the guideline summary". Next, deep-link to the exact recommendation and highlight it.
4. **Topic-level progress over time**: a performance trend chart per topic and a "strongest/weakest topics" list on Today.
5. **Exam-date planner**: set an exam date, then get daily targets (new cards, questions per day) and a countdown on Today.
6. **Accounts and sync** (optional): progress currently lives on the device, with backup/restore to a file.
7. **Usability testing** with 5–8 trainees on two tasks ("do today's review", "practise 10 TTP questions"). Measure time-on-task and errors before and after.

---

## 3. Deployment

All changes are static files in `public/`. `vercel.json` already serves `public/` and sets no-cache headers for `index.html`, `sw.js`, `content.js` and the manifest. If the GitHub repository is connected to a Vercel project, each push to a branch creates a preview deployment, and a merge to the production branch deploys to production. Installed copies of the app show an **"Update available"** banner, because the build stamps a new service-worker `BUILD_ID`.

# Prepare Haematology

A Progressive Web App (PWA) for FRCPath Haematology Part 1 revision. It includes topic-by-topic guideline summaries, spaced-repetition flashcards, MCQ/EMQ practice, timed mock exams and audio read-aloud.

- **Learn by domain and topic.** *Learn* opens on a list of domains. Each domain lists its topics in paper order, and each topic page covers one section of a paper. A topic page has: why it matters, key points, numbers to know, tables, every recommendation quoted in full with its grade, exam pitfalls, a memory hook and self-test questions. Move between topics with Previous/Next, the ← → keys, or a swipe. Every topic, table and recommendation links to the page of the source PDF it comes from (`…pdf#page=N`).
- **Audio player.** Reads topics aloud at 0.75×–2×. It has a seek bar you can drag and ±10 s buttons.
- **Submit a paper to Claude.** Upload a guideline PDF, or give a link to one, and Claude drafts topics, flashcards and questions. Drafts are saved only on the learner's device and labelled *AI draft: not checked*. They can be downloaded as JSON for a maintainer to check and add (see below).

- **Installable** on Android, iOS/iPadOS and desktop (Chrome/Edge) straight from the browser. There's no app store or APK.
- **Works offline** after the first visit. Guideline PDFs are kept once opened, or you can save them all from *Install & Offline*.
- **Flashcards by domain and topic.** Browse cards grouped by domain or by paper. Each topic links to the paper section it is based on, and you can study a single topic or a whole domain.
- **Exam planner.** Set your exam date for a countdown and daily targets. Progress by topic shows an 8-week trend.
- **Private by default.** Progress is stored only on the learner's device. *Settings → Back up progress* exports it to a file, and the same page can restore it on another device.

## Project layout

```
content/                  ← SOURCE OF TRUTH for all study content (edit these)
  papers/*.json             one file per paper: guideline details, ALL its recommendations
                            (verbatim, graded, with PDF page) and its topic summary pages
  questions.json            MCQ / EMQ / essay questions (each has a topicId, optional recId)
  flashcards.json           spaced-repetition cards (each has a topicId, optional recId)
scripts/build_web_content.py  validates content/ and generates public/content.js + stamps sw.js
server/synthesise.js      the "submit a paper to Claude" endpoint (shared by both hosts)
api/synthesise.js         Vercel Function wrapper for it
worker.js                 Cloudflare Worker: /api/synthesise, then static assets
public/                   ← the deployed site
  index.html                the app (single page)
  content.js                GENERATED from content/ (do not edit)
  sw.js                     service worker (offline cache, update flow)
  site.webmanifest          PWA manifest (icons, shortcuts)
  icons/, favicon.ico       app icons (incl. maskable + Apple touch icon)
  assets/aistudio/*.pdf     source guideline PDFs (see licensing note below)
docs/AUDIT_AND_DESIGN_PLAN.md  audit, content accuracy review and roadmap
```

## Editing content

1. Edit the JSON in `content/`. Mark a guideline `"isVerified": true` only once it has been checked against its source.
   - **Papers** (`content/papers/<name>.json`) have three parts. `guideline` holds the bibliographic details and `overview`. `recommendations` lists every recommendation as `{id, group, grade, text, page}`: text verbatim, `grade` like `"1B"` or `null`, `page` = PDF page counting from 1. `topics` is ordered and becomes the topic pages. Each topic has `id, name, pages, why, keyPoints, numbers [[value, meaning]], tables [{title, page, columns, rows, note}], recs [recommendation ids], pitfalls, hook, selfTest [{q, a}]`. Use `**double asterisks**` to bold the most testable phrase. A topic takes its domain from the paper's category. To rename a topic id without breaking learners' cards, list the old id in `aliases`.
   - **Questions and flashcards** need a `sourceResourceId` (the paper), a `topicId` from that paper, and optionally a `recId`. With a `recId`, the "Read the recommendation" link opens the topic and highlights that recommendation.
   - The build fails if a recommendation is not shown on any topic page, a topic references an unknown recommendation, a card's paper or domain doesn't match its topic, or there are unbalanced `**` markers.
2. Run `npm run build` (or `python3 scripts/build_web_content.py`).
3. Commit both `content/` and the regenerated `public/content.js` and `public/sw.js`.

The build fails if content is inconsistent, for example an unknown guideline, an out-of-range answer, an unknown domain, a duplicate ID or a missing PDF. CI (`.github/workflows/content-check.yml`) runs `npm run check` and fails if the generated files are stale.

Every build stamps a new `BUILD_ID` into `sw.js`. Installed apps then show an **"Update available"** banner, and the learner chooses when to reload, so a mock exam in progress is never interrupted.

## Running locally

```bash
npm run dev
```

Then open http://localhost:8080. Service workers require `localhost` or HTTPS.

## Submitting papers to Claude (optional)

The *Submit a paper to Claude* button posts the PDF (or a link to it) to `/api/synthesise`. The endpoint calls the Claude API (`claude-opus-5-5`, with structured JSON output and the server-side refusal fallback) and streams progress back. The API key never reaches the browser. To turn it on, set these environment variables in the host's dashboard (or `wrangler secret put`):

| Variable | Purpose |
|---|---|
| `ANTHROPIC_API_KEY` | Claude API key (required) |
| `SYNTH_ACCESS_CODE` | Shared code learners type in the app, so strangers can't spend the key (required) |
| `SYNTH_MODEL` | Optional model override |

Without them, the endpoint answers *not set up* and the rest of the app works as normal. Limits: Vercel accepts request bodies up to about 4.5 MB (PDFs of about 3 MB), so use a link for larger files. Cloudflare accepts PDFs up to 30 MB. A run usually takes 1–4 minutes, and the Vercel function is allowed 300 s (`vercel.json`).

Drafts live in the learner's browser: `localStorage` for the content, IndexedDB for an uploaded PDF so page links still work. *Download JSON* exports a draft in the `content/papers` format, with the draft cards and questions under `_draftFlashcards` and `_draftQuestions`. To publish one, check every item against the PDF, add the PDF to `public/assets/aistudio/`, set `pdf` and `isVerified`, move the cards and questions into `content/flashcards.json` and `content/questions.json`, then run the build.

## Deploying

`public/` is a static site, plus the one optional API route above. Run `npm install` once to get the Claude SDK. It's configured for Cloudflare (`wrangler.toml`, `npm run deploy`, where the worker runs first so `/api/*` reaches it) and Vercel (`vercel.json`, with the function in `api/`). `index.html`, `content.js`, `sw.js` and the manifest are served with `must-revalidate` so updates propagate.

## Licensing note

The bundled BJH and RCPath PDFs in `public/assets/aistudio/` are publicly downloadable from the deployed site. Confirm you have the right to redistribute them, or replace them with DOI links (set `"pdf": null` in `content/guidelines.json` and delete the files).

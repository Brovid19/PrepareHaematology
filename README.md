# Prepare Haematology

A Progressive Web App (PWA) for FRCPath Haematology Part 1 revision. It includes guideline digests, spaced-repetition flashcards, MCQ/EMQ practice, timed mock exams and audio read-aloud.

- **Installable** on Android, iOS/iPadOS and desktop (Chrome/Edge) straight from the browser. There's no app store or APK.
- **Works offline** after the first visit. Guideline PDFs are kept once opened, or you can save them all from *Install & Offline*.
- **Exam planner.** Set your exam date for a countdown and daily targets. Progress by topic shows an 8-week trend.
- **Private by default.** Progress is stored only on the learner's device. *Settings → Back up progress* exports it to a file, and the same page can restore it on another device.

## Project layout

```
content/                  ← SOURCE OF TRUTH for all study content (edit these)
  guidelines.json           guideline digests: summary, cutoffs, staging, recommendations, pitfalls
  questions.json            MCQ / EMQ / essay questions
  flashcards.json           spaced-repetition cards
scripts/build_web_content.py  validates content/ and generates public/content.js + stamps sw.js
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

1. Edit the JSON in `content/`. Every question and card needs a `sourceResourceId` that matches a guideline `id`. Mark a guideline `"isVerified": true` only once it has been checked against its source.
   Every question and card also needs an `anchor`: the part of the guideline it is based on (`rec:N` is the Nth recommendation, counting from 0; or `cutoffs`, `staging`, `pitfall:N`, `summary`). This drives the "Read recommendation N in the guideline" links.
2. Run `npm run build` (or `python3 scripts/build_web_content.py`).
3. Commit both `content/` and the regenerated `public/content.js` and `public/sw.js`.

The build fails if content is inconsistent, for example an unknown guideline, an out-of-range answer, an unknown domain, a duplicate ID or a missing PDF. CI (`.github/workflows/content-check.yml`) runs `npm run check` and fails if the generated files are stale.

Every build stamps a new `BUILD_ID` into `sw.js`. Installed apps then show an **"Update available"** banner, and the learner chooses when to reload, so a mock exam in progress is never interrupted.

## Running locally

```bash
npm run dev
```

Then open http://localhost:8080. Service workers require `localhost` or HTTPS.

## Deploying

`public/` is a static site. It's configured for Cloudflare (`wrangler.toml`, `npm run deploy`) and Vercel (`vercel.json`). `index.html`, `content.js`, `sw.js` and the manifest are served with `must-revalidate` so updates propagate.

## Licensing note

The bundled BJH and RCPath PDFs in `public/assets/aistudio/` are publicly downloadable from the deployed site. Confirm you have the right to redistribute them, or replace them with DOI links (set `"pdf": null` in `content/guidelines.json` and delete the files).

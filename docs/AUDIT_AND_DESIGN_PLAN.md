# Prepare Haematology — Audit, Content Accuracy Review & Learner-Centred Design Plan

_Review date: 31 October 2026 · Scope: Android app (`app/`), website (`public/index.html`), Cloudflare/Vercel config, all seeded content (`SeedData.kt`, `GuidelineAiSynthesizer.kt`, website data arrays)._

**How the content was checked.** Every claim tied to one of the five 2026 BSH PDFs, or to the RCPath sample/past papers bundled in `app/src/main/assets/guidelines/`, was checked against the text pulled out of those PDFs. Those items are marked **[verified vs PDF]**. Claims about older guidelines (IMWG, WHO, BSH transfusion/HIT/APS/APL/sickle/haemochromatosis) have no source document in the repo. I checked them against standard published guidance, and they are marked **[needs SME confirmation]**. A consultant haematologist should sign these off before release.

---

## 0b. Unverified content removed (31 Oct 2026)

Rollback point: branch `backup/pre-unverified-removal-2026-10-31`.

The app now contains **only content checked against a source PDF in this repo**: the five 2026 BSH guidelines (Garg, Biss, Tam, MacDonald, Osborne), with 15 questions and 19 flashcards.

- **Guidelines removed (9):** IMWG myeloma, WHO-HAEM5, BSH blood components/irradiation, BSH HIT + UKHCDO acquired haemophilia, BSH haemochromatosis, BSH sickle transfusion, ELN APL, BSH APS and BSH/ISTH TTP.
- **Questions removed (13), all of which depended on those guidelines:**
  - the 3 RCPath sample MCQs;
  - the 5 transfusion EMQs;
  - the 3 past-paper essays (myeloma, sickle, APL);
  - HIT;
  - positive DAT.
- **Flashcards removed:** 18.
- **UI references removed:** the HIT and APS takeaways, the domain blurbs that mentioned removed topics, the search hints, and the Transfusion Medicine filter and mastery meter (that domain now has no content).
- **Kept:** the RCPath sample and past-paper PDFs in the library. They are primary documents, but no questions currently use them.
- **Learners keep progress** on the remaining cards. Cards from removed guidelines drop out of saved decks automatically.

To bring a topic back, add its guideline to `content/` with a source PDF, check every claim, set `"isVerified": true`, and rebuild.

## 0a. Architecture change: Android app removed, now a PWA (31 Oct 2026)

Rollback point: branch `backup/pre-pwa-2026-10-31` (the Android + web version with all content fixes).

- **Removed:** the Android project (`app/`, Gradle build files), the 27 MB APK in `public/downloads/`, and every link to it.
- **Content now lives in `content/*.json`**, converted from the former `SeedData.kt` with byte-identical output. The build script validates it and generates `public/content.js`. This finishes Phase 1 (one source of truth).
- **Installable PWA:**
  - Manifest with any and maskable icons, built from the existing app artwork, plus an Apple touch icon.
  - Shortcuts for Review, Practice and Mock.
  - A service worker with a versioned app-shell precache, network-first pages and content, and PDFs kept for offline reading.
  - A learner-controlled "Update available" flow.
  - An install button and per-platform install steps.
  - An offline indicator and persistent-storage request.
  - Progress backup and restore to a file.
- **Verified in headless Chromium:**
  - Chrome reports no installability or manifest errors.
  - The service worker controls the page.
  - A full reload works offline.
  - Shortcut deep links work.
  - Backup and restore round-trip correctly.
  - All 7 PDFs cache for offline use.
  - The update banner hands over to the new version.
  - All earlier study-flow tests still pass.
- **Also fixed:** two settings were saved under one key and read under another. These were mock duration and voice locale (the daily goal was not shown after reload). A first visit no longer reloads itself when the service worker takes control.
- Sections below that refer to Kotlin files describe the codebase as audited; those files no longer exist.

## 0. Fix status (applied 31 Oct 2026)

Rollback point: branch `backup/pre-fixes-2026-10-31` (commit `6b4bb40`, the state before any fixes).

**Applied**
- **Content (app + website):** every 🔴 and 🟠 item in §2 is corrected in `SeedData.kt`, the app's exam-pitfall and banner text, and the website. Unverifiable grades were removed from items tied to guidelines that have no source PDF in the repo. Those 9 resources are now flagged `isVerified = false`, shown as "Not yet verified", and their questions show "Not verified" as the evidence grade. Wrong DOIs and attributions are fixed; links I couldn't verify now point to a PubMed search instead of a guessed DOI.
- **One source of truth:** the website's content is now generated from `SeedData.kt` by `scripts/build_web_content.py` into `public/content.js`. The script also checks content integrity: unknown sources, out-of-range answer indexes, mixed domain names and duplicate IDs fail the build. A GitHub Action (`.github/workflows/content-check.yml`) fails if the two drift.
- **Data safety:** upserts replace REPLACE, so re-ingesting a guideline no longer cascade-deletes progress. Every launch syncs corrected content while keeping SM-2 progress. The racing double-seed is removed.
- **Import:** the template "AI synthesizer" is replaced. Bundled guidelines return their verified content (same IDs, no duplicates). Any other file becomes an *unverified* placeholder with no generated content. Matching uses word boundaries. The UI is relabelled "experimental", with no AI claims.
- **Learning loop:**
  - Due-mode skip fixed.
  - Due is now the default mode.
  - Buttons use correct SM-2 qualities (Again 1 / Hard 3 / Good 4 / Easy 5) and show the real next interval.
  - The Difficult drill means real lapses only.
  - Mocks exclude essays and count unanswered questions as wrong.
  - Weak spots clear once the latest attempt is correct.
  - Domain names are unified.
  - Recall filters cover every guideline.
  - Weak-spot → Practice navigation is fixed.
  - The mock length, shuffle, default review mode and default practice domain settings are now used.
- **Website:**
  - Hard-coded "88/82/75/92% mastery" and "100% retention" are replaced with real figures from the learner's own answers and reviews.
  - The fake importer is removed.
  - "View PDF" appears only where the real PDF exists.
  - Content is HTML-escaped (XSS).
  - Saved decks keep progress but pick up corrected text.
  - "PASS" is relabelled as a practice benchmark.
  - The service worker is network-first for HTML/content.
- **Tests:** new Robolectric tests cover progress-preserving re-seeding, no cascade on re-ingest, honest import, domain/verification consistency, and essay-free mocks.

**Not done (needs a decision, an Android SDK, or a clinician)**
- Android build and unit tests could not run here (no Android SDK in this environment). Kotlin changes were parse-checked only. Run `./gradlew testDebugUnitTest assembleDebug` locally or in CI.
- `public/downloads/prepare-haematology.apk` is still the **old build**; rebuild it after the tests pass.
- Licensing of the bundled BJH/RCPath PDFs (§1 #12): still hosted pending your decision.
- Consultant sign-off on the 9 "not yet verified" resources (§2.2), then set `isVerified = true`.
- Remaining Phase 2–4 items: navigation rewrite, FSRS, a separate bookmarks table (needs a schema migration), real LLM extraction with review, the remaining settings, and the site restructure.

---

## 1. Executive summary — the 12 things to fix first

| # | Severity | Finding | Where |
|---|---|---|---|
| 1 | 🔴 Critical | **The "AI synthesizer" is not AI.** It matches keywords and returns hard-coded templates. A PDF that doesn't match gets **made-up** cutoffs ("Sensitivity >95%, Specificity >92%"), recommendations labelled "Grade 1A" and a fake *Br J Haematol* citation. All of it is saved with `isVerified = true`. | `data/ai/GuidelineAiSynthesizer.kt:134-176, 432-471, 683-712, 191` |
| 2 | 🔴 Critical | **PDF text extraction doesn't work.** It reads the binary PDF as UTF-8 lines, so it gets back noise. Keyword matching on that noise is close to random. | `GuidelineAiSynthesizer.kt:28-54` |
| 3 | 🔴 Critical | **Keyword matches are far too loose.** `"tam"` matches *vitamin* or *tamoxifen*, so the upload is labelled as the Tam 2026 POCT guideline. `"who"`, `"hit"` (in *white*, *within*), `"nice"` and `"bsh"` misfire the same way. Uploads get labelled as the wrong real guideline. | `GuidelineAiSynthesizer.kt:67-133` |
| 4 | 🔴 Critical | **Re-ingesting a guideline deletes the learner's progress.** `OnConflictStrategy.REPLACE` on the parent table runs DELETE+INSERT. The `ForeignKey.CASCADE` then deletes every question and flashcard linked to it, including SM-2 history and custom cards (custom cards default to the amyloid resource). | `AppDao.kt:26-30`, `Flashcard.kt`, `Question.kt`, `ExamViewModel.kt:707` |
| 5 | 🔴 Critical | **Corrected content never reaches existing users.** `ensureDataSeeded()` only inserts IDs it hasn't seen before. Every fix in §2 stays invisible on installed devices unless content gets a version number and a migration. | `ExamRepository.kt:20-44` |
| 6 | 🟠 High | **Factual errors in the study content** (full list in §2): an inverted POCT statistic, a Factor X case that contradicts itself, "hypermagnesaemia" with arsenic, a wrong DOI, invented recommendation numbers and grades, and wrong guideline attributions. | `SeedData.kt`, `index.html` |
| 7 | 🟠 High | **Evidence grades are made up for non-BSH sources.** WHO classifications and IMWG criteria are not GRADE-rated, yet the app labels them "Grade 1A". | `SeedData.kt:46, 1244`, others |
| 8 | 🟠 High | **The website's "View PDF" opens the wrong document for 9 of 14 guidelines.** It opens the RCPath sample-MCQ PDF, and for IMWG it opens the past-paper PDF. | `public/index.html:1387-1563` |
| 9 | 🟠 High | **Two domain names for the same thing:** "Haemato-Oncology" vs "Malignant Haematology". The 10 PMBCL/APL/WHO-AML items are invisible under the Practice filter and missing from Dashboard mastery. | `PracticeScreen.kt:172`, `DashboardScreen.kt:484` vs `SeedData.kt` |
| 10 | 🟠 High | **"Due" review mode skips cards.** Once a card is rated it leaves the due list, but the index still goes up by one, so the next card is skipped. | `ExamViewModel.kt:378-392` |
| 11 | 🟠 High | **14 of 15 Settings do nothing.** Only speech rate is wired up. Mock length, default review mode, daily goal, shuffle, theme and text scale are ignored. | `SettingsManager.kt` vs all consumers |
| 12 | 🟠 High | **Copyright and licensing risk.** Institution-licensed BJH PDFs (stamped "Downloaded … by NICE") and RCPath papers are redistributed publicly from `/public/assets/` and inside the APK. | `public/assets/aistudio/`, `app/src/main/assets/guidelines/` |

---

## 2. Content accuracy screen (summaries, questions, flashcards)

Severity key: 🔴 wrong and could cost marks or harm understanding · 🟠 misattributed, overstated or inconsistent · 🟡 imprecise or a style issue.

### 2.1 Errors verified against the bundled source PDFs

| Sev | Item | What the app says | What the source says | Fix |
|---|---|---|---|---|
| 🔴 | POCT summary `SeedData.kt:161`, `fc_poct_02` (`:1130`), `q_poct_02` rationale (`:804`), synthesizer `:154,542` | "BGAs **systematically underestimate** Hb… leading to **unnecessary transfusions in up to 21%**… mean 8 g/L discrepancy" | Tam 2026: in one study, *21% of patients who would be transfused on the central-lab Hct would **not** be transfused on the BGA Hct*, so the BGA reads **higher**. The "mean 8 g/L" figure refers to **systematic overestimation**. Underestimation is reported only against the CLSI centrifugal reference method. | Rewrite: "Studies disagree; BGAs both under- and overestimate Hb. They are not interchangeable with the lab (CLIA criteria). Do not use them alone to decide on transfusion (1C)." |
| 🔴 | `q_amyloid_03` stem/labs (`:648-649`) | Factor X deficiency shown with an **isolated prolonged PT** and a **normal APTT (32 s)** | Factor X is in the common pathway, so a deficiency prolongs **both** PT and APTT. As written, the vignette contradicts its own answer. | Change APTT to prolonged (e.g. 48 s) and add "FX activity 18%". |
| 🔴 | Amyloid DOI (`SeedData.kt:21`, `ApprovedResource.kt`, synthesizer `:131`, website `:1366`) | `10.1111/bjh.20173` | PDF header: **`10.1111/bjh.70472`** | Correct it in all 4 places. |
| 🟠 | Amyloid "Rec 2" (`:20`, website) | "should be investigated **promptly**" | "should be noted and **if persistent**, then should be investigated further (1A)" | Use the guideline's wording. |
| 🟠 | Amyloid troponin units (`:19`, `q_amyloid_01` labs `:202`, `fc_01`) | "TnT <0.035 **ng/L**"; patient "**hs**-TnT 0.052 ng/L" | The PDF table also prints "ng/L", which is almost certainly a typo in the source. The Mayo/European threshold is 0.035 **µg/L** (= 35 ng/L). An hs-TnT of 0.052 ng/L would be undetectable. | Keep the guideline's numbers but add the note "0.035 µg/L (35 ng/L)". In the vignette write "TnT 0.052 µg/L (52 ng/L)". |
| 🟠 | Synthesizer amyloid branch (`GuidelineAiSynthesizer.kt:157,166,175`) | "dFLC >50 mg/L"; "Stage I median OS >10 years"; "Dara-CyBorD first-line (1B)"; "Never delay treatment… (1A)"; "Monitor cardiac biomarkers every cycle (1A)" | **None of these appear in Garg 2026.** It is a diagnosis guideline with no treatment recommendations, and Stage I survival is "not reached at 5 years". | Delete. Use the verified seed content only. |
| 🟠 | Gynae "Rec 4 / Rec 5 (Grade 2D)" (`:150`), `fc_guideline_gynae_anticoag_switch` (`:1271`) | The DOAC switch and the IUS advice are numbered "Rec 5 / Rec 4, Grade 2D" | Biss 2026 has **6 formal recommendations**, all about how the service is organised (2C/2D). The DOAC switch and IUS points are **narrative text with no grade**. The source also says "lower", not "**significantly** lower", and says the switch is for "treatment-resistant cases". | Label them "Good-practice point (ungraded)". Remove "significantly". Add "in treatment-resistant HMB". |
| 🟠 | Gynae hero banner (`SummarisedGuidelinesScreen.kt:789`) | "Tranexamic acid is first-line non-hormonal therapy" | The source only mentions "first-line haemostatic management such as tranexamic acid" when discussing referral, and makes no recommendation. | Soften to "commonly tried first". |
| 🟠 | PMBCL `q_pmbcl_02` rationale (`:922`), `fc_pmbcl_02` (`:1188`) | "Multiple large studies (**including LYSA** and IELSG37) showed R-CHOP-21 inferior" | The 23.8% vs 8.2% DS5 figure comes **only from IELSG37**, which was not powered to compare regimens. The LYSA study found **no efficacy difference** between R-CHOP-14 and DA-R-EPOCH, only more toxicity with DA-R-EPOCH. | Remove LYSA. Add "IELSG37 was not powered to compare induction regimens". |
| 🟡 | PMBCL immunophenotype (`:187`) | "CD15− (distinguishes from cHL)" | "Occasional cases are CD15+". CD30 is weak and patchy, CD10 is positive in 20–30%. | "Usually CD15−". |
| 🟡 | PMBCL VTE (`fc_pmbcl_03`, `q_pmbcl_03`) | "28% incidence of VTE **at presentation**" | "Thrombotic complications in 28% of patients" (retrospective, n=156). The text says VTE is particularly common at presentation, but 28% isn't specific to presentation. | Reword. |
| 🟠 | TTP-lab "Rec 1 (Grade 1B): do NOT wait…" (`:176`, `q_ttp_01`, `fc_ttp_01`) | Presented as a graded recommendation | Introductory prose with no grade ("not a prerequisite for immediate management"). The 1B grades belong to *"access to urgent ADAMTS13"* and *"collect into 0.109 M citrate"*. | Remove the grade from this statement. |
| 🟠 | TTP-lab grey zone (`:174-175`, `fc_ttp_01`) | "10–20 IU/dL grey zone; >20 IU/dL TTP highly unlikely", stated for all assays | The grey zone is defined for **manual FRET/ELISA only**. "A comparable grey area in the current automated platforms has not been established". **HemosIL CLEIA can give falsely low results (<10)**. The ">20 highly unlikely" row is not in the source. | Add the assay caveat. Remove the ">20" row or mark it as interpretation. |
| 🟠 | Open-conformation flashcard (`fc_ttp_03`, `:175`) | Taught as a relapse-prediction tool | Conformation assays "are **not currently recommended for routine clinical use** (2C)". | Add this recommendation to the card. |
| 🟡 | TTP-lab "citrate filled ≥90% (Grade 1B)" | Fill volume included in the 1B rec | The 1B rec is "collect into 0.109 M citrate". The ≥90% fill is in the text. | Minor rewording. |
| 🟡 | POCT "no-blame culture (Grade 1A)" (`:163`) | 1A | No-blame reporting culture is **1B** in the PDF. | Correct the grade. |
| 🟠 | RCPath sample questions (`q_rcpath_sample_01-03`, EMQ 1-5) | "According to the … **RCPath official marking scheme / sample key**" | The bundled sample PDF has **no answer key**. The keyed answers are reasonable, but the claim that they come from RCPath isn't supported. Stems were also changed (Q7: "**spherocytes** and red cell fragments" became "schistocytes and polychromasia"; Q8: "hypo/**dys**fibrinogenaemia" became hypofibrinogenaemia). | Say "Answer key: author consensus". Restore the original stems or label them "adapted". |
| 🟡 | Myeloma essay stem | Adds "Calcium 2.85" and the lab panel; leaves out the sub-questions (a)–(d) and "28 months later…" | The real paper (Sept 2019 Q2) gives Hb and creatinine only, and the parts are part of the question. | Show the parts (a)–(d) with their marks in the stem. Learners currently see a model answer to questions they were never asked. |

### 2.2 Errors against wider published guidance **[needs SME confirmation]**

| Sev | Item | Issue |
|---|---|---|
| 🔴 | APL essay complications (`SeedData.kt:608`) | "Hepatotoxicity and **hyper**magnesaemia/hypokalaemia". With arsenic trioxide the concern is **hypo**magnesaemia and hypokalaemia, which prolong QTc (keep K⁺ >4.0, Mg²⁺ >0.9 mmol/L). |
| 🔴 | Platelet thresholds (`:57,59`) | "<50 ×10⁹/L for … **lumbar puncture**". BSH 2017 (Estcourt) uses **40** for LP, 20 for CVC, 50 for major surgery/liver biopsy, 80 for epidural and 100 for neurosurgery/posterior eye. "<20 with sepsis" should read "consider 10–20 with additional risk factors". |
| 🟠 | Sickle pre-op (`:96-98`, `fc_11`, sickle essay) | Teaches "HbS <30% before major surgery" as the BSH target and says it "reduces post-op ACS from 30% to <3%". BSH 2017 (Davis) mainly recommends **top-up to Hb 100 g/L** before medium-risk surgery (TAPS trial), with exchange for selected high-risk cases. The 30%→<3% figure has no source. |
| 🟠 | HIT attributed to **UKHCDO** (`ukhcdo_haemostasis_2022`, `q_hit_01`, `fc_12`) | HIT guidance is **BSH** (Watson et al., 2012). UKHCDO covers acquired haemophilia (Collins 2013). Split into two resources. Fondaparinux is off-label for HIT in the UK, so mark it that way. |
| 🟠 | "BSH TTP Guidelines 2023" (`bsh_ttp_guidelines`) | The citation "Scully, Cataland, Coppo… JTH 2020;18:2485-95" and DOI `jth.15006` are the **ISTH 2020 TTP diagnosis guideline** (Zheng et al.). The BSH TTP guideline is Scully et al., BJH 2012 (plus the 2023 caplacizumab update). Fix the authors, body and year. |
| 🟠 | "BSH & ELN APL 2020" | The ELN 2019 recommendations (Sanz, *Blood* 2019) are not a BSH guideline. The year is inconsistent (2020 vs 2019). |
| 🟠 | APS resource "2022", citation Keeling **2012**, DOI `bjh.18386` | Year, citation and DOI don't match each other. Also mention the **2023 ACR/EULAR APS classification criteria** that replace Sydney. "DOACs contraindicated" is stronger than BSH's "not recommended" in triple-positive or arterial APS. |
| 🟠 | Haemochromatosis "updated 2021"; sickle "updated 2021"; IMWG "2021" with a 2014 citation | The updates appear to be invented. Use the real publication years. |
| 🟠 | WHO 5th "Mature B-ALL (Burkitt)" (`:44`, `fc_07`) | "Mature B-ALL" is the old FAB-L3 term. In WHO-HAEM5, Burkitt is a mature B-cell lymphoma, not a B-ALL. Also, the WHO classification doesn't issue "Rec 1–5 (Grade 1A)". In particular, "give ATRA immediately" is a **treatment** recommendation (ELN), not WHO. |
| 🟠 | WHO AML blast exceptions (`fc_guideline_who_aml_genetics`) | Incomplete and labelled "Grade 1A". WHO-HAEM5 drops the 20% cut-off for most defining genetic abnormalities (also NPM1, MECOM, NUP98, etc.) **except BCR::ABL1 and CEBPA**. The ICC 2022 uses a 10% threshold. Candidates are examined on how the two systems differ. |
| 🟡 | WHO DOI `s41375-022-01620-2` | This is the **lymphoid** paper (Alaggio). The myeloid content needs Khoury et al. (`s41375-022-01613-1`). |
| 🟡 | PNH flow (`:46`, `fc_15`) | ICCS high-sensitivity panels use **FLAER + CD24 (neutrophils)** and **FLAER + CD14 (monocytes)**, with CD59 on red cells. CD55 isn't recommended for high-sensitivity testing. |
| 🟡 | Exam format | The UI says "SBA/VIVA". FRCPath Haematology Part 1 is written papers only, with no viva. |

### 2.3 Grounding-model problems in the data

- **Wrong `sourceResourceId`.** VWD (`q_emq_transfusion_03`, `fc_guideline_vwd_type3`), TTP plasma exchange (`q_emq_transfusion_04`), the positive DAT, fibrinogen and the sickle essay/`fc_11` all point to `bsh_transfusion_components`, which says nothing about these topics. The APL essay and `fc_08` point to `who_haem_5th` instead of `bsh_apl_guidelines`. The "grounding" drawer therefore shows unrelated evidence.
- **Website and app content have drifted apart.** The website keeps its own copy (14 guidelines, 11 questions, ~25 flashcards). The app has 14 / 28 / 37. Fixes have to be made twice. → Use **one JSON content source** for both (see §5, Phase 1).

---

## 3. Code and architecture audit

### 3.1 Android app

| Sev | Issue | Location | Fix |
|---|---|---|---|
| 🔴 | REPLACE on parent rows cascades deletes to children and SM-2 progress | `AppDao.kt:26-30`, `ExamRepository.kt:147-167` | Use `@Upsert` (Room 2.5+) or `INSERT IGNORE` + `UPDATE`. Keep user state (`card_progress`, `answers`) in tables **with no FK cascade** to content. |
| 🔴 | No content versioning, so fixes never ship | `ExamRepository.ensureDataSeeded` | Add `contentVersion` to each record and a `content_meta` table. Upsert content fields only and keep the SM-2 fields. |
| 🔴 | `fallbackToDestructiveMigration()` wipes all progress on a schema change | `AppDatabase.kt:46` | Write `Migration`s and set `exportSchema = true`. |
| 🟠 | Race between the `onCreate` callback seeding and `ensureDataSeeded()` seeding | `AppDatabase.kt:47-56` | Seed in one place (repository, inside a transaction). Drop the callback. |
| 🟠 | Due-mode index skip | `ExamViewModel.kt:378-392` | In DUE mode keep the index (the list shrinks under it), or work from a session snapshot of card IDs. |
| 🟠 | `DIFFICULT_DRILL` includes every new card (`intervalDays <= 1`) | `SpacedRepetitionScreen.kt:155` | Use the lapse count or recent "Again" ratings. |
| 🟠 | Default session mode is ALL_CARDS (cramming) | `ExamViewModel.kt:104` | Default to DUE and read `settings.defaultSessionMode`. |
| 🟠 | The "Hard" rating (q=2) resets the card like "Again" | `calculateSm2` | In SM-2, 3 = hard pass. Map the buttons Again=1, Hard=3, Good=4, Easy=5, or move to **FSRS**. |
| 🟠 | "Estimated retention rate" is an invented weighted average (95/82/60) | `ExamViewModel.kt:499` | Remove it, or calculate it from real review outcomes. |
| 🟠 | The mock exam includes ESSAY items (`correctIndex = -1`) | `ExamViewModel.kt:520-523` | Filter to MCQ/EMQ. Record unanswered questions as incorrect. |
| 🟠 | Weak spots never clear: any question ever answered wrong stays a weak spot | `AppDao.kt:98` | Use the *latest* attempt per question, or a mastery rule such as 2 correct in a row. |
| 🟠 | You can only bookmark a question after answering it (the bookmark is stored on answer rows) | `AppDao.kt:92-95` | Add a separate `bookmarks` table. |
| 🟠 | Weak-spot → Practice uses the index in the **unfiltered** list while Practice indexes the filtered list | `MainActivity.kt:337` | Navigate by question ID, not index. |
| 🟠 | Recall filters are hard-coded to 5 legacy resources, so none of the 2026 guidelines can be filtered | `SpacedRepetitionScreen.kt:138-148`, `MainActivity.kt:169-178` | Build the filters from the `resources` table. |
| 🟠 | Custom cards default to `bsh_amyloidosis_2026`, so they're misattributed and get cascade-deleted | `ExamViewModel.kt:707` | Use a nullable source and a "Personal notes" pseudo-resource. |
| 🟠 | Manually ingested or "AI" resources are marked `isVerified = true` | `ExamViewModel.kt:646`, synthesizer `:191` | Default to `false`. Show an "Unverified — user-supplied" badge. |
| 🟡 | Navigation is an enum with a single global back button to Dashboard. There's no deep linking and screen state is lost on process death | `MainActivity.kt` | Navigation-Compose with a typed route per screen. Use `SavedStateHandle`. |
| 🟡 | God-ViewModel (1,170 lines) holds the UI, SR, mock, ingest, TTS and settings logic | `ExamViewModel.kt` | Split by feature: `ReviewViewModel`, `PracticeViewModel`, `MockViewModel`, `AudioController`. |
| 🟡 | Unused dependencies: `firebase-ai`, AppCheck (debug in release), Retrofit, OkHttp, Moshi. ~6 MB of PDFs bundled but never opened | `app/build.gradle.kts`, `assets/guidelines` | Remove them, or actually use the PDFs (an in-app PDF viewer with a page anchor per claim). Turn on `isMinifyEnabled` for release. |
| 🟡 | `applicationId = com.aistudio...`, namespace `com.example`, `versionCode = 1` while the APK has been rebuilt 3 times | `app/build.gradle.kts` | Use your own package, bump the version every release, and generate the APK in CI instead of committing it. |
| 🟡 | Accessibility: tiny 10 sp nav labels, a dark-only colour palette, and no content descriptions on many icon buttons | various | Respect system font scale, meet WCAG AA contrast, and add TalkBack labels. |
| 🟡 | The tests check the *template* output of the synthesizer, which locks in the problem | `ExampleRobolectricTest.kt:133` | Replace with content-integrity tests (§5, Phase 1). |

### 3.2 Website / deployment

| Sev | Issue | Fix |
|---|---|---|
| 🟠 | A single 3,248-line `index.html` with all content inline; everything in `localStorage`, so progress is lost when site data is cleared and doesn't sync with the app | Static-site generator, or a small SPA that loads a shared `content.json`. Optional account sync. |
| 🟠 | Wrong `pdfUrl` mapping (see §1 #8) | Generate the links from the shared content file. Use `null` when there's no PDF and show the DOI instead. |
| 🟠 | Heavy `innerHTML` templating of content and user input (custom cards, uploads), so stored XSS is possible from imported or user text | Use `textContent` / a templating library with escaping. Add a CSP header. |
| 🟡 | Two Wrangler configs (`wrangler.toml` and `wrangler.jsonc`) plus `vercel.json`, `_redirects` and a worker SPA fallback all do the same job | Pick one host. Delete the rest. |
| 🟡 | The service worker caches `/` cache-first with a fixed `v1` name, so users can be stuck on an old HTML version | Network-first for HTML, version the cache per build, and show an "update available" prompt. |
| 🟡 | The SPA fallback returns 200 for unknown URLs (soft 404s); the manifest points to a missing `/favicon.ico`; no sitemap, OG tags or structured data | Proper 404, real icons (192/512 maskable), `sitemap.xml`, `og:` tags. |
| 🟡 | The 27 MB APK is committed to git and served as a download | Publish through GitHub Releases or Play internal testing. Keep binaries out of git history. |

---

## 4. Use cases and personas

| Persona | Context | Core jobs-to-be-done |
|---|---|---|
| **P1 · ST3–ST5 trainee, 6–9 months out** | Commuting, on call, short sessions | Build broad coverage of the curriculum; keep up a daily review habit; listen to summaries on the move. |
| **P2 · Trainee 4–8 weeks out** | Intensive study blocks | Timed papers in exam format; find and fix weak domains; essay planning practice. |
| **P3 · Resit candidate** | Knows what went wrong last time | Diagnostic test → targeted remediation plan for the weakest domains; compare progress against the last attempt. |
| **P4 · IMG / non-UK trainee** | Unfamiliar with UK practice | UK-specific context (SaBTO, SHOT, NHS commissioning, BSH vs international guidance); a glossary. |
| **P5 · Educator / consultant** | Supervises trainees | Check content, mark errors, see cohort weak spots, assign question sets. |
| **P6 · Content editor** | Keeps the bank current | Add a new BSH guideline; link every claim to a page in the source; review and publish; withdraw superseded items. |

**Primary user stories (MVP order)**
1. *As P1, I open the app and see "Today: 23 cards due · 10-question mixed quiz · 6 min audio", and finish it in under 15 minutes.*
2. *As P2, I take a **Paper 2-format** timed mock (MCQ/EMQ, exam-like timing), then get a breakdown by curriculum domain and a remediation queue.*
3. *As P2/P3, I write an essay plan against the **real past-paper sub-questions** and compare it with a mark scheme I can tick through to score myself.*
4. *As any learner, I tap "Why?" on any fact and see the exact sentence and page of the source guideline, with its real grade or "ungraded".*
5. *As P6, I upload a new guideline, real LLM extraction proposes cards with page citations, and nothing goes live until a named reviewer approves it.*
6. *As P5, I flag a card as wrong. It goes to a review queue and is hidden after N flags until resolved.*

---

## 5. Hierarchical information architecture

### 5.1 Android app (bottom navigation with 4 destinations + profile)

```
Prepare Haematology (app)
├── 1. Today  (home: the daily study plan)
│   ├── Daily plan card: Due reviews · New cards quota · Quick quiz · Audio
│   ├── Streak & weekly goal (habit, not vanity metrics)
│   ├── Exam countdown + readiness by domain (calibrated, from real data)
│   └── "Resume where you left off"
├── 2. Learn  (understand: browse the curriculum)
│   ├── Curriculum tree (RCPath domains)
│   │   ├── Haemato-oncology ─► Topic (e.g. Myeloma)
│   │   ├── Haemostasis & thrombosis   ├── Concept summary (1 screen, layered)
│   │   ├── Transfusion                ├── Key cutoffs table
│   │   ├── General haematology        ├── Worked case / algorithm
│   │   └── Laboratory haematology     ├── Linked cards · questions · past-paper Qs
│   │                                  └── Sources (guideline → section → page)
│   ├── Guideline library (by body/year; "superseded" flags)
│   └── Glossary / UK-practice primers (for P4)
├── 3. Practise  (retrieve: test yourself)
│   ├── Review (spaced repetition: Due · New · Lapsed)
│   ├── Question bank (filters: domain, topic, type, status, source)
│   ├── Mock papers
│   │   ├── Paper 2 format (MCQ/EMQ, timed, exam-like timing)
│   │   └── Paper 1 essays (timed planning → self-mark with rubric)
│   └── Weak-spot drill (auto-generated, interleaved)
├── 4. Progress  (reflect: see how you're doing)
│   ├── Mastery heat-map by domain → topic
│   ├── Retention curve (real recall rate) & review forecast
│   ├── Confidence calibration (sure vs actually correct)
│   └── Mock history & trend
└── Profile (top-right avatar)
    ├── Exam date & study plan settings
    ├── Audio settings · Accessibility · Theme
    ├── Report a content error / my reports
    ├── Data: export / backup / sync
    └── About content: version, editorial policy, reviewers
```

**Design principles for the hierarchy**
- **No more than 3 levels deep, with no dead ends.** Every Learn topic links straight to Practise items on the same topic, and every question links back to its Learn topic.
- **Progressive disclosure:** headline fact → key cutoffs → rationale → source excerpt. This replaces the current "everything expanded" accordions.
- **Move "Sandbox" out of the learner's navigation.** It's an editor tool for P6.

### 5.2 Website

```
preparehaematology (web)
├── /                         Landing: value prop · how it works · exam dates · CTA (start / get app)
├── /study                    Logged-in web app (mirrors Android: Today · Learn · Practise · Progress)
├── /curriculum               Public SEO hub
│   ├── /curriculum/haemato-oncology
│   │   └── /curriculum/haemato-oncology/myeloma      ← topic page: summary, cutoffs, sources, 3 free Qs
│   ├── /curriculum/haemostasis-thrombosis/…
│   ├── /curriculum/transfusion/…
│   ├── /curriculum/general-haematology/…
│   └── /curriculum/laboratory-haematology/…
├── /guidelines               Guideline index (title, body, year, DOI, status: current/superseded)
│   └── /guidelines/{slug}    Structured digest + DOI link (no rehosted PDFs unless OA licence permits)
├── /exam                     About FRCPath Part 1: format, timing, dates, strategy, past-paper themes
├── /methods                  Editorial policy · how content is verified · evidence grading explained · corrections log
├── /changelog                Content & app releases (what changed and why)
├── /download                 Android app (Play / Releases), PWA install
└── /about · /privacy · /terms · /contact (report an error)
```

The public topic pages answer search queries (e.g. "AL amyloidosis staging FRCPath") and lead into the app. The logged-in `/study` area is the same product as the app and uses the same content file.

---

## 6. Optimisation plan (phased)

### Phase 0 — Safety stop (≈1 week)
1. Hide the PDF "AI analysis" and manual-ingest features from learners, or label them **"Unverified — experimental"**, and delete the else-branch templates that invent content.
2. Fix every 🔴 item in §2 in **both** `SeedData.kt` and `index.html`.
3. Add a visible **"Report an error"** button to every card and question.
4. Stop hosting the BJH/RCPath PDFs publicly until each one's licence is confirmed. Link to the DOI instead.

### Phase 1 — Content platform (2–4 weeks)
1. **One source of truth:** `content/*.json` (or YAML) with a schema: `resource`, `claim`, `card`, `question`. Every `claim` carries `{source_id, section, page, quote, grade|null, verified_by, verified_on}`.
2. Generate `SeedData` (Kotlin) and the website data from it at build time.
3. **CI content checks:** schema validation; each `sourceResourceId` exists; grades are drawn from that source's grading system (`null` for WHO/IMWG); DOI format; every `quote` must appear in the source text; domain names come from one enum; EMQ options are unique; `correctIndex` is in range.
4. **Content versioning and safe upsert** (fixes §3.1 rows 1–3); split user state from content.
5. Fix the 🟠 items in §2 and assign correct `sourceResourceId`s.

### Phase 2 — Core learning loop (4–6 weeks)
1. Build the IA in §5.1 (Navigation-Compose, feature ViewModels).
2. Fix the SR bugs. Move to **FSRS** (or a correct SM-2 with 4 buttons). Add daily new-card limits and a review queue sorted by due date.
3. Mock exams: **MCQ/EMQ only**, timing like the real exam, a review-later flag, and a review screen sorted by domain afterwards. Essays get their own timed planning mode with a rubric you tick through to mark yourself.
4. Wire up every Setting, or remove it.

### Phase 3 — Real AI, with guardrails (4–6 weeks)
1. Server-side extraction (a real PDF parser) → LLM drafts claims, cards and SBA questions **with page-anchored quotes**. Use JSON schema output, then a second-pass check that each answer is supported by the quoted text.
2. Mandatory **human review queue** (P6). Nothing is published with `verified=true` until a named reviewer approves it.
3. Automatic diff when a guideline is updated, which flags any cards built from the superseded version.

### Phase 4 — Web, sync and quality (ongoing)
1. Website rebuild per §5.2; PWA with network-first HTML.
2. Optional account and sync (progress travels between web and app).
3. Analytics that respect privacy: retention curves, item difficulty and discrimination (point-biserial) so bad questions can be retired.
4. Accessibility AA pass; light, dark and high-contrast themes; dynamic type.

**Success metrics:** 30-day retention of learners; daily-active / weekly-active; % of due reviews completed; real recall rate at review (target 85–90%); calibration error; mock score trend; content error reports per 1,000 views and median time to fix.

---

## 7. Learning-science recommendations for retention

| Principle (evidence) | Current state | Recommendation |
|---|---|---|
| **Retrieval practice** (testing effect) | Flashcards show a lot of text; "flip" invites recognition rather than recall | **One fact per card** (minimum-information principle). Ask for active recall. Use cloze cards for numbers ("ADAMTS13 < __ IU/dL"). Split the current list-style cards (e.g. "6 red-flag triggers") into 6 cards. |
| **Spacing** | SM-2 with bugs; "All cards" cramming is the default | Default to Due mode with FSRS, target recall 0.9, and new cards in daily batches of 10–20. Push reminders at a time the learner chooses. |
| **Interleaving** | Practice is filtered by domain and the order is fixed | Mixed-domain quizzes are the default; drills that put easily confused topics side by side (TTP vs HUS vs DIC; AL vs ATTR; LA vs FVIII inhibitor; WHO-HAEM5 vs ICC). |
| **Elaborative feedback** | Rationale only explains the right answer | Explain **why each distractor is wrong**. Add a "key discriminator" line and a link to the Learn topic. |
| **Confidence-based marking / metacognition** | None | Before revealing the answer, ask "Sure / Unsure / Guess". Surface "confidently wrong" items first; show a calibration chart. |
| **Desirable difficulty and generation** | Essay notes are free text | Essay mode: a timed plan (headings only, 5 min) → reveal the rubric → mark yourself point by point → the points you missed become cards automatically. |
| **Dual coding** | Text only | Algorithm diagrams (for example the TTP laboratory workup) and staging tables. Image-based questions (blood films, morphology) are **out of scope**: FRCPath Part 1 is written papers (essays and MCQ/EMQ) with no image or viva component. |
| **Worked examples → fading** | Not present | For calculations and staging (4Ts, AL-ISS, R-ISS, PLASMIC): show a worked example, then a partly completed one, then the learner does it alone. |
| **Audio for consolidation, not first exposure** | TTS reads dense summaries | Short 2–4 minute audio "pearls" with built-in pauses for recall ("pause and say the Stage IIIC criteria… now listen"). Speak units out loud ("ten to the nine per litre"). |
| **Habit design** | Streak counters with no plan | A daily plan in under 15 minutes, a streak freeze, a weekly review summary, and an exam-date countdown that adjusts how many new cards you get. |
| **Trust and transparency** | Grades invented; "verified" badge on everything | Show **real** grades, or "ungraded / good-practice point". Show the source quote and page. Publish an editorial policy and a corrections log. For a clinical exam, trust is what keeps learners coming back. |
| **Reduce cognitive load** | Very long screens; ALL-CAPS emphasis; emoji; dense colour coding | One task per screen, plain-language headings, limited use of emphasis, 16 sp body text minimum, and colour never the only signal. |

---

## Appendix A — Inventory reviewed
- Seeded content: 14 resources, 28 questions (MCQ 18, EMQ 7, essay 3), 37 flashcards.
- Domain labels in use: `Haemato-Oncology` (13), `Malignant Haematology` (10), `Haemostasis & Thrombosis` (20), `Transfusion Medicine` (12), `General Haematology` (10). The first two need merging.
- Source PDFs checked: Garg 2026 (AL amyloidosis), Biss 2026 (haem-gynae GPP), Tam 2026 (POCT GPP), MacDonald 2026 (TTP lab), Osborne 2026 (PMBCL), RCPath Part 1 sample MCQ/EMQ, RCPath Paper 1 past papers 2017–2019.
- Curriculum gaps visible in the past papers but with no content in the bank: eosinophilia, AF anticoagulation and INR derivation, mantle cell lymphoma, UFH monitoring and reversal, erythrocytosis (JAK2-negative), aplastic anaemia, PNH management, Waldenström/hyperviscosity, ITP in pregnancy, myelofibrosis, Hodgkin lymphoma, blood-donor safety, sickle cell in pregnancy, bleeding history assessment.

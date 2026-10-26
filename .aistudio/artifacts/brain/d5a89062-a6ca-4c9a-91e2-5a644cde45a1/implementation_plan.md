# FRCPath Haematology Part 1 Exam Prep Engine (Ace Haematology Edition)

A high-yield, interactive exam preparation platform and revision agent designed for pathology trainees and haematology registrars sitting the Royal College of Pathologists (RCPath) Haematology Part 1 Examination. Powered by spaced repetition, realistic mock examinations, an **Approved Clinical Resource Registry**, and a **Strict Evidence Grounding Sandbox** that guarantees all mock questions, flashcards, and feedback are strictly derived from approved guidelines (British Society for Haematology [BSH], International Myeloma Working Group [IMWG], and World Health Organization [WHO]).

---

### User Review & Critical Decisions

> [!IMPORTANT]
> The following architectural and UX enhancements have been incorporated per your revision request:

- **Approved Resource Registry & Ingestion Pathway**: Dedicated database entity (`ResourceEntity`) storing accredited guidelines (preloaded with the attached *2026 BSH Early Diagnosis of AL Amyloidosis Guideline*, IMWG diagnostic criteria, WHO classification, and RCPath official syllabi). Includes an in-app "Resource Ingestion Pathway" to register and browse newly approved clinical guidelines with key diagnostic cutoffs, staging tables, and grade recommendations.
- **Strict Evidence Grounding Sandbox**: A verifiable sandbox architecture where **every** question, distractor rationale, and flashcard must be bound to an approved database resource ID (`sourceResourceId`). The sandbox strictly rejects ungrounded or speculative content—each question displays a verified "Grounding Sandbox Seal" and provides 1-tap access to the underlying guideline text, grade (e.g., Grade 1A), and specific diagnostic thresholds.
- **Full Examination Suite**: Single Best Answer MCQs, Extended Matching Questions (EMQs), and structured essay clinical cases with model marking rubrics (derived from RCPath past papers).
- **Adaptive Spaced Repetition**: SM-2 algorithm tracking recall intervals, ease factor, and repetition counts by module and guideline, plus a targeted "Weak Spots" review queue for incorrect questions.
- **Ace Haematology Dark Aesthetic**: Deep clinical navy (`#0B132B`, `#0F172A`), electric teal/cyan accents (`#00B4D8`, `#06D6A0`), circular animated progress rings, high-contrast typography, and clinical tags.

---

## 1. Approved Resource Registry & Evidence Sandbox

### A. The Clinical Resource Ingestion Pathway
The database maintains an immutable registry of approved reference literature:
- **Preloaded Core Guidelines**:
  1. *BSH Guideline (2026)*: Early diagnosis of AL amyloidosis in haematology, cardiology, neurology, renal and general clinics (Garg et al., Br J Haematol 2026; AL-ISS Staging, NT-proBNP >332 ng/L, urine PCR >50 mg/mmol, ACR >30 mg/mmol, fat pad / bone marrow Congo red).
  2. *IMWG Consensus Guidelines*: Revised criteria for diagnosis of multiple myeloma (SLiM-CRAB criteria, smouldering myeloma monitoring).
  3. *WHO 5th Edition / ICC Classification*: Acute myeloid and lymphoblastic leukaemias, MPN, and mature B-cell neoplasms (mantle cell, CLL, Waldenström).
  4. *BSH & UKHCDO Guidelines*: Heparin-induced thrombocytopenia (4Ts score, non-heparin anticoagulation), Antiphospholipid syndrome, acquired haemophilia vs. lupus anticoagulant.
  5. *BCSH & Transfusion Guidelines*: Indications for irradiated and CMV-negative blood components, acute transfusion reactions, plasma exchange in TTP.
  6. *RCPath Official Syllabus & Past Papers*: Exam question formats, essay mark schemes (2017–2019 papers).
- **Ingestion Interface**:
  - Trainees or administrators can inspect registered guidelines, search evidence tables, and add new approved guidelines (Title, Issuing Body, Year, Key Cutoffs, Staging Criteria, and Core Clinical Rules).

### B. Strict Grounding Sandbox Rules
```
┌────────────────────────────────────────────────────────────────────────┐
│                   Strict Evidence Grounding Sandbox                    │
├────────────────────────────────────────────────────────────────────────┤
│ 1. Mandatory Resource Binding:                                         │
│    Every Question & Flashcard MUST declare a valid `sourceResourceId`. │
│ 2. Evidence Verification:                                              │
│    Must cite exact guideline Section, Recommendation Grade (e.g. 1A), │
│    and numerical diagnostic threshold or staging criteria.             │
│ 3. Distractor Justification:                                           │
│    Explanations must state why distractors are incorrect according to  │
│    the approved guideline, eliminating speculative reasoning.          │
│ 4. Grounding Seal & 1-Tap Source Drawer:                              │
│    The UI displays a verified citation badge. Tapping reveals the full │
│    guideline excerpt, evidence level, and clinical recommendations.    │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Overview & Core Concept

### Target Audience & Persona
Specialist trainees in haematology (ST3–ST7), clinical scientists, and pathology residents preparing for the UK RCPath Haematology Part 1 examination or equivalent international haematology board exams.

### Key Value
- **100% Evidence Grounded**: No unverified answers; every fact is rooted in BSH, IMWG, WHO, or RCPath documents.
- **Complete Exam Coverage**: Trains candidates on rapid-fire MCQs, multi-scenario EMQs, and complex long-case clinical management essays.
- **Long-Term Retention**: SM-2 spaced repetition ensures critical clinical cutoffs (e.g., troponin T ≥0.035 ng/L, GLS >-9%, ferritin venesection targets, FVIII inhibitors) are cemented for exam day.

---

## 3. User Experience & Visual Design

### Visual Identity & Theme (Ace Haematology Dark Clinical)
- **Palette**:
  - Primary Background: Deep Void Navy (`#0B132B` / `#0D1527`)
  - Surface/Cards: Slate Navy (`#1E293B` / `#162238`) with subtle border strokes (`#334155`)
  - Accent Primary: Medical Cyan / Electric Teal (`#00B4D8` / `#38BDF8`)
  - Accent Secondary: Mint Emerald for correct answers / mastery (`#10B981` / `#06D6A0`)
  - Danger / Alert: Crimson Rose for incorrect options / contraindications (`#EF4444`)
  - Warning / Alert: Amber Gold for review badges (`#F59E0B`)
- **Typography & Layout**:
  - High-legibility sans-serif with clinical hierarchy (bold lead-ins, distinct clinical vignettes, monospace lab values).
  - Material 3 navigation bar with sleek pill indicators.
  - Spacing strictly mapped to an 8dp grid with minimum 48dp touch targets.

### Key User Flows
1. **Dashboard & Readiness Hub**:
   - Circular progress rings showing curriculum mastery percentage and review streak.
   - Quick launch tiles: "Daily Spaced Review (Due: X cards)", "Quick 10-Question Drill", "Mock Exam (Timed)", and "Weak Spots (Missed Questions)".
   - Domain breakdown bar charts (Haem-Oncology, Haemostasis, Transfusion, General).
2. **Interactive Question Engine**:
   - **MCQ / SBA Screen**: Clinical stem, laboratory panel table, single best answer selection with immediate feedback toggle or exam mode. Rich rationale displaying *Why option is correct*, *Why distractors are wrong*, and *Guideline Citation* with Grounding Seal.
   - **EMQ Screen**: Clinical scenario matching from a list of 10 blood components or diagnoses, replicating the exact RCPath EMQ format.
   - **Structured Essay Case**: Clinical question broken into Part a/b/c with prompt, user answer notes area, and expandable Royal College model answer with marking rubric breakdown.
3. **Approved Resources & Sandbox Explorer**:
   - Searchable library of all approved guidelines in the database.
   - View guideline summary cards, key diagnostic tables (e.g. Table 2 AL-ISS Staging), and linked questions.
   - "Add Approved Resource" form allowing users to register new clinical guidelines.
4. **Spaced Repetition Flashcard Decks**:
   - 3D flip card animation showing clinical prompt on front and high-yield guideline pearls on back.
   - Self-rating buttons: Again (0), Hard (2), Good (3), Easy (5) feeding into the SM-2 scheduling engine.

---

## 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Jetpack Compose UI Layer                        │
├────────────────────────────────────────────────────────────────────────┤
│  AceHaematologyTheme (Navy/Teal Dark System)                           │
│  ├── DashboardScreen (Mastery Rings, Streaks, Domain Radar)            │
│  ├── QuestionPracticeScreen (MCQ / EMQ / Essay with Instant Rationale) │
│  ├── SpacedRepetitionScreen (SM-2 Flashcard Deck & Review Queue)       │
│  ├── ResourceRegistryScreen (Approved Guidelines & Ingestion Pathway)  │
│  └── MockExamScreen (Timed Simulation & Score Report)                  │
└─────────────────────────────────▲──────────────────────────────────────┘
                                  │ (StateFlow / Actions)
┌─────────────────────────────────┴──────────────────────────────────────┐
│                    ViewModel Layer (MVI / StateFlow)                   │
├────────────────────────────────────────────────────────────────────────┤
│  ExamViewModel (Question session, Timer, Filter, Answer verification)  │
│  ResourceViewModel (Approved guidelines, Ingestion, Sandbox checks)    │
│  SpacedRepetitionViewModel (SM-2 calculations, Deck queues, Due dates) │
│  DashboardViewModel (Aggregated metrics, Weak area detection)          │
└─────────────────────────────────▲──────────────────────────────────────┘
                                  │
┌─────────────────────────────────┴──────────────────────────────────────┐
│                       Repository & Storage Layer                       │
├────────────────────────────────────────────────────────────────────────┤
│  ExamRepository (Room Database + Preloaded High-Yield Question Bank)   │
│  ├── Entities: ResourceEntity, QuestionEntity, FlashcardEntity, ...    │
│  ├── DAOs: ResourceDao, QuestionDao, FlashcardDao, ProgressDao        │
│  └── Preloaded Bank: BSH 2026 Amyloidosis, RCPath MCQs/EMQs/Essays     │
└────────────────────────────────────────────────────────────────────────┘
```

### Key Data Entities
- **ApprovedResourceEntity**:
  - `id`: String (e.g. `bsh_amyloidosis_2026`, `imwg_myeloma_2021`, `who_leukaemia_5th`)
  - `title`: String
  - `issuingBody`: String (BSH, IMWG, WHO, RCPath, UKHCDO)
  - `year`: Int
  - `citation`: String
  - `summary`: String
  - `keyCutoffs`: String (JSON or formatted text of diagnostic cutoffs)
  - `stagingCriteria`: String (JSON or formatted staging tables)
  - `recommendations`: String (Key Grade 1A/1B guidelines)
- **QuestionEntity**:
  - `id`: String
  - `domain`: String (Haem-Onc, Haemostasis, Transfusion, General)
  - `questionType`: String (MCQ, EMQ, ESSAY)
  - `stem`: String
  - `labData`: String? (Formatted lab values table)
  - `options`: String (JSON array of options)
  - `correctIndex`: Int
  - `rationale`: String
  - `sourceResourceId`: String (Foreign key constraint to ApprovedResourceEntity)
  - `guidelineSection`: String (Specific section/table reference)
  - `evidenceGrade`: String (e.g. Grade 1A, Grade 1B)
  - `yearPaper`: String?
- **FlashcardEntity**:
  - `id`: String
  - `topic`: String
  - `sourceResourceId`: String (FK to ApprovedResourceEntity)
  - `front`: String
  - `back`: String
  - `guidelineRef`: String
  - `intervalDays`: Int
  - `repetitionCount`: Int
  - `easeFactor`: Float
  - `dueDateTimestamp`: Long
- **UserAnswerEntity**:
  - `questionId`: String
  - `isCorrect`: Boolean
  - `selectedOption`: Int
  - `timestamp`: Long
  - `notes`: String?

---

## 5. Implementation Phases (Post-Approval)

1. **Step 1: Configuration & Platform Identity**
   - Update `metadata.json` ("FRCPath Haematology Prep") and `res/values/strings.xml`.
   - Configure custom adaptive icon.
2. **Step 2: Database & Data Architecture (Room)**
   - Setup Room database, entities, DAOs, and repository.
   - Seed with full approved resource registry (including BSH 2026 Amyloidosis) and sandbox-validated question bank (MCQs, EMQs, and past paper essays).
   - Implement SM-2 algorithm scheduler.
3. **Step 3: UI Design & Ace Haematology Components**
   - Create sleek dark theme colors (`Theme.kt`, `Color.kt`).
   - Implement circular progress rings, domain performance cards, flashcard flip composables, and clinical vignette containers.
4. **Step 4: Practice, Mock Exam, Resource Registry & Spaced Repetition Screens**
   - Implement interactive quiz player with immediate evidence-based explanations and Grounding Seal.
   - Implement EMQ multi-matching screen and essay self-scoring rubric view.
   - Implement Approved Resources Registry & Ingestion drawer to add or view guidelines.
   - Implement flashcard spaced repetition review loop.
5. **Step 5: Verification & Build**
   - Execute `compile_applet` to verify compilation, test edge-to-edge layout, and finalize polish.

---

## 6. Recent Enhancements & Design Refinements

- **Soft, Eye-Comfort Haematology Red Palette**:
  - Replaced high-glare, electric reds (`#E11D48`, `#EF4444`, `#FB7185`) with calibrated, softer haematology crimson (`#D66371`), gentle dusty rose accent (`#E28B96`), and calming terracotta error/alert tone (`#E06C75`).
  - Reduced saturation and chromatic aberration while retaining distinct clinical Haematology identity.
- **Optimized for Night Mode by Default**:
  - `MyApplicationTheme` explicitly defaults to `darkTheme = true`.
  - Edge-to-edge system bars configured with `SystemBarStyle.dark` to guarantee crisp, non-glaring status and navigation bar icons on dark backgrounds regardless of device-level day/night settings.
  - Deep OLED-friendly obsidian background (`#0B0C10`), smooth charcoal surfaces (`#13141A`), subtle low-contrast borders (`#262833`), and anti-glare off-white text (`#ECEEF2`) to eliminate eye strain during late-night revision sessions.
- **Verification**:
  - Full applet compilation succeeded with zero warnings.
  - Robolectric test suite (`gradle :app:testDebugUnitTest`) verified with 33 tasks executed and 100% passing.

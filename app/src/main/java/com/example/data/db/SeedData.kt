package com.example.data.db

import com.example.data.model.ApprovedResource
import com.example.data.model.Flashcard
import com.example.data.model.Question

object SeedData {

    fun getApprovedResources(): List<ApprovedResource> = listOf(
        ApprovedResource(
            id = "bsh_amyloidosis_2026",
            title = "Early diagnosis of AL amyloidosis in haematology, cardiology, neurology, renal and general clinics: A British Society for Haematology Guideline",
            issuingBody = "British Society for Haematology",
            year = 2026,
            citation = "Garg M, Choudhuri S, Parrish C, Hawkins S, Lachmann H, Clayton L, Richardson J, Chan YLT, Wechalekar A. Br J Haematol. 2026;208(6):1931–1938.",
            category = "Malignant Haematology",
            summary = "Official 2026 BSH clinical guideline establishing early diagnostic algorithms, biomarker screening in MGUS/myeloma, non-invasive biopsy sequences, and adopting the international AL-ISS staging system. Early diagnosis yields ~80% 5-year survival vs <30% in advanced disease.",
            keyCutoffs = "• Renal: 24h proteinuria >0.5g/24h OR urine PCR >50 mg/mmol with concordant urine ACR >30 mg/mmol\n• Cardiac: NT-proBNP >332 ng/L (or unexplained progressive rise in paraproteinaemia)\n• Hepatic: Alkaline Phosphatase (ALP) >1.5x institutional ULN (raised in 86% of liver AL)\n• Neuropathy: Sustained BP fall >=20/10 mmHg by 3 min with blunted HR rise\n• Urgent Referral Triggers: Serum Albumin <25 g/L, eGFR <50 mL/min, all cardiac amyloidosis, postural drop >20 mmHg, Bilirubin >2x ULN, Factor X deficiency bleeding",
            stagingCriteria = "AL-ISS 2026 Cardiac Staging (Khwaja et al. 2026):\n• Stage I: NT-proBNP <332 ng/L & TnT <0.035 ng/L (Median survival: Not reached at 5 yr)\n• Stage II: NT-proBNP >332 but <8500 ng/L OR TnT >=0.035 ng/L (Not reached at 5 yr)\n• Stage IIIA: NT-proBNP <8500 ng/L & TnT >=0.035 ng/L (76 months)\n• Stage IIIB: NT-proBNP >8500 ng/L & TnT >=0.035 ng/L (26 months)\n• Stage IIIC: NT-proBNP >8500 ng/L, TnT >=0.035 ng/L & Global Longitudinal Strain (GLS) >-9% (4 months)\n\nRenal Staging (Palladini et al.):\n• Stage 1: Proteinuria <=5 g/24h & eGFR >=50 (0% dialysis at 2 yr)\n• Stage 2: Either proteinuria >5 g/24h OR eGFR <50 (11-25% dialysis at 2 yr)\n• Stage 3: Both proteinuria >5 g/24h & eGFR <50 (60-75% dialysis at 2 yr)",
            recommendations = "• Rec 1: Urine PCR and ACR and NT-proBNP should be added to standard tests for assessment as well as monitoring of MGUS (Grade 1A)\n• Rec 2: Falling serum albumin levels and raised alkaline phosphatase should be investigated promptly (Grade 1A)\n• Rec 3: Patients can progress to AL amyloidosis with minimal or no increase in clonal light chains (Grade 1A)\n• Rec 4: Amyloid should be screened in unexplained HFpEF, low ECG voltage with LVH, bilateral carpal tunnel, or hypotension post ACE-I/ARBs (Grade 1B)\n• Rec 5: Bone marrow or abdominal fat pad biopsy with Congo red should precede renal biopsy; renal biopsy avoided if AL confirmed elsewhere (Grade 2B)",
            url = "https://doi.org/10.1111/bjh.20173"
        ),
        ApprovedResource(
            id = "imwg_myeloma_2021",
            title = "International Myeloma Working Group (IMWG) Criteria for the Diagnosis and Staging of Multiple Myeloma",
            issuingBody = "International Myeloma Working Group",
            year = 2021,
            citation = "Rajkumar SV, Dimopoulos MA, Palumbo A, et al. Lancet Oncol. 2014;15(12):e538-e548; updated IMWG consensus.",
            category = "Malignant Haematology",
            summary = "Consensus criteria defining Multiple Myeloma based on clonal bone marrow plasma cells >=10% or biopsy-proven plasmacytoma PLUS CRAB features or SLiM myeloma-defining events.",
            keyCutoffs = "• SLiM Biomarkers (>=80% risk of progression in 2 yr):\n  - S: Sixty percent clonal bone marrow plasma cells (>=60%)\n  - Li: Light chain ratio (involved/uninvolved sFLC ratio >=100 with involved >=100 mg/L)\n  - M: MRI >1 focal bone lesion (>=5 mm in size)\n• CRAB Criteria:\n  - C: Calcium >2.75 mmol/L (or >0.25 mmol/L above ULN)\n  - R: Renal insufficiency (CrCl <40 mL/min or creatinine >177 umol/L)\n  - A: Anaemia (Hb <100 g/L or >20 g/L below normal)\n  - B: Bone lesions (>=1 osteolytic lesions on WBLDCT/PET-CT/skeletal survey)",
            stagingCriteria = "R-ISS Staging:\n• Stage I: ISS I (beta-2 microglobulin <3.5 mg/L & albumin >=35 g/L), normal LDH, standard risk FISH\n• Stage III: ISS III (beta-2 microglobulin >=5.5 mg/L) with high-risk FISH [del(17p), t(4;14), t(14;16)] OR high LDH\n• Stage II: Neither Stage I nor Stage III",
            recommendations = "• Whole-body low-dose CT (WBLDCT) is the preferred initial imaging modality over plain skeletal surveys\n• Acute renal failure in suspected myeloma requires immediate hydration, avoidance of NSAIDs/contrast, and prompt dexamethasone + bortezomib\n• Suspected cord compression requires emergency MRI of whole spine within 24 hours",
            url = "https://doi.org/10.1016/S1470-2045(14)70442-5"
        ),
        ApprovedResource(
            id = "who_haem_5th",
            title = "World Health Organization (WHO) Classification of Haematolymphoid Tumours (5th Edition)",
            issuingBody = "World Health Organization",
            year = 2022,
            citation = "Alaggio R, Amador C, Anagnostopoulos I, et al. Leukemia. 2022;36:1720–1748.",
            category = "Malignant Haematology",
            summary = "The definitive international standard for haematolymphoid neoplasm classification, detailing acute leukaemias, mature lymphoid malignancies, and myeloproliferative disorders.",
            keyCutoffs = "• AML: >=20% blasts in bone marrow or blood (or <20% if defining genetics: PML-RARA, RUNX1-RUNX1T1, CBFB-MYH11, KMT2A-rearranged, DEK-NUP214)\n• B-ALL Differentiation Stages:\n  - Pro-B: CD19+, HLA-DR+, TdT+, CD10-, Cyt Ig-, SmIg-\n  - Common ALL: CD19+, CD10+ (CALLA), TdT+, Cyt Ig-, SmIg-\n  - Pre-B ALL: CD19+, CD10+, TdT+, Cytoplasmic mu heavy chain (Cyt Ig)+, SmIg-\n  - Mature B-ALL (Burkitt): SmIg+, CD19+, CD20+, CD10+, BCL6+, TdT- (t(8;14) MYC-IGH)",
            stagingCriteria = "Subclassification of acute leukaemias by defining recurrent genetic abnormalities vs. differentiation.",
            recommendations = "• Multiparameter flow cytometry and rapid molecular testing (FISH/RT-PCR) are mandatory at baseline\n• Acute promyelocytic leukaemia (APL) with PML-RARA must be treated with ATRA as an emergency prior to confirmatory genetics",
            url = "https://doi.org/10.1038/s41375-022-01620-2"
        ),
        ApprovedResource(
            id = "bsh_transfusion_components",
            title = "British Society for Haematology: Guidelines on the use of blood components & prevention of Transfusion-Associated Graft-versus-Host Disease",
            issuingBody = "British Society for Haematology",
            year = 2020,
            citation = "BSH Transfusion Task Force: Guidelines for the clinical use of red cell, platelet, plasma and cryoprecipitate components.",
            category = "Transfusion Medicine",
            summary = "Guidelines for selection and administration of blood components, specific indications for irradiated, CMV-negative, washed products, and emergency reversal of anticoagulation.",
            keyCutoffs = "• Platelet transfusion thresholds: <10x10^9/L for stable marrow failure; <20x10^9/L with sepsis/fever; <50x10^9/L for moderate surgery/invasive procedure; <100x10^9/L for neurosurgery\n• Fibrinogen threshold: <1.5 g/L in major bleeding or obstetric haemorrhage (target >2.0 g/L in PPH)\n• Warfarin emergency reversal: Prothrombin Complex Concentrate (PCC 25-50 IU/kg) + IV Vitamin K 5-10 mg; avoid FFP unless PCC unavailable",
            stagingCriteria = "Mandatory indications for irradiated cellular components:\n• Treatment with purine analogues (fludarabine, cladribine, pentostatin) - lifelong\n• Treatment with bendamustine, clofarabine\n• Autologous and allogeneic stem cell transplant recipients\n• Hodgkin lymphoma (all stages, lifelong)\n• Intrauterine and exchange transfusions",
            recommendations = "• Purine analogue treated patients MUST receive irradiated cellular components lifelong to prevent fatal TA-GVHD (Grade 1A)\n• In acute major bleeding with elevated INR, PCC produces rapid normalization within 15 minutes compared to hours for FFP (Grade 1A)\n• Type 3 VWD requires intermediate-purity FVIII/VWF concentrate; DDAVP is ineffective (Grade 1A)",
            url = "https://b-s-h.org.uk/guidelines/guidelines/the-administration-of-blood-components"
        ),
        ApprovedResource(
            id = "ukhcdo_haemostasis_2022",
            title = "UKHCDO Guidelines: Diagnosis and Management of Heparin-Induced Thrombocytopenia (HIT) and Acquired Haemophilia A",
            issuingBody = "United Kingdom Haemophilia Centre Doctors' Organisation",
            year = 2022,
            citation = "UKHCDO Working Party Guidelines for acquired coagulation disorders, HIT, and factor inhibitors.",
            category = "Haemostasis & Thrombosis",
            summary = "Protocols for investigating unexplained thrombocytopenia post-heparin, 4Ts score calculation, HIT laboratory confirmation, and distinguishing factor inhibitors from lupus anticoagulants.",
            keyCutoffs = "• 4Ts Score: High probability (6-8), Intermediate (4-5), Low (0-3)\n• Platelet count drop >50% and nadir >=20x10^9/L 5-10 days after heparin exposure (or <1 day if heparin in past 30 days)\n• Acquired Haemophilia A vs Lupus Anticoagulant:\n  - Isolated prolonged APTT with normal PT\n  - 50:50 mixing test fails to correct in both, but FVIII inhibitor is time- and temperature-dependent (progressive prolongation after 2h at 37°C)\n  - DRVVT with platelet neutralization procedure (PNP) corrects with excess phospholipid in Lupus Anticoagulant, NOT in FVIII inhibitor",
            stagingCriteria = "Bethesda inhibitor assay: <5 Bethesda Units (BU) low-titre; >=5 BU high-titre inhibitor.",
            recommendations = "• In suspected HIT (4Ts >=4): Stop ALL heparin immediately (including flushes); switch to non-heparin alternative (Argatroban, Fondaparinux, Danaparoid); do NOT give prophylactic platelets; do NOT give Warfarin until platelets recover >150x10^9/L (Grade 1A)\n• In Acquired Haemophilia A with active bleeding: use bypassing agents (rFVIIa or FEIBA); avoid standard FVIII concentrates in high-titre inhibitors (Grade 1A)",
            url = "https://www.ukhcdo.org/guidelines/"
        ),
        ApprovedResource(
            id = "bsh_iron_haemochromatosis",
            title = "BSH Guideline: Diagnosis and management of genetic haemochromatosis and iron overload",
            issuingBody = "British Society for Haematology",
            year = 2021,
            citation = "Fitzsimons EJ, Cullis JO, Thomas DW, et al. Br J Haematol. 2018;181(4):444-455; updated 2021.",
            category = "General Haematology",
            summary = "Recommendations for investigating hyperferritinaemia, HFE genotyping (C282Y homozygotes, C282Y/H63D compound heterozygotes), liver MRI FerriScan, and venesection endpoints.",
            keyCutoffs = "• Transferrin saturation >50% in men, >45% in women indicates iron overload\n• Induction Venesection target: Serum ferritin <50 mcg/L\n• Maintenance phase target: Serum ferritin 50-100 mcg/L\n• Ferritin >1000 mcg/L, AST/ALT elevation, or platelets <200x10^9/L requires evaluation for hepatic fibrosis/cirrhosis (FibroScan or liver biopsy)",
            stagingCriteria = "Genotype categories: Type 1 HFE (C282Y/C282Y or C282Y/H63D), Type 2 Juvenile (HJV, HAMP), Type 3 TFR2, Type 4 Ferroportin.",
            recommendations = "• Venesection frequency: Typically 450-500 mL weekly or fortnightly during induction until ferritin is <50 mcg/L (Grade 1A)\n• Check Hb prior to each venesection; defer if Hb <110 g/L (Grade 1B)\n• Transferrin saturation normalises late and fluctuates; serum ferritin is the primary monitoring parameter for optimal venesection (Grade 1A)",
            url = "https://doi.org/10.1111/bjh.15164"
        ),
        ApprovedResource(
            id = "bsh_sickle_transfusion",
            title = "BSH Guideline: Red cell transfusion in sickle cell disease and management of alloimmunisation",
            issuingBody = "British Society for Haematology",
            year = 2021,
            citation = "Davis BA, Allard S, Qureshi A, et al. Br J Haematol. 2017;176(2):179-191; updated 2021.",
            category = "Transfusion Medicine",
            summary = "Standards for pre-operative transfusion, acute chest syndrome management, automated red cell exchange (apheresis), extended antigen matching, and hyperhaemolysis prevention.",
            keyCutoffs = "• Pre-op Hb S target: <30% for major/orthopaedic/cardiothoracic surgery (reduces post-op ACS from 30% to <3%)\n• Maximum post-transfusion Hb: 100 - 110 g/L (never exceed 110 g/L to avoid life-threatening hyperviscosity)\n• Extended matching: Mandatory Rh (D, C, c, E, e) and Kell (K) matching for all SCD patients",
            stagingCriteria = "Indications for automated red cell exchange vs top-up: Exchange preferred for ACS with hypoxaemia, acute stroke, pre-op when baseline Hb >90 g/L.",
            recommendations = "• Extended Rh and K matched, HbS-negative, leucodepleted blood must be provided (Grade 1A)\n• In acute chest syndrome with falling PaO2 or worsening infiltrates, prompt red cell exchange is life-saving (Grade 1A)\n• If hyperhaemolysis occurs (Hb falling below pre-transfusion level + reticulocytopenia): stop all transfusions; treat with IVIG and high-dose steroids (Grade 1A)",
            url = "https://doi.org/10.1111/bjh.14346"
        ),
        ApprovedResource(
            id = "bsh_apl_guidelines",
            title = "BSH & ELN Guidelines for the Diagnosis and Management of Acute Promyelocytic Leukaemia",
            issuingBody = "British Society for Haematology / ELN",
            year = 2020,
            citation = "Sanz MA, Fenaux P, Tallman MS, et al. Blood. 2019;133(7):645-664; adapted BSH Guidance.",
            category = "Malignant Haematology",
            summary = "Emergency management of APL with PML-RARA rearrangement, coagulopathy rescue, ATRA + ATO chemotherapy-free protocols, differentiation syndrome recognition, and molecular monitoring.",
            keyCutoffs = "• High-risk APL: WBC >10x10^9/L (risk of fatal early intracranial haemorrhage and differentiation syndrome)\n• Standard/Low-risk: WBC <=10x10^9/L\n• Platelet transfusion threshold: maintain >50x10^9/L\n• Fibrinogen threshold: maintain >1.5 g/L with cryoprecipitate or fibrinogen concentrate",
            stagingCriteria = "Sanz Risk Score (Low, Intermediate, High based on WBC and Platelet count).",
            recommendations = "• Start ATRA (45 mg/m²/day) IMMEDIATELY upon morphological suspicion; do NOT wait for cytogenetics or PML-RARA PCR (Grade 1A)\n• In Differentiation Syndrome (fever, weight gain, dyspnoea, pleural effusions): start Dexamethasone 10 mg IV BD immediately (Grade 1A)\n• Standard-risk frontline: Arsenic Trioxide (ATO) + ATRA achieves >95% cure without anthracyclines (Grade 1A)",
            url = "https://doi.org/10.1182/blood-2018-04-808006"
        ),
        ApprovedResource(
            id = "bsh_antiphospholipid",
            title = "BSH Guideline on Investigation and Management of Antiphospholipid Syndrome (APS)",
            issuingBody = "British Society for Haematology",
            year = 2022,
            citation = "Keeling D, Mackie I, Moore GW, Greer IA, Greaves M. Br J Haematol. 2012;157(1):47-58; updated Sydney criteria consensus.",
            category = "Haemostasis & Thrombosis",
            summary = "Consensus criteria for primary and secondary APS, persistent triple positivity (LA, aCL, anti-beta2GPI), obstetric APS protocols, and catastrophic APS (CAPS) therapy.",
            keyCutoffs = "• Laboratory criteria: Positive on 2 or more occasions at least 12 weeks apart:\n  1. Lupus Anticoagulant (LA) detected per ISTH criteria (screening, mixing, confirmation)\n  2. Anticardiolipin IgG/IgM (>40 GPL/MPL or >99th centile)\n  3. Anti-beta-2-glycoprotein-I IgG/IgM (>99th centile)\n• Obstetric criteria: >=3 consecutive early miscarriages (<10w), >=1 fetal death (>=10w), or premature birth <34w with severe pre-eclampsia/placental insufficiency",
            stagingCriteria = "Triple-positive APS carries the highest thrombotic risk; DOACs are contraindicated in triple-positive arterial/venous APS.",
            recommendations = "• In thrombotic APS, Warfarin (target INR 2.0-3.0) remains standard of care; DOACs are NOT recommended in triple-positive APS due to increased arterial stroke risk (Grade 1A)\n• In obstetric APS: Low-dose Aspirin (75-150 mg) + prophylactic LMWH throughout pregnancy and 6 weeks postpartum (Grade 1A)\n• Catastrophic APS (CAPS): Multimodal therapy with therapeutic anticoagulation + high-dose corticosteroids + plasma exchange +/- IVIG or eculizumab (Grade 1A)",
            url = "https://doi.org/10.1111/bjh.18386"
        ),
        ApprovedResource(
            id = "bsh_ttp_guidelines",
            title = "BSH Guidelines on the Diagnosis and Management of Thrombotic Thrombocytopenic Purpura (TTP)",
            issuingBody = "British Society for Haematology",
            year = 2023,
            citation = "Scully M, Cataland S, Coppo P, et al. J Thromb Haemost. 2020;18(10):2485-2495; BSH TTP guidelines.",
            category = "Haemostasis & Thrombosis",
            summary = "Emergency protocol for immune and congenital TTP: clinical triad/pentad, PLASMIC score, urgent therapeutic plasma exchange, caplacizumab anti-vWF nanobody, and rituximab.",
            keyCutoffs = "• Diagnostic criteria: Severe microangiopathic haemolytic anaemia (MAHA) + thrombocytopenia without other evident cause\n• ADAMTS13 activity <10% confirms TTP diagnosis\n• PLASMIC score 6-7 predicts high probability of severe ADAMTS13 deficiency",
            stagingCriteria = "Acute presentation vs Relapse; Congenital TTP (Upshaw-Schulman) vs Immune TTP.",
            recommendations = "• Do NOT wait for ADAMTS13 activity results to start treatment: commence Therapeutic Plasma Exchange (TPE) with SD-FFP (1.5 plasma volume) within 4-8 hours (Grade 1A)\n• Administer high-dose Methylprednisolone (1 g/day for 3 days or 1 mg/kg oral prednisolone) (Grade 1A)\n• Add Caplacizumab (anti-vWF) and Rituximab early to prevent early mortality, refractoriness, and relapse (Grade 1A)\n• Platelet transfusions are CONTRAINDICATED in TTP unless life-threatening bleeding is present (Grade 1A)",
            url = "https://doi.org/10.1111/jth.15006"
        )
    )

    fun getQuestions(): List<Question> = listOf(
        // Question 1: BSH 2026 AL Amyloidosis - Staging MCQ
        Question(
            id = "q_amyloid_01",
            domain = "Haemato-Oncology",
            questionType = "MCQ",
            title = "Cardiac AL Amyloidosis Staging (AL-ISS 2026)",
            stem = "A 68-year-old female with newly confirmed systemic AL amyloidosis undergoes baseline staging. Echocardiography demonstrates concentric ventricular thickening with 'apical sparing' and an abnormal global longitudinal strain (GLS) of -7%. Laboratory investigations are shown below.\n\nAccording to the AL International Staging System (AL-ISS) adopted in the 2026 BSH Guideline, what is the patient's cardiac stage and projected median survival?",
            labData = "NT-proBNP: 9,450 ng/L (NR <332)\nHigh-sensitivity Troponin T: 0.052 ng/L (NR <0.035)\neGFR: 62 mL/min/1.73m²\nUrine Albumin:Creatinine Ratio: 45 mg/mmol",
            options = listOf(
                "Stage II; Median survival not reached at 5 years",
                "Stage IIIA; Median survival approximately 76 months",
                "Stage IIIB; Median survival approximately 26 months",
                "Stage IIIC; Median survival approximately 4 months",
                "Stage IV; Median survival less than 1 month"
            ),
            correctIndex = 3,
            rationale = "According to the 2026 BSH AL Amyloidosis Guideline (Garg et al., Table 2) and the newly adopted AL-ISS system (Khwaja et al. 2026):\n• Stage IIIB is defined by NT-proBNP >8500 ng/L and Troponin T >=0.035 ng/L (median survival 26 months).\n• Stage IIIC defines ultra-poor risk, requiring NT-proBNP >8500 ng/L, Troponin T >=0.035 ng/L AND abnormal global longitudinal strain (GLS) >-9% (median survival 4 months).\nBecause this patient has NT-proBNP 9450, TnT 0.052, and GLS -7% (which is >-9%), she fulfills Stage IIIC criteria.",
            sourceResourceId = "bsh_amyloidosis_2026",
            guidelineSection = "Table 2: Staging of cardiac amyloidosis (AL-ISS 2026)",
            evidenceGrade = "Grade 1A",
            yearPaper = "BSH 2026 Core Curriculum"
        ),

        // Question 2: BSH 2026 AL Amyloidosis - Early Screening in MGUS MCQ
        Question(
            id = "q_amyloid_02",
            domain = "Haemato-Oncology",
            questionType = "MCQ",
            title = "Biomarker Screening in Monoclonal Gammopathy",
            stem = "A 62-year-old man with known IgG lambda MGUS attends annual surveillance. He feels well with no bone pain, weight loss, or dyspnoea. His serum paraprotein is stable at 5 g/L and free light chain ratio is 0.18. Routine annual screening is performed.\n\nWhich combination of screening investigations and findings should raise immediate suspicion of presymptomatic AL amyloidosis according to the 2026 BSH Guideline?",
            labData = "FBC: Hb 138 g/L, Plt 220x10^9/L, WBC 6.2x10^9/L\nSerum Albumin: 38 g/L (down from 45 g/L 12 months ago)\nUrine PCR: 62 mg/mmol\nUrine ACR: 38 mg/mmol\nNT-proBNP: 390 ng/L",
            options = listOf(
                "Findings are benign MGUS; repeat in 12 months as paraprotein is stable",
                "Concordant rise in urine PCR >50 and ACR >30 with NT-proBNP >332 ng/L indicates early renal and cardiac AL involvement; perform fat pad/bone marrow Congo red",
                "Isolated rise in NT-proBNP indicates hypertension; refer to cardiology and continue routine haematology follow-up",
                "Refer for immediate emergency autologous stem cell transplant",
                "Perform renal biopsy immediately as first-line investigation before any bone marrow or fat pad testing"
            ),
            correctIndex = 1,
            rationale = "The 2026 BSH Guideline (Recommendations for MGUS Monitoring, Grade 1A) states:\n1. Urine PCR, ACR, and NT-proBNP should be added to standard tests for assessment as well as monitoring of MGUS.\n2. Suspect kidney involvement if urine PCR >50 mg/mmol with a concordant rise in ACR >30 mg/mmol.\n3. Suspect cardiac involvement if NT-proBNP >332 ng/L (in MGUS/myeloma clinics).\n4. Falling albumin levels and raised ALP should prompt immediate investigation.\n5. In suspected AL, abdominal fat pad aspirate or bone marrow biopsy with Congo red should be performed before invasive renal biopsy (Grade 2B).",
            sourceResourceId = "bsh_amyloidosis_2026",
            guidelineSection = "Section: When to Suspect AL Amyloidosis in MGUS / Table 1",
            evidenceGrade = "Grade 1A",
            yearPaper = "BSH 2026 Core Curriculum"
        ),

        // Question 3: RCPath Sample MCQ 1 - Acute Leukaemia Immunophenotype
        Question(
            id = "q_rcpath_sample_01",
            domain = "Haemato-Oncology",
            questionType = "MCQ",
            title = "B-ALL Immunophenotypic Subtyping",
            stem = "A bone marrow examination is performed on a 48-year-old woman who presents with pancytopenia and circulating blasts in her peripheral blood film. Flow cytometry of the bone marrow reveals:\n\n• TdT: Positive\n• HLA-DR: Positive\n• CD10: Positive\n• CD19: Positive\n• Cytoplasmic Immunoglobulin (Cyt Ig): Positive\n• Surface Immunoglobulin (SmIg): Negative\n• CD33: Weak positive\n• CD34: Positive\n\nWhich type of leukaemia does this immunophenotype represent?",
            labData = "Hb: 78 g/L\nWBC: 28x10^9/L (75% blasts)\nPlatelets: 32x10^9/L",
            options = listOf(
                "B ALL",
                "Common ALL",
                "Pre-pre-B ALL",
                "Pre-B ALL",
                "Pro-B ALL"
            ),
            correctIndex = 3,
            rationale = "According to the WHO 5th Edition Classification and RCPath official marking scheme:\n• Pro-B ALL: CD19+, HLA-DR+, TdT+, CD10-, Cyt Ig-, SmIg-\n• Common ALL: CD19+, CD10+ (CALLA), TdT+, Cyt Ig negative, SmIg-\n• Pre-B ALL: Characterized by presence of cytoplasmic mu heavy chains (Cytoplasmic Ig positive), CD19+, CD10+, with surface Ig (SmIg) negative.\n• Mature B-ALL (Burkitt type): Characterized by surface immunoglobulin (SmIg) positivity and TdT negativity.\nTherefore, the expression of Cyt Ig with negative SmIg defines Pre-B ALL.",
            sourceResourceId = "who_haem_5th",
            guidelineSection = "Chapter: B-lymphoblastic leukaemia/lymphoma",
            evidenceGrade = "Consensus",
            yearPaper = "RCPath Part 1 Sample MCQ 1"
        ),

        // Question 4: RCPath Sample MCQ 2 - Genetic Haemochromatosis
        Question(
            id = "q_rcpath_sample_02",
            domain = "General Haematology",
            questionType = "MCQ",
            title = "Haemochromatosis Venesection Monitoring",
            stem = "A 39-year-old Caucasian man is referred with a serum ferritin of 1945 ng/mL and is found to be compound heterozygous for HFE C282Y and H63D. Liver function tests are normal. He is commenced on an induction venesection programme.\n\nOptimal venesection is best assessed by which of the following?",
            labData = "Serum Ferritin: 1945 ng/mL (NR 30-400)\nTransferrin Saturation: 78% (NR 15-45%)\nALT: 28 IU/L\nBilirubin: 12 umol/L",
            options = listOf(
                "Normalisation of magnetic resonance of liver",
                "Normalisation of percentage transferrin saturation",
                "Normalisation of serum ferritin",
                "Normalisation of serum ferritin and percentage transferrin saturation",
                "Normalisation of serum ferritin and serum iron"
            ),
            correctIndex = 2,
            rationale = "According to the BSH Guideline on Genetic Haemochromatosis and RCPath sample key:\n• The target for induction venesection is reduction of serum ferritin to <50 mcg/L (ng/mL).\n• Serum transferrin saturation normalises late in the course of de-ironing and fluctuates substantially with dietary intake; it is not a reliable endpoint for venesection frequency during induction.\n• Liver MRI is useful for quantifying iron overload at baseline but is not required to monitor routine venesection.\nHence, optimal venesection is assessed by normalisation of serum ferritin.",
            sourceResourceId = "bsh_iron_haemochromatosis",
            guidelineSection = "Section: Monitoring of venesection therapy",
            evidenceGrade = "Grade 1A",
            yearPaper = "RCPath Part 1 Sample MCQ 2"
        ),

        // Question 5: RCPath Sample MCQ 3 - Acquired Haemophilia vs Lupus Anticoagulant
        Question(
            id = "q_rcpath_sample_03",
            domain = "Haemostasis & Thrombosis",
            questionType = "MCQ",
            title = "Distinguishing Acquired Haemophilia A from Lupus Anticoagulant",
            stem = "A 24-year-old woman has a 2-litre primary post-partum haemorrhage. Coagulation studies show:\n• Platelet count: 240x10^9/L (Normal)\n• PT: 13 seconds (Control 12 seconds)\n• APTT Ratio: 2.9 (Markedly prolonged)\n• Fibrinogen: 3.5 g/L (Normal)\n\nWhich test will distinguish between acquired haemophilia A (Factor VIII inhibitor) and lupus anticoagulant (antiphospholipid antibody)?",
            labData = "PT: 13s\nAPTT Ratio: 2.9\nFibrinogen: 3.5 g/L\nPlatelets: 240x10^9/L",
            options = listOf(
                "Bleeding time",
                "Dilute Russell's Viper Venom Time (DRVVT) with platelet neutralisation procedure",
                "Factor VIII assay",
                "Platelet function tests",
                "Von Willebrand factor activity"
            ),
            correctIndex = 1,
            rationale = "According to UKHCDO Guidelines on Acquired Bleeding Disorders and RCPath sample key:\n• Both lupus anticoagulant and acquired factor VIII inhibitors cause an isolated prolonged APTT that fails to correct immediately on standard 50:50 mixing tests.\n• The DRVVT with platelet neutralisation procedure (or confirmatory high-phospholipid reagent) directly demonstrates phospholipid dependence: excess freeze-thawed platelets or hexagonal phase phosphatidylethanolamine will neutralize the lupus anticoagulant and shorten the clotting time, but will NOT neutralize a factor VIII inhibitor.\n• While a one-stage Factor VIII assay will show low FVIII in acquired haemophilia, lupus anticoagulant can also cause artifactual low FVIII levels due to inhibitor interference in one-stage clotting assays (requiring chromogenic assay or multiple dilutions).",
            sourceResourceId = "ukhcdo_haemostasis_2022",
            guidelineSection = "Laboratory Differential: Lupus Anticoagulant vs Specific Inhibitors",
            evidenceGrade = "Grade 1A",
            yearPaper = "RCPath Part 1 Sample MCQ 3"
        ),

        // Question 6: RCPath Sample EMQ 1 - Transfusion Warfarin ICH
        Question(
            id = "q_emq_transfusion_01",
            domain = "Transfusion Medicine",
            questionType = "EMQ",
            title = "Blood Product Selection: Warfarin-Associated Intracerebral Haemorrhage",
            stem = "A 77-year-old man presents with acute intracerebral haemorrhage. He has been taking Warfarin since undergoing mechanical aortic valve replacement 3 years earlier. His INR on admission is 3.5.\n\nFrom the standard blood component options, select the most suitable product for this patient.",
            labData = "INR: 3.5\nHb: 130 g/L\nPlatelets: 195x10^9/L\nCT Head: Acute left parietal haematoma",
            options = listOf(
                "Cryoprecipitate",
                "Fibrinogen concentrate",
                "Intermediate purity factor VIII concentrate (containing VWF)",
                "Leucocyte depleted platelet concentrate",
                "Leucocyte depleted, irradiated platelet concentrate",
                "Leucocyte depleted, CMV negative platelet concentrate",
                "Leucocyte depleted, irradiated, CMV negative platelet concentrate",
                "Methylene blue treated virally-inactivated fresh frozen plasma",
                "Prothrombin complex concentrate (containing factors II, VII, IX & X)",
                "Solvent detergent virally-inactivated fresh frozen plasma"
            ),
            correctIndex = 8,
            rationale = "According to BSH Guidelines on Transfusion & Management of Bleeding on Antithrombotic Agents:\n• For immediate reversal of life-threatening bleeding (such as intracranial haemorrhage) in patients on Warfarin, Prothrombin Complex Concentrate (PCC 25-50 IU/kg) plus intravenous Vitamin K (5-10 mg) is the treatment of choice (Grade 1A).\n• PCC rapidly normalises clotting factor levels within 10-15 minutes without causing circulatory volume overload, whereas FFP takes hours to infuse and achieves sub-therapeutic factor recovery.",
            sourceResourceId = "bsh_transfusion_components",
            guidelineSection = "Emergency Warfarin Reversal Protocol",
            evidenceGrade = "Grade 1A",
            yearPaper = "RCPath Sample EMQ Q4"
        ),

        // Question 7: RCPath Sample EMQ 2 - Transfusion CLL Fludarabine
        Question(
            id = "q_emq_transfusion_02",
            domain = "Transfusion Medicine",
            questionType = "EMQ",
            title = "Blood Product Selection: CLL Post-Fludarabine Epistaxis",
            stem = "A 76-year-old woman with chronic lymphocytic leukaemia (CLL) presents with severe epistaxis and a platelet count of 5x10^9/L. Her treatment regimen has recently been changed after an earlier course of Fludarabine.\n\nFrom the standard blood component options, select the most suitable product for this patient.",
            labData = "Platelets: 5x10^9/L\nHb: 92 g/L\nWBC: 4.2x10^9/L\nPrior therapy: Fludarabine-containing regimen",
            options = listOf(
                "Cryoprecipitate",
                "Fibrinogen concentrate",
                "Intermediate purity factor VIII concentrate (containing VWF)",
                "Leucocyte depleted platelet concentrate",
                "Leucocyte depleted, irradiated platelet concentrate",
                "Leucocyte depleted, CMV negative platelet concentrate",
                "Leucocyte depleted, irradiated, CMV negative platelet concentrate",
                "Methylene blue treated virally-inactivated fresh frozen plasma",
                "Prothrombin complex concentrate (containing factors II, VII, IX & X)",
                "Solvent detergent virally-inactivated fresh frozen plasma"
            ),
            correctIndex = 4,
            rationale = "According to BSH Guidelines on the Prevention of Transfusion-Associated Graft-versus-Host Disease (TA-GVHD):\n• All patients treated with purine analogues (e.g., Fludarabine, Cladribine, Pentostatin) must receive irradiated cellular blood components (red cells and platelets) indefinitely to prevent fatal TA-GVHD (Grade 1A).\n• Leucocyte depletion alone does not prevent TA-GVHD; gamma or X-ray irradiation (25 Gy) is essential to inactivate donor T-lymphocytes.\n• CMV testing is not routinely required for an immunocompromised elderly adult on purine analogues in the UK unless undergoing allogeneic BMT.",
            sourceResourceId = "bsh_transfusion_components",
            guidelineSection = "Indications for Irradiated Cellular Components",
            evidenceGrade = "Grade 1A",
            yearPaper = "RCPath Sample EMQ Q5"
        ),

        // Question 8: RCPath Sample EMQ 3 - Type 3 VWD in Labour
        Question(
            id = "q_emq_transfusion_03",
            domain = "Transfusion Medicine",
            questionType = "EMQ",
            title = "Blood Product Selection: Type 3 VWD in Labour",
            stem = "A 22-year-old woman with type 3 von Willebrand disease presents in active labour and requires an emergency Caesarean section. Her Factor VIIIc, von Willebrand antigen (VWF:Ag), and Ristocetin cofactor activity are all less than 10%.\n\nFrom the standard blood component options, select the most suitable product for this patient.",
            labData = "Factor VIIIc: 6%\nVWF:Ag: 4%\nRistocetin Cofactor: 5%\nPresentation: In labour, emergency C-section",
            options = listOf(
                "Cryoprecipitate",
                "Fibrinogen concentrate",
                "Intermediate purity factor VIII concentrate (containing VWF)",
                "Leucocyte depleted platelet concentrate",
                "Leucocyte depleted, irradiated platelet concentrate",
                "Leucocyte depleted, CMV negative platelet concentrate",
                "Leucocyte depleted, irradiated, CMV negative platelet concentrate",
                "Methylene blue treated virally-inactivated fresh frozen plasma",
                "Prothrombin complex concentrate (containing factors II, VII, IX & X)",
                "Solvent detergent virally-inactivated fresh frozen plasma"
            ),
            correctIndex = 2,
            rationale = "According to UKHCDO and BSH Guidelines for von Willebrand Disease:\n• Patients with severe Type 3 VWD lack both VWF and Factor VIII. Desmopressin (DDAVP) is ineffective due to lack of endogenous stores.\n• Replacement therapy requires a plasma-derived intermediate-purity Factor VIII concentrate that contains high amounts of functional von Willebrand factor (e.g., Haemate-P, Wilate) (Grade 1A).\n• Cryoprecipitate is reserved only for situations where virally-inactivated factor concentrates are unavailable.",
            sourceResourceId = "bsh_transfusion_components",
            guidelineSection = "Management of von Willebrand Disease in Obstetrics",
            evidenceGrade = "Grade 1A",
            yearPaper = "RCPath Sample EMQ Q6"
        ),

        // Question 9: RCPath Sample EMQ 4 - TTP Post-Partum Plasma Exchange
        Question(
            id = "q_emq_transfusion_04",
            domain = "Transfusion Medicine",
            questionType = "EMQ",
            title = "Blood Product Selection: Post-Partum Microangiopathic Haemolytic Anaemia",
            stem = "A 35-year-old woman develops microangiopathic haemolytic anaemia at 40 weeks gestation. The platelet count continues to fall 24 hours after delivery to 18x10^9/L. Her coagulation screen is completely normal. Blood film reveals prominent schistocytes (red cell fragments) and polychromasia. Therapeutic plasma exchange is urgently commenced.\n\nFrom the standard blood component options, select the most suitable replacement fluid for plasma exchange.",
            labData = "Platelets: 18x10^9/L\nHb: 76 g/L\nLDH: 1450 IU/L\nPT/APTT: Normal\nBlood Film: Abundant red cell fragments (schistocytes)",
            options = listOf(
                "Cryoprecipitate",
                "Fibrinogen concentrate",
                "Intermediate purity factor VIII concentrate (containing VWF)",
                "Leucocyte depleted platelet concentrate",
                "Leucocyte depleted, irradiated platelet concentrate",
                "Leucocyte depleted, CMV negative platelet concentrate",
                "Leucocyte depleted, irradiated, CMV negative platelet concentrate",
                "Methylene blue treated virally-inactivated fresh frozen plasma",
                "Prothrombin complex concentrate (containing factors II, VII, IX & X)",
                "Solvent detergent virally-inactivated fresh frozen plasma"
            ),
            correctIndex = 9,
            rationale = "According to BSH Guidelines on the Diagnosis and Management of Thrombotic Thrombocytopenic Purpura (TTP):\n• Therapeutic Plasma Exchange (TPE) with Solvent Detergent Fresh Frozen Plasma (SD-FFP, Octaplas) is the gold-standard treatment of choice for TTP (Grade 1A).\n• SD-FFP replenishes functional ADAMTS13 protease while removing ADAMTS13 autoantibodies and ultra-large VWF multimers.\n• In the UK, SD-FFP is preferred over standard single-donor FFP due to its reduced risk of pathogen transmission and lower incidence of allergic/TRALI reactions.\n• Platelet transfusions are contraindicated in TTP unless life-threatening haemorrhage is present.",
            sourceResourceId = "bsh_transfusion_components",
            guidelineSection = "TTP Therapeutic Plasma Exchange Replacement Fluids",
            evidenceGrade = "Grade 1A",
            yearPaper = "RCPath Sample EMQ Q7"
        ),

        // Question 10: RCPath Sample EMQ 5 - Hypofibrinogenaemia Surgery
        Question(
            id = "q_emq_transfusion_05",
            domain = "Transfusion Medicine",
            questionType = "EMQ",
            title = "Blood Product Selection: Hypofibrinogenaemia Elective Surgery",
            stem = "A 28-year-old man with inherited hypofibrinogenaemia requires an elective mastoidectomy. Baseline fibrinogen level is 0.6 g/L.\n\nFrom the standard blood component options, select the most suitable product to achieve surgical hemostasis.",
            labData = "Clauss Fibrinogen: 0.6 g/L (NR 1.5 - 4.5)\nPT: 16s (slight prolongation due to low substrate)\nAPTT: 39s\nPlatelets: 210x10^9/L",
            options = listOf(
                "Cryoprecipitate",
                "Fibrinogen concentrate",
                "Intermediate purity factor VIII concentrate (containing VWF)",
                "Leucocyte depleted platelet concentrate",
                "Leucocyte depleted, irradiated platelet concentrate",
                "Leucocyte depleted, CMV negative platelet concentrate",
                "Leucocyte depleted, irradiated, CMV negative platelet concentrate",
                "Methylene blue treated virally-inactivated fresh frozen plasma",
                "Prothrombin complex concentrate (containing factors II, VII, IX & X)",
                "Solvent detergent virally-inactivated fresh frozen plasma"
            ),
            correctIndex = 1,
            rationale = "According to BSH & UKHCDO Guidelines on the Management of Rare Inherited Bleeding Disorders:\n• In congenital hypo- or afibrinogenaemia requiring prophylaxis for elective surgery, pasteurized virally-inactivated Fibrinogen Concentrate (e.g., Riastap / Fibryga) is the treatment of choice (Grade 1A).\n• Target fibrinogen trough level is typically >=1.5 g/L to 2.0 g/L for major surgery.\n• Fibrinogen concentrate provides precise dosing with minimal volume and carries a vastly lower risk of viral transmission and allergic reactions compared to cryoprecipitate.",
            sourceResourceId = "bsh_transfusion_components",
            guidelineSection = "Treatment of Congenital Fibrinogen Disorders",
            evidenceGrade = "Grade 1A",
            yearPaper = "RCPath Sample EMQ Q8"
        ),

        // Question 11: RCPath 2019 Paper 1 Essay - Multiple Myeloma Acute Management & Spinal Cord Compression
        Question(
            id = "q_essay_myeloma_2019",
            domain = "Haemato-Oncology",
            questionType = "ESSAY",
            title = "Multiple Myeloma Presentation, Acute Renal Injury & Cord Compression",
            stem = "A previously fit and healthy 62-year-old man presents with fatigue and back pain. Laboratory testing reveals Hb 82 g/L, Creatinine 350 umol/L, Calcium 2.85 mmol/L. Multiple myeloma is suspected.\n\n[RCPath 2019 Paper 1, Question 2 - 25 Marks Total]",
            labData = "Hb: 82 g/L\nCreatinine: 350 umol/L (eGFR 16 mL/min)\nCalcium: 2.85 mmol/L\nSerum Albumin: 31 g/L\nTotal Protein: 98 g/L",
            options = emptyList(),
            correctIndex = -1,
            rationale = "This is a past FRCPath Part 1 essay question examining urgent acute myeloma management, imaging modalities, frontline therapy, and metastatic spinal cord compression (MSCC). Grounded in BSH Myeloma and IMWG guidelines.",
            sourceResourceId = "imwg_myeloma_2021",
            guidelineSection = "Section: Myeloma Emergencies & SLiM-CRAB Criteria",
            evidenceGrade = "Grade 1A",
            yearPaper = "RCPath Paper 1 September 2019 (Q2)",
            essayModelAnswer = """
PART (a): Initial Investigations and Urgent Acute Management (10 marks)
• Initial Investigations:
  - FBC, peripheral film (rouleaux).
  - Renal function, U&Es, electrolytes, serum calcium (adjusted), albumin, uric acid, LDH, Beta-2 microglobulin.
  - Serum protein electrophoresis (SPEP) and immunofixation (IFE).
  - Serum free light chain (sFLC) assay (kappa, lambda, ratio).
  - Urine protein electrophoresis (UPEP) and immunofixation for Bence Jones protein; urine ACR/PCR.
  - Bone marrow aspirate and trephine biopsy with cytogenetics/FISH [t(4;14), t(14;16), del(17p), 1q gain].
  - Coagulation screen.
• Urgent Acute Management:
  - Vigorous IV hydration with 0.9% normal saline (target 3 L/day urine output).
  - Management of Hypercalcaemia: IV saline hydration + IV bisphosphonate (dose-adjusted zoledronic acid or pamidronate) or denosumab in severe renal failure.
  - Management of Acute Kidney Injury (myeloma cast nephropathy): Avoid all nephrotoxic agents (NSAIDs, ACE-I/ARBs, aminoglycosides) and iodinated radiocontrast.
  - Prompt anti-myeloma induction: High-dose Dexamethasone (20-40 mg weekly) + Bortezomib (subcutaneous; requires no dose reduction in renal failure).
  - Consider urgent plasmapheresis or high cut-off haemodialysis if rapid light chain reduction required.
  - Analgesia (paracetamol, titrated opioids; avoid NSAIDs).
  - Thromboprophylaxis and VZV/PJP prophylaxis (aciclovir, co-trimoxazole).

PART (b): Recommended Imaging Options in Newly Diagnosed Myeloma (4 marks)
• Whole-Body Low-Dose CT (WBLDCT): First-line imaging of choice according to BSH, NICE, and IMWG guidelines. Superior sensitivity for osteolytic lesions compared to plain radiographs, with low radiation dose.
• Whole-Body or Whole-Spine MRI: Preferred modality for assessing suspected spinal cord compression, nerve root compromise, solitary plasmacytoma, or diffuse marrow infiltration.
• 18F-FDG PET-CT: Highly sensitive for detecting active extramedullary disease, identifying dynamic metabolic response, and evaluating smouldering vs active disease.
• Conventional Skeletal Survey: Historically used, but now superseded due to low sensitivity (requires 30-50% bone mineral loss to detect lesion).

PART (c): First-Line Therapy Options and Guiding Factors (6 marks)
• Transplant-Eligible (fit 62yo):
  - Standard UK standard of care: Quadruplet or Triplet induction: Daratumumab + Bortezomib + Lenalidomide + Dexamethasone (Dara-VRd) or Bortezomib + Thalidomide/Lenalidomide + Dexamethasone (VTD / VRd).
  - Followed by high-dose Melphalan (140-200 mg/m²) and Autologous Stem Cell Transplantation (ASCT).
  - Maintenance with Lenalidomide.
• Guiding Factors:
  - Age and Performance Status (ECOG) / Frailty score.
  - Renal function: Bortezomib and Dexamethasone can be given full dose; Lenalidomide requires careful dose-adjustment for eGFR.
  - Cytogenetic risk profile: High-risk FISH [del(17p), t(4;14), t(14;16)] benefits from proteasome inhibitor maintenance.
  - Patient preference and venous access.

PART (d): Sudden Left Leg Weakness and Urinary Incontinence (5 marks)
• Likely Diagnosis: Metastatic Spinal Cord Compression (MSCC) / Cauda Equina Syndrome.
• Initial Management Plan:
  - Immediate high-dose intravenous Dexamethasone (16 mg stat, then 8 mg BD) with PPI gastroprotection.
  - Emergency whole-spine MRI scan to be performed within 24 hours (target <12 hours).
  - Flat bed rest with log-rolling until spinal stability is assessed.
  - Immediate referral to the acute MSCC coordinator and spinal surgical / orthopaedic oncology team for consideration of urgent surgical decompression and stabilization.
  - Urgent Clinical Oncology review for emergency radiotherapy if surgery contraindicated.
  - Catheterisation for urinary retention and monitor neurological deficits hourly.
            """.trimIndent(),
            markingRubric = "Part a: 10 marks (5 marks labs/investigations, 5 marks acute hypercalcaemia/renal management)\nPart b: 4 marks (1 mark per modality with clear rationale: WBLDCT, MRI, PET-CT, plain film limitations)\nPart c: 6 marks (3 marks regimen specification, 3 marks factors: renal, fitness, cytogenetics)\nPart d: 5 marks (1 mark diagnosis MSCC, 1 mark dexamethasone, 1 mark emergency MRI, 1 mark spinal surgical referral, 1 mark supportive care)"
        ),

        // Question 12: RCPath 2019 Paper 1 Essay - Sickle Cell Pre-Op & Transfusion
        Question(
            id = "q_essay_sickle_2019",
            domain = "Transfusion Medicine",
            questionType = "ESSAY",
            title = "Sickle Cell Disease Orthopaedic Surgery, Transfusion & Jehovah's Witness Counseling",
            stem = "A 54-year-old male with Hb SS, recurrent crises, history of acute chest syndrome, and pulmonary hypertension is admitted following a mechanical fall with a fractured neck of femur. He needs urgent orthopaedic surgery. He is blood group B RhD negative. Hb S was 84% 4 months ago, and Hb at admission is 90 g/L. He is reluctant to accept transfusion due to blood safety concerns and his wife is a Jehovah's Witness.\n\n[RCPath 2019 Paper 1, Question 1 - 25 Marks Total]",
            labData = "Hb: 90 g/L\nHb S: 84%\nBlood Group: B RhD Negative\nDiagnosis: Right neck of femur fracture; pulmonary hypertension",
            options = emptyList(),
            correctIndex = -1,
            rationale = "FRCPath Part 1 essay testing haemoglobinopathy peri-operative optimization, red cell exchange vs simple transfusion, blood safety ethics, extended red cell phenotyping, and transfusion targets per BSH Sickle Cell Guidelines.",
            sourceResourceId = "bsh_transfusion_components",
            guidelineSection = "BSH Guidelines: Transfusion in Sickle Cell Disease",
            evidenceGrade = "Grade 1A",
            yearPaper = "RCPath Paper 1 September 2019 (Q1)",
            essayModelAnswer = """
PART (a): Managing Concerns, Optimization, and Complications (20 marks)
• Managing Concerns and Ethics:
  - Establish patient's individual capacity and personal wishes independent of family pressure (clarify whether the patient himself is a baptized Jehovah's Witness or holds personal refusal).
  - Explore specific fears regarding transfusion safety (infection, reactions). Reassure regarding modern UK screening (NAT testing, virally safe blood supply).
  - If the patient has capacity and refuses blood: Respect advance decision / refusal. Discuss acceptable fractions and bloodless techniques (cell salvage with unbroken continuous circuit, acute normovolaemic haemodilution, tranexamic acid).
  - Emphasize the high risk of catastrophic peri-operative sickling and Acute Chest Syndrome (ACS) without transfusion in major orthopaedic surgery.
• Pre-Operative Optimization:
  - Multi-disciplinary team (Haematologist, Orthopaedic surgeon, Anaesthetist, Intensive care).
  - Pain control (multimodal, PCA).
  - Maintain optimal hydration with IV fluids (avoid dehydration and fluid overload in pulmonary hypertension).
  - Maintain arterial oxygenation (keep SpO2 >=95% with supplemental oxygen).
  - Prevent hypothermia (forced air warming blankets in theatre).
  - Thromboprophylaxis: Mechanical and chemical LMWH (timed around regional/neuraxial anaesthesia).
  - Pre-operative exchange transfusion (automated red cell exchange / apheresis is preferred over manual exchange).
• Potential Complications:
  - Sickle-related: Acute chest syndrome, vaso-occlusive crisis, stroke, worsening pulmonary hypertension / right heart failure, osteomyelitis, fat embolism syndrome.
  - Transfusion-related:
    * Red cell alloimmunization (high risk in SCD; up to 30-50%).
    * Delayed haemolytic transfusion reaction (DHTR) with hyperhaemolysis (destruction of both donor and autologous red cells, falling Hb below baseline).
    * Transfusion-related acute lung injury (TRALI) and Transfusion-associated circulatory overload (TACO).
    * Iron overload (in repeated transfusions).
    * Febrile non-haemolytic reactions.

PART (b): Blood Component Specification, Volume, and Targets (5 marks)
• Specification of Red Cells:
  - ABO and RhD compatible (B RhD Negative).
  - Extended phenotyped/matched for Rh (D, C, c, E, e) and Kell (K) antigens to prevent alloimmunization.
  - Matched for any historical red cell antibodies.
  - Hb S negative units (donor tested negative for sickle trait).
  - Leucocyte depleted (standard UK blood service).
  - Cytomegalovirus (CMV) negative if pregnant or transplant candidate; fresh units (<10-14 days old).
• Exchange Volume and Targets:
  - Target pre-operative Hb S: <30% (reduces risk of ACS and peri-operative mortality).
  - Target final total Hb: 100 - 110 g/L (never exceed 110 g/L to prevent hyperviscosity crisis).
  - Red Cell Volume calculation: Typically 6-8 units (approx. 1.5 red cell volumes exchanged during automated apheresis, based on total blood volume = 70 mL/kg * weight * [1 - Hct]).
            """.trimIndent(),
            markingRubric = "Part a: 20 marks (6 marks ethical/safety counseling, 6 marks medical optimization, 8 marks detailed sickle & transfusion complications)\nPart b: 5 marks (2 marks extended matching/HbS-neg specs, 2 marks targets HbS <30% and Hb <110 g/L, 1 mark volume estimation)"
        ),

        // Question 13: RCPath 2018 Paper 1 Essay - Acute Promyelocytic Leukaemia
        Question(
            id = "q_essay_apl_2018",
            domain = "Haemato-Oncology",
            questionType = "ESSAY",
            title = "Acute Promyelocytic Leukaemia Management, Differentiation Syndrome & Molecular Relapse",
            stem = "A 22-year-old woman presents with a 2-week history of bruising on her limbs and epistaxis. Full blood count reveals Hb 96 g/L, WBC 135x10^9/L, Platelets 20x10^9/L. Blood film demonstrates numerous immature myeloid cells with folded nuclei, heavy granulation, and infrequent Auer rods. FISH confirms PML-RARA rearrangement.\n\n[RCPath 2018 Paper 1, Question 1 - 25 Marks Total]",
            labData = "WBC: 135x10^9/L (Hyperleukocytic)\nHb: 96 g/L\nPlatelets: 20x10^9/L\nGenetics: PML-RARA positive",
            options = emptyList(),
            correctIndex = -1,
            rationale = "High-yield FRCPath Part 1 essay covering oncologic emergency management of high-risk APL, differentiation syndrome diagnostic criteria and treatment, and molecular relapse salvage.",
            sourceResourceId = "who_haem_5th",
            guidelineSection = "BSH & ELN Guidelines: Acute Promyelocytic Leukaemia",
            evidenceGrade = "Grade 1A",
            yearPaper = "RCPath Paper 1 September 2018 (Q1)",
            essayModelAnswer = """
PART 1: Diagnosis, Immediate Tests, Management & Complications (12 marks)
• Diagnosis: High-Risk Acute Promyelocytic Leukaemia (APL) with PML-RARA rearrangement (high risk due to WBC >10x10^9/L).
• Immediate Additional Tests:
  - Coagulation screen (PT, APTT, Fibrinogen Clauss, Thrombin time, D-dimer) to evaluate for Disseminated Intravascular Coagulation (DIC).
  - Bone marrow aspirate and trephine biopsy (morphology, cytogenetics for t(15;17), RT-PCR for PML-RARA transcript type [bcr1, bcr2, bcr3]).
  - U&Es, electrolytes, calcium, magnesium, phosphate, uric acid, LDH (baseline tumour lysis syndrome [TLS]).
  - Baseline 12-lead ECG (measure QTc interval, especially prior to arsenic trioxide).
  - Baseline echocardiogram.
  - Blood group, antibody screen, and viral serology (HIV, Hepatitis B/C).
• Initial Management:
  - Start All-Trans Retinoic Acid (ATRA, 45 mg/m²/day in divided doses) IMMEDIATELY upon suspicion, without waiting for molecular confirmation.
  - Because WBC >10x10^9/L (High Risk), add chemotherapy/Arsenic Trioxide (ATO) prompt induction: Anthracycline (Idarubicin or Daunorubicin) + ATRA OR ATO + ATRA with cytoreductive chemotherapy (Idarubicin or Gemtuzumab Ozogamicin).
  - Aggressive supportive management for coagulopathy:
    * Transfuse platelets to maintain count >50x10^9/L (even without active bleeding).
    * Cryoprecipitate or fibrinogen concentrate to maintain fibrinogen >1.5 g/L.
    * Fresh frozen plasma to correct prolonged PT/APTT.
    * Monitor coags 2-3 times daily until coagulopathy resolves.
  - Allopurinol or rasburicase with IV hydration for TLS.
• Complications of Treatment:
  - Differentiation syndrome (fever, weight gain, dyspnoea, pleural effusions).
  - QTc prolongation and ventricular arrhythmias (with Arsenic Trioxide).
  - Severe bleeding / catastrophic fatal intracranial haemorrhage.
  - Hyperleukocytosis.
  - Pseudotumor cerebri (headache, papilloedema from ATRA).
  - Hepatotoxicity and hypermagnesaemia/hypokalaemia.

PART 2: Day 5 Breathlessness, Weight Gain 5 kg, Bilateral Infiltrates (8 marks)
• Differential Diagnosis:
  1. APL Differentiation Syndrome (formerly ATRA syndrome).
  2. Fluid overload / pulmonary oedema secondary to blood products/hydration.
  3. Severe pulmonary infection / neutropenic sepsis (bacterial, viral, fungal).
  4. Diffuse alveolar haemorrhage.
  5. Leukostasis (given presentation WBC 135x10^9/L).
• Management Plan:
  - For Differentiation Syndrome:
    * Initiate Dexamethasone 10 mg IV twice daily IMMEDIATELY at the earliest sign. Continue until complete resolution of symptoms.
    * Temporarily interrupt ATRA and/or ATO if severe organ dysfunction or hypoxemia occurs.
    * Oxygen therapy and close respiratory monitoring.
    * Furosemide diuresis to manage fluid retention.
  - For infection/sepsis: Blood cultures, sputum, urgent broad-spectrum IV antibiotics (e.g., piperacillin-tazobactam), viral swabs.
  - Restrict fluid intake, monitor fluid balance and daily weights.

PART 3: Molecular Relapse at 18 Months (5 marks)
• Confirmation & Staging:
  - Repeat bone marrow aspirate: morphologic assessment and quantitative RT-PCR for PML-RARA.
  - Confirm molecular relapse on two consecutive bone marrow samples taken 2-4 weeks apart.
  - Screen for central nervous system (CNS) relapse (lumbar puncture).
• Salvage Therapy:
  - If initial therapy was ATRA + chemotherapy (without ATO): Induction with Arsenic Trioxide (ATO) + ATRA achieves >90% secondary molecular remission.
  - If initial therapy included ATO: Second-line chemotherapy + ATRA, Gemtuzumab Ozogamicin, or ATO with cytarabine.
  - Post-Remission Consolidation:
    * In patients achieving second complete molecular remission (PCR negative): Autologous Stem Cell Transplantation (ASCT) is the preferred standard.
    * In patients with persistent PCR positivity or early second relapse: Allogeneic Stem Cell Transplantation (allo-SCT).
            """.trimIndent(),
            markingRubric = "Part 1: 12 marks (2 marks diagnosis & risk stratification, 4 marks DIC & baseline tests, 4 marks ATRA/ATO management, 2 marks complications)\nPart 2: 8 marks (3 marks differentials: differentiation syndrome, fluid overload, infection; 5 marks management: IV dexamethasone, holding ATRA, diuresis)\nPart 3: 5 marks (2 marks confirmation & CNS check, 2 marks salvage ATO/ATRA, 1 mark ASCT/Allo-SCT role)"
        ),

        // Question 14: BSH 2026 AL Amyloidosis - Renal & Neuropathy Case MCQ
        Question(
            id = "q_amyloid_03",
            domain = "Haemostasis & Thrombosis",
            questionType = "MCQ",
            title = "Autonomic Neuropathy & Coagulopathy in Amyloidosis",
            stem = "A 65-year-old male presents with lightheadedness on standing, chronic nocturnal diarrhoea, and extensive spontaneous periorbital ecchymoses ('raccoon eyes'). Lying BP is 138/84 mmHg, which falls to 102/64 mmHg at 3 minutes of standing with a heart rate change from 68 to 72 bpm. Coagulation screen shows an isolated prolongation of PT (19s) with normal fibrinogen and platelets.\n\nAccording to the 2026 BSH AL Amyloidosis Guideline, what is the underlying mechanism of the ecchymoses and postural hypotension?",
            labData = "Lying BP: 138/84 mmHg\nStanding BP (3 min): 102/64 mmHg (HR 72 bpm)\nPT: 19s (Control 12s)\nAPTT: 32s\nPlatelets: 215x10^9/L",
            options = listOf(
                "Primary idiopathic thrombocytopenic purpura with vasovagal syncope",
                "Factor X deficiency due to binding by amyloid fibrils and neurogenic orthostatic hypotension from autonomic neuropathy",
                "Scurvy with adrenal insufficiency",
                "Warfarin toxicity with diabetic autonomic neuropathy",
                "Severe Von Willebrand disease with sympathetic hyperreactivity"
            ),
            correctIndex = 1,
            rationale = "According to the 2026 BSH Guideline on AL Amyloidosis (Garg et al.):\n• Periorbital ecchymoses (purpura) and bleeding in AL amyloidosis are classic hallmark signs caused by vascular amyloid fragility and acquired Factor X deficiency (caused by rapid clearance and adsorption of circulating Factor X onto amyloid fibrils in tissues).\n• Neurogenic orthostatic hypotension is defined as a sustained fall in blood pressure >=20/10 mmHg within 3 minutes of standing with blunted compensatory heart rate elevation (<10 bpm), characteristic of autonomic amyloid infiltration.\n• Both bleeding due to Factor X deficiency and postural drop >20 mmHg are explicit criteria for urgent haematology clinic referral.",
            sourceResourceId = "bsh_amyloidosis_2026",
            guidelineSection = "Section: When to Refer Urgently to a Haematology Clinic / Figure 1",
            evidenceGrade = "Grade 1A",
            yearPaper = "BSH 2026 Core Curriculum"
        ),

        // Question 15: Heparin-Induced Thrombocytopenia (HIT) MCQ
        Question(
            id = "q_hit_01",
            domain = "Haemostasis & Thrombosis",
            questionType = "MCQ",
            title = "Management of Suspected Heparin-Induced Thrombocytopenia",
            stem = "A 71-year-old woman develops a fall in platelet count from 240x10^9/L to 65x10^9/L on post-operative day 7 following hip replacement surgery while receiving prophylactic Low-Molecular-Weight Heparin (enoxaparin). Ultrasound confirms a new extensive femoral deep vein thrombosis. Her calculated 4Ts score is 7 (High Probability).\n\nAccording to UKHCDO and BSH Guidelines, what is the most appropriate immediate management?",
            labData = "Pre-op Platelets: 240x10^9/L\nDay 7 Platelets: 65x10^9/L (drop >50%)\nCoagulation Screen: Normal\nDoppler: Acute right femoral DVT",
            options = listOf(
                "Switch enoxaparin to therapeutic unfractionated heparin infusion and recheck platelets in 24 hours",
                "Stop enoxaparin, immediately start therapeutic non-heparin anticoagulation (e.g., Argatroban or Fondaparinux), and request anti-PF4/heparin antibodies",
                "Administer 2 adult doses of irradiated platelets and start Warfarin 10 mg stat",
                "Continue enoxaparin and insert an inferior vena cava (IVC) filter",
                "Discontinue enoxaparin and observe without any anticoagulation until antibody results return"
            ),
            correctIndex = 1,
            rationale = "According to UKHCDO Guidelines on Heparin-Induced Thrombocytopenia:\n• In patients with intermediate or high 4Ts scores (score >=4), ALL heparin products must be discontinued immediately.\n• HIT is a paradoxical prothrombotic emergency; therapeutic alternative non-heparin anticoagulation (Argatroban, Fondaparinux, or Danaparoid) must be initiated immediately without waiting for laboratory confirmation.\n• Platelet transfusions are contraindicated because they can fuel further thrombosis.\n• Warfarin is strictly contraindicated in the acute phase of HIT due to the risk of microvascular thrombosis and coumarin-induced skin necrosis/limb gangrene resulting from rapid protein C depletion.",
            sourceResourceId = "ukhcdo_haemostasis_2022",
            guidelineSection = "Section: Immediate Management of Suspected HIT",
            evidenceGrade = "Grade 1A",
            yearPaper = "RCPath Past Papers & UKHCDO Guidelines"
        ),

        // Question 16: Transfusion - Positive DAT at 2 AM
        Question(
            id = "q_transfusion_dat_01",
            domain = "Transfusion Medicine",
            questionType = "MCQ",
            title = "Emergency Positive DAT Investigation and Red Cell Support",
            stem = "The hospital transfusion laboratory is contacted at 2:00 AM regarding a 44-year-old male scheduled for emergency cardiac transplantation. Routine pre-transfusion testing reveals a positive Direct Antiglobulin Test (DAT 3+ with IgG) and the indirect antibody screen panel is pan-reactive across all 11 reagent red cells.\n\nWhat is the most critical immediate laboratory procedure to enable safe provision of red cells for emergency surgery?",
            labData = "DAT: 3+ (anti-IgG positive, C3d negative)\nAntibody Screen: Pan-reactive (all cells positive at 37°C IAT, auto-control positive)\nClinical Status: In theatre waiting for cardiac graft arrival",
            options = listOf(
                "Cancel the transplant and issue no red cells under any circumstance",
                "Perform an eluate and red cell autoadsorption/alloadsorption to rule out underlying clinically significant red cell alloantibodies masked by the autoantibody",
                "Issue random uncrossmatched O RhD positive red cells without testing",
                "Administer 2 g/kg intravenous immunoglobulin and proceed without crossmatching",
                "Irradiate the donor red blood cells to inactivate the pan-reactive antibody"
            ),
            correctIndex = 1,
            rationale = "According to BSH Guidelines for Pre-transfusion Compatibility Procedures:\n• When a warm autoantibody causes pan-reactivity on the antibody panel, the primary risk is that underlying clinically significant red cell alloantibodies (e.g., anti-Rh, Kell, Duffy, Kidd) may be masked by the autoantibody.\n• Adsorption techniques (patient red cell autoadsorption if not recently transfused, or differential alloadsorption using matched phenotyped cells) must be performed to adsorb the autoantibody from the serum, leaving any alloantibodies in the adsorbed serum for identification.\n• In extreme life-or-death emergencies where blood cannot wait for completed adsorption, 'least incompatible' units or extended phenotypically matched units (ABO, Rh, and Kell matched) are issued with senior haematologist authorization.",
            sourceResourceId = "bsh_transfusion_components",
            guidelineSection = "Section: Warm Autoimmune Haemolytic Anaemia and Pan-reactive Serology",
            evidenceGrade = "Grade 1A",
            yearPaper = "RCPath Paper 1 2019 Q4"
        )
    )

    fun getFlashcards(): List<Flashcard> = listOf(
        Flashcard(
            id = "fc_01",
            domain = "Haemato-Oncology",
            topic = "AL Amyloidosis Staging",
            sourceResourceId = "bsh_amyloidosis_2026",
            front = "What defines AL-ISS Stage IIIC cardiac amyloidosis and what is its median survival?",
            back = "• NT-proBNP >8500 ng/L\n• High-sensitivity Troponin T >=0.035 ng/L\n• Global Longitudinal Strain (GLS) >-9%\n\nMedian Survival: Approximately 4 months (Ultra-poor risk group).",
            guidelineRef = "BSH 2026 AL Amyloidosis Guideline (Table 2; Khwaja et al.)"
        ),
        Flashcard(
            id = "fc_02",
            domain = "Haemato-Oncology",
            topic = "MGUS Amyloid Screening",
            sourceResourceId = "bsh_amyloidosis_2026",
            front = "What three screening biomarkers should be added to routine monitoring of MGUS/SMM to detect early AL amyloidosis?",
            back = "1. Urine Protein:Creatinine Ratio (PCR) - suspect if >50 mg/mmol\n2. Urine Albumin:Creatinine Ratio (ACR) - suspect if concordant >30 mg/mmol\n3. NT-proBNP - suspect if >332 ng/L\n\nGrade 1A recommendation. Note: Patients can progress with minimal or no increase in clonal light chains.",
            guidelineRef = "BSH 2026 AL Amyloidosis Guideline (Rec 1A)"
        ),
        Flashcard(
            id = "fc_03",
            domain = "Haemato-Oncology",
            topic = "AL Amyloid Biopsy Sequence",
            sourceResourceId = "bsh_amyloidosis_2026",
            front = "In suspected AL amyloidosis with proteinuria, when should a kidney biopsy be avoided?",
            back = "A kidney biopsy can be avoided if AL amyloid deposits are confirmed with Congo red staining on less invasive tissue (bone marrow trephine or abdominal fat pad aspirate/biopsy) in the presence of a clonal paraprotein (Grade 2B).",
            guidelineRef = "BSH 2026 AL Amyloidosis Guideline (Rec 2B)"
        ),
        Flashcard(
            id = "fc_04",
            domain = "Haemato-Oncology",
            topic = "AL Amyloid Urgent Referral",
            sourceResourceId = "bsh_amyloidosis_2026",
            front = "List the 6 red-flag triggers for URGENT haematology referral in suspected AL amyloidosis.",
            back = "1. Serum Albumin <25 g/L\n2. Falling eGFR <50 mL/min/1.73m²\n3. All cases of cardiac amyloidosis\n4. Autonomic neuropathy with postural drop >20 mmHg\n5. Hepatic amyloid with Bilirubin >2x ULN\n6. Bleeding/bruising due to acquired Factor X deficiency",
            guidelineRef = "BSH 2026 AL Amyloidosis Guideline"
        ),
        Flashcard(
            id = "fc_05",
            domain = "Haemato-Oncology",
            topic = "Multiple Myeloma SLiM Criteria",
            sourceResourceId = "imwg_myeloma_2021",
            front = "What are the three SLiM myeloma-defining biomarkers (IMWG criteria)?",
            back = "• S: Sixty percent (>=60%) clonal bone marrow plasma cells\n• Li: Light chain ratio (involved/uninvolved sFLC ratio >=100, with involved >=100 mg/L)\n• M: MRI >1 focal bone lesion (each >=5 mm in diameter)",
            guidelineRef = "IMWG Consensus Criteria (Rajkumar et al. 2014/2021)"
        ),
        Flashcard(
            id = "fc_06",
            domain = "Haemato-Oncology",
            topic = "Myeloma First-Line Imaging",
            sourceResourceId = "imwg_myeloma_2021",
            front = "What is the recommended 1st-line imaging for newly diagnosed Multiple Myeloma?",
            back = "Whole-Body Low-Dose CT (WBLDCT). Plain skeletal surveys are obsolete due to low sensitivity (misses 30% of lytic lesions). Whole-spine MRI is required if cord compression is suspected.",
            guidelineRef = "BSH & IMWG Diagnostic Guidelines"
        ),
        Flashcard(
            id = "fc_07",
            domain = "Haemato-Oncology",
            topic = "B-ALL Immunophenotyping",
            sourceResourceId = "who_haem_5th",
            front = "How do you distinguish Common ALL from Pre-B ALL and Mature B-ALL on flow cytometry?",
            back = "• Common ALL: CD10+ (CALLA), CD19+, TdT+, Cytoplasmic Ig negative, SmIg negative\n• Pre-B ALL: CD10+, CD19+, TdT+, Cytoplasmic Ig (mu heavy chain) POSITIVE, SmIg negative\n• Mature B-ALL: Surface Ig (SmIg) POSITIVE, TdT negative",
            guidelineRef = "WHO 5th Edition Haematolymphoid Tumours"
        ),
        Flashcard(
            id = "fc_08",
            domain = "Haemato-Oncology",
            topic = "APL Differentiation Syndrome",
            sourceResourceId = "who_haem_5th",
            front = "What is the clinical hallmark and immediate treatment of APL Differentiation Syndrome?",
            back = "• Hallmarks: Unexplained fever, weight gain (>5 kg), dyspnoea, bilateral pulmonary infiltrates, pleural/pericardial effusions.\n• Treatment: Dexamethasone 10 mg IV twice daily IMMEDIATELY at the earliest sign. Temporarily hold ATRA/ATO if severe.",
            guidelineRef = "BSH & ELN Guidelines for Acute Promyelocytic Leukaemia"
        ),
        Flashcard(
            id = "fc_09",
            domain = "Transfusion Medicine",
            topic = "Blood Product Irradiation",
            sourceResourceId = "bsh_transfusion_components",
            front = "Which chemotherapy drugs mandate LIFELONG irradiated cellular blood components?",
            back = "Purine analogues (Fludarabine, Cladribine, Pentostatin), Bendamustine, and Clofarabine.\n\nIrradiation (25 Gy) destroys donor T-lymphocytes to prevent fatal Transfusion-Associated Graft-versus-Host Disease (TA-GVHD).",
            guidelineRef = "BSH Guidelines on Prevention of TA-GVHD"
        ),
        Flashcard(
            id = "fc_10",
            domain = "Transfusion Medicine",
            topic = "Emergency Warfarin Reversal",
            sourceResourceId = "bsh_transfusion_components",
            front = "What is the first-line treatment for life-threatening intracranial bleeding on Warfarin?",
            back = "Prothrombin Complex Concentrate (PCC 25-50 IU/kg) plus IV Vitamin K (5-10 mg stat).\n\nNormalises INR within 10-15 minutes with minimal volume. FFP is NOT recommended unless PCC is unavailable.",
            guidelineRef = "BSH Guidelines on Antithrombotic Reversal"
        ),
        Flashcard(
            id = "fc_11",
            domain = "Transfusion Medicine",
            topic = "Sickle Cell Transfusion Targets",
            sourceResourceId = "bsh_transfusion_components",
            front = "What are the target pre-operative Hb S% and maximum total Hb in Sickle Cell Disease?",
            back = "• Target Hb S: <30% prior to major/orthopaedic surgery\n• Target total Hb: 100 - 110 g/L (Never exceed 110 g/L due to dangerous hyperviscosity crisis risk)",
            guidelineRef = "BSH Sickle Cell Disease Transfusion Guidelines"
        ),
        Flashcard(
            id = "fc_12",
            domain = "Haemostasis & Thrombosis",
            topic = "HIT 4Ts Score & Anticoagulation",
            sourceResourceId = "ukhcdo_haemostasis_2022",
            front = "In suspected HIT (4Ts >=4), what 3 key drug rules must be followed immediately?",
            back = "1. Stop ALL heparin immediately (including catheter line flushes).\n2. Start a therapeutic non-heparin anticoagulant immediately (Argatroban, Fondaparinux, Danaparoid).\n3. Do NOT start Warfarin until platelet count has recovered >150x10^9/L (risk of venous limb gangrene from protein C depletion).",
            guidelineRef = "UKHCDO Guidelines on Heparin-Induced Thrombocytopenia"
        ),
        Flashcard(
            id = "fc_13",
            domain = "Haemostasis & Thrombosis",
            topic = "Factor VIII Inhibitor vs Lupus Anticoagulant",
            sourceResourceId = "ukhcdo_haemostasis_2022",
            front = "How do you distinguish an acquired Factor VIII inhibitor from a Lupus Anticoagulant on mixing studies?",
            back = "• Both cause isolated prolonged APTT that does not correct immediately.\n• FVIII inhibitor is time- and temperature-dependent: progressively prolongs after 2-hour incubation at 37°C.\n• Lupus Anticoagulant is immediate-acting and corrects with excess phospholipid in confirmatory testing (DRVVT / PNP).",
            guidelineRef = "UKHCDO Guidelines on Acquired Bleeding Disorders"
        ),
        Flashcard(
            id = "fc_14",
            domain = "General Haematology",
            topic = "Genetic Haemochromatosis Endpoints",
            sourceResourceId = "bsh_iron_haemochromatosis",
            front = "What is the primary target parameter for assessing optimal induction venesection in HFE haemochromatosis?",
            back = "Serum Ferritin <50 mcg/L (ng/mL).\n\nTransferrin saturation fluctuates widely with diet and normalises late; ferritin is the gold standard monitoring parameter.",
            guidelineRef = "BSH Guideline on Genetic Haemochromatosis"
        ),
        Flashcard(
            id = "fc_15",
            domain = "General Haematology",
            topic = "Paroxysmal Nocturnal Haemoglobinuria (PNH)",
            sourceResourceId = "who_haem_5th",
            front = "What is the definitive diagnostic test for Paroxysmal Nocturnal Haemoglobinuria (PNH)?",
            back = "High-sensitivity multiparameter flow cytometry demonstrating deficiency of GPI-anchored proteins (CD55, CD59) and FLAER (fluorescent aerolysin) on granulocytes and monocytes.",
            guidelineRef = "BSH & International PNH Guidelines"
        )
    )
}

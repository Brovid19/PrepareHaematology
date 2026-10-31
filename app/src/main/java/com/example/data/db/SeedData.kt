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
            summary = "FRCPath Exam Focus: Early AL amyloidosis screening in monoclonal gammopathies, differentiating AL from ATTR, cardiac biomarker thresholds (NT-proBNP 332 ng/L), non-invasive tissue biopsy sequence to avoid renal biopsy, and the 2026 AL-ISS staging system. Early diagnosis yields ~80% 5-year survival vs <30% in advanced disease.",
            keyCutoffs = "• Renal: 24h proteinuria >0.5g/24h OR urine PCR >50 mg/mmol with concordant urine ACR >30 mg/mmol\n• Cardiac: NT-proBNP >332 ng/L (or unexplained progressive rise in paraproteinaemia)\n• Hepatic: Alkaline Phosphatase (ALP) >1.5x institutional ULN (raised in 86% of liver AL)\n• Neuropathy: Sustained BP fall >=20/10 mmHg by 3 min with blunted HR rise\n• Urgent Referral Triggers: Serum Albumin <25 g/L, eGFR <50 mL/min, all cardiac amyloidosis, postural drop >20 mmHg, Bilirubin >2x ULN, Factor X deficiency bleeding",
            stagingCriteria = "AL-ISS 2026 Cardiac Staging (Khwaja et al. 2026; Garg 2026 Table 2). Note: the guideline table prints troponin T in 'ng/L'; the conventional threshold is 0.035 µg/L (= 35 ng/L):\n• Stage I: NT-proBNP <332 ng/L & TnT <0.035 ng/L (Median survival: Not reached at 5 yr)\n• Stage II: NT-proBNP >332 but <8500 ng/L OR TnT >=0.035 ng/L (Not reached at 5 yr)\n• Stage IIIA: NT-proBNP <8500 ng/L & TnT >=0.035 ng/L (76 months)\n• Stage IIIB: NT-proBNP >8500 ng/L & TnT >=0.035 ng/L (26 months)\n• Stage IIIC: NT-proBNP >8500 ng/L, TnT >=0.035 ng/L & Global Longitudinal Strain (GLS) >-9% (4 months)\n\nRenal Staging (Palladini et al.):\n• Stage 1: Proteinuria <=5 g/24h & eGFR >=50 (0% dialysis at 2 yr)\n• Stage 2: Either proteinuria >5 g/24h OR eGFR <50 (11-25% dialysis at 2 yr)\n• Stage 3: Both proteinuria >5 g/24h & eGFR <50 (60-75% dialysis at 2 yr)",
            recommendations = "• Rec 1 (Grade 1A): Urine PCR, ACR, and NT-proBNP should be added to standard tests for assessment as well as monitoring of MGUS.\n• Rec 2 (Grade 1A): Falling serum albumin levels and raised alkaline phosphatase should be noted and, if persistent, investigated further.\n• Rec 3 (Grade 1A): Patients can progress to AL amyloidosis with minimal or no increase in clonal light chains.\n• Rec 4 (Grade 1B): Amyloid should be considered in unexplained cardiac failure, and screening for AL undertaken in all unexplained cardiac failure, especially HFpEF (red flags: low-voltage ECG with 'LVH', bilateral carpal tunnel, hypotension after ACE-I/ARB).\n• Rec 5 (Grade 2B): Bone marrow or abdominal fat pad biopsy with Congo red should precede renal biopsy; renal biopsy avoided if AL confirmed elsewhere.",
            url = "https://doi.org/10.1111/bjh.70472"
        ),
        ApprovedResource(
            id = "imwg_myeloma_2021",
            title = "International Myeloma Working Group (IMWG) Criteria for the Diagnosis and Staging of Multiple Myeloma",
            issuingBody = "International Myeloma Working Group",
            year = 2014,
            citation = "Rajkumar SV, Dimopoulos MA, Palumbo A, et al. International Myeloma Working Group updated criteria for the diagnosis of multiple myeloma. Lancet Oncol. 2014;15(12):e538-e548. Management points draw on BSH/UK myeloma practice.",
            category = "Malignant Haematology",
            summary = "FRCPath Exam Focus: Distinction between MGUS, Smouldering Multiple Myeloma (SMM), and symptomatic Multiple Myeloma requiring treatment. Core SBA themes include SLiM-CRAB criteria, Whole-Body Low-Dose CT (WBLDCT) superiority over skeletal survey, emergency management of spinal cord compression, and renal impairment induction without bortezomib dose adjustment.",
            keyCutoffs = "• SLiM Biomarkers (>=80% risk of progression in 2 yr):\n  - S: Sixty percent clonal bone marrow plasma cells (>=60%)\n  - Li: Light chain ratio (involved/uninvolved sFLC ratio >=100 with involved >=100 mg/L)\n  - M: MRI >1 focal bone lesion (>=5 mm in size)\n• CRAB Criteria:\n  - C: Calcium >2.75 mmol/L (or >0.25 mmol/L above ULN)\n  - R: Renal insufficiency (CrCl <40 mL/min or creatinine >177 umol/L)\n  - A: Anaemia (Hb <100 g/L or >20 g/L below normal)\n  - B: Bone lesions (>=1 osteolytic lesions on WBLDCT/PET-CT/skeletal survey)",
            stagingCriteria = "R-ISS Staging System:\n• Stage I: ISS I (beta-2 microglobulin <3.5 mg/L & albumin >=35 g/L), normal LDH, standard risk FISH\n• Stage III: ISS III (beta-2 microglobulin >=5.5 mg/L) with high-risk FISH [del(17p), t(4;14), t(14;16)] OR high LDH\n• Stage II: Neither Stage I nor Stage III",
            recommendations = "• Rec 1: Whole-body low-dose CT (WBLDCT) is the preferred initial imaging modality over plain skeletal surveys; plain skeletal survey misses ~30% of lytic lesions.\n• Rec 2: In suspected myeloma with acute renal failure, immediately initiate vigorous hydration, avoid nephrotoxic drugs/radiocontrast, and commence Bortezomib + Dexamethasone (Bortezomib requires no renal dose modification).\n• Rec 3: Suspected spinal cord compression is a haematological emergency requiring whole-spine MRI within 24 hours and emergency dexamethasone 16 mg daily.\n• Rec 4: SLiM criteria (>=60% bone marrow plasma cells, involved/uninvolved sFLC ratio >=100, >1 focal MRI lesion >=5mm) define myeloma requiring immediate anti-myeloma therapy regardless of CRAB symptoms.\n• Rec 5: Bisphosphonates (Zoledronic acid or Pamidronate) should be administered to all patients requiring frontline therapy to prevent skeletal-related events.",
            url = "https://doi.org/10.1016/S1470-2045(14)70442-5",
            // Not yet checked against a primary source in this repo; needs consultant sign-off.
            isVerified = false
        ),
        ApprovedResource(
            id = "who_haem_5th",
            title = "World Health Organization (WHO) Classification of Haematolymphoid Tumours (5th Edition)",
            issuingBody = "World Health Organization",
            year = 2022,
            citation = "Alaggio R, Amador C, Anagnostopoulos I, et al. Leukemia. 2022;36:1720–1748.",
            category = "Malignant Haematology",
            summary = "FRCPath Exam Focus: Diagnostic criteria for acute leukaemias, myelodysplastic neoplasms, and mature lymphoid neoplasms. Essential SBA concepts include cytogenetic exceptions to the 20% blast threshold in AML (PML-RARA, RUNX1-RUNX1T1, CBFB-MYH11), sequential flow cytometry immunophenotyping of B-ALL differentiation, and distinguishing Pre-B ALL from Mature B-ALL.",
            keyCutoffs = "• AML: >=20% blasts in bone marrow or blood, EXCEPT AML with defining genetic abnormalities, which WHO-HAEM5 diagnoses at any blast count (e.g. PML::RARA, RUNX1::RUNX1T1, CBFB::MYH11, KMT2A rearrangement, DEK::NUP214, MECOM rearrangement, NUP98 rearrangement, NPM1). BCR::ABL1 and CEBPA-mutated AML still require >=20%. (The ICC 2022 uses >=10% for most of these.)\n• B-ALL Differentiation Stages (EGIL-style subgroups):\n  - Pro-B: CD19+, HLA-DR+, TdT+, CD10-, Cyt Ig-, SmIg-\n  - Common ALL: CD19+, CD10+ (CALLA), TdT+, Cyt Ig-, SmIg-\n  - Pre-B ALL: CD19+, CD10+, TdT+, Cytoplasmic mu heavy chain (Cyt Ig)+, SmIg-\n  - SmIg+, TdT- with CD10+/BCL6+ and MYC::IGH t(8;14): Burkitt lymphoma/leukaemia, a MATURE B-cell neoplasm in WHO-HAEM5 (the old FAB 'L3 / mature B-ALL' label is obsolete)",
            stagingCriteria = "Subclassification of acute leukaemias by defining recurrent genetic abnormalities vs. differentiation.",
            recommendations = "The WHO classification defines diagnostic criteria; it does not issue graded recommendations.\n• Criterion 1: Integrated diagnosis requires morphology, multiparameter flow cytometry, cytogenetics and molecular genetics (FISH/RT-PCR/NGS).\n• Criterion 2: AML with a defining genetic abnormality [e.g. t(15;17) PML::RARA, t(8;21) RUNX1::RUNX1T1, inv(16)/t(16;16) CBFB::MYH11] is diagnosed regardless of blast percentage (BCR::ABL1 and CEBPA excepted).\n• Criterion 3: In B-ALL, Pro-B (CD10-), Common (CD10+), and Pre-B (cytoplasmic mu+) subgroups are immunophenotypic stages; genetic subtype (e.g. BCR::ABL1, KMT2A, hyperdiploidy) drives risk.\n• Clinical note (ELN, not WHO): suspected APL warrants immediate ATRA before genetic confirmation.\n• PNH testing (ICCS guidance, not WHO): high-sensitivity flow using FLAER with CD24 on neutrophils and FLAER with CD14 on monocytes; CD59 on red cells for clone typing. CD55 alone is not recommended.",
            url = "https://doi.org/10.1038/s41375-022-01620-2",
            // Not yet checked against a primary source in this repo; needs consultant sign-off.
            isVerified = false
        ),
        ApprovedResource(
            id = "bsh_transfusion_components",
            title = "British Society for Haematology: Guidelines on the use of blood components & prevention of Transfusion-Associated Graft-versus-Host Disease",
            issuingBody = "British Society for Haematology",
            year = 2020,
            citation = "BSH guidelines: Estcourt LJ et al. Guidelines for the use of platelet transfusions. Br J Haematol. 2017;176:365-394; Foukaneli T et al. Guidelines on the use of irradiated blood components. Br J Haematol. 2020;191:704-724; plus BSH plasma/cryoprecipitate and warfarin-reversal guidance.",
            category = "Transfusion Medicine",
            summary = "FRCPath Exam Focus: Evidence-based selection of blood components, specific indications for cellular irradiation to prevent Transfusion-Associated Graft-versus-Host Disease (TA-GVHD), platelet transfusion thresholds across clinical scenarios, and emergency management of acute coagulopathies and life-threatening warfarin bleeding.",
            keyCutoffs = "• Platelet transfusion thresholds (BSH 2017): prophylaxis <10x10^9/L in reversible marrow failure (consider 10-20 with additional risk factors e.g. sepsis); CVC insertion 20; lumbar puncture 40; major surgery / percutaneous liver biopsy 50; epidural 80; neurosurgery or posterior eye surgery 100\n• Fibrinogen threshold: <1.5 g/L in major bleeding or obstetric haemorrhage (target >2.0 g/L in PPH)\n• Warfarin emergency reversal: Prothrombin Complex Concentrate (PCC 25-50 IU/kg) + IV Vitamin K 5-10 mg; avoid FFP unless PCC unavailable",
            stagingCriteria = "Mandatory indications for irradiated cellular components:\n• Treatment with purine analogues (fludarabine, cladribine, pentostatin) - lifelong\n• Treatment with bendamustine, clofarabine\n• Autologous and allogeneic stem cell transplant recipients\n• Hodgkin lymphoma (all stages, lifelong)\n• Intrauterine and exchange transfusions",
            recommendations = "• Rec 1: Patients treated with purine analogues (Fludarabine, Cladribine, Pentostatin), Bendamustine, or Clofarabine MUST receive irradiated cellular components (red cells and platelets) LIFELONG to prevent fatal TA-GVHD.\n• Rec 2: In major life-threatening bleeding or intracranial haemorrhage on Warfarin, administer 4-factor Prothrombin Complex Concentrate (PCC 25–50 IU/kg) plus IV Vitamin K 5–10 mg immediately; FFP is NOT recommended unless PCC is unavailable.\n• Rec 3: Prophylactic platelet transfusion threshold is <10 x 10^9/L in reversible bone marrow failure (consider 10-20 x 10^9/L with additional bleeding risk such as sepsis); procedure thresholds: CVC 20, lumbar puncture 40, major surgery 50, epidural 80, neurosurgery/posterior eye 100 x 10^9/L.\n• Rec 4: Platelet transfusion is relatively contraindicated in TTP and HIT (and generally avoided in post-transfusion purpura) unless there is life-threatening haemorrhage.\n• Rec 5: Fibrinogen threshold in major obstetric or trauma haemorrhage is <1.5–2.0 g/L, requiring replacement with Cryoprecipitate (two 5-unit pools) or Fibrinogen Concentrate.",
            url = "https://b-s-h.org.uk/guidelines/guidelines/the-administration-of-blood-components",
            // Not yet checked against a primary source in this repo; needs consultant sign-off.
            isVerified = false
        ),
        ApprovedResource(
            id = "ukhcdo_haemostasis_2022",
            title = "BSH Guideline on Heparin-Induced Thrombocytopenia (HIT) and UKHCDO Guideline on Acquired Haemophilia A",
            issuingBody = "British Society for Haematology (HIT) / UKHCDO (acquired haemophilia)",
            year = 2013,
            citation = "Watson H, Davidson S, Keeling D. Guidelines on the diagnosis and management of heparin-induced thrombocytopenia: second edition. Br J Haematol. 2012;159:528-540. Collins P et al. Diagnosis and management of acquired coagulation inhibitors: a guideline from UKHCDO. Br J Haematol. 2013;162:758-773.",
            category = "Haemostasis & Thrombosis",
            summary = "FRCPath Exam Focus: Rapid assessment and management of suspected Heparin-Induced Thrombocytopenia (HIT), calculating and interpreting the 4Ts score, ordering appropriate screening and functional assays, and distinguishing acquired Factor VIII inhibitors from Lupus Anticoagulants in unexplained prolonged APTT.",
            keyCutoffs = "• 4Ts Score: High probability (6-8), Intermediate (4-5), Low (0-3)\n• Platelet count drop >50% and nadir >=20x10^9/L 5-10 days after heparin exposure (or <1 day if heparin in past 30 days)\n• Acquired Haemophilia A vs Lupus Anticoagulant:\n  - Isolated prolonged APTT with normal PT\n  - 50:50 mix: a lupus anticoagulant typically fails to correct immediately; an acquired FVIII inhibitor may correct (or nearly) immediately, then prolongs after 1-2 h incubation at 37°C (time- and temperature-dependent)\n  - DRVVT with platelet neutralization procedure (PNP) corrects with excess phospholipid in Lupus Anticoagulant, NOT in FVIII inhibitor",
            stagingCriteria = "Bethesda inhibitor assay: <5 Bethesda Units (BU) low-titre; >=5 BU high-titre inhibitor.",
            recommendations = "• Rec 1: In suspected HIT with an intermediate or high 4Ts score (>=4), immediately stop ALL forms of heparin (including catheter line flushes) and start therapeutic-dose non-heparin anticoagulation (argatroban or danaparoid; fondaparinux is used off-label).\n• Rec 2: Do NOT administer Warfarin in acute HIT until platelets have recovered to >150 x 10^9/L, due to the severe risk of microvascular thrombosis and venous limb gangrene caused by early Protein C depletion.\n• Rec 3: Do NOT give prophylactic platelet transfusions in HIT as they may fuel arterial and venous thrombotic events.\n• Rec 4: In Acquired Haemophilia A with active bleeding, initiate bypassing therapy immediately with recombinant activated Factor VII (rFVIIa 90 mcg/kg) or Activated Prothrombin Complex Concentrate (FEIBA 50–100 U/kg); avoid standard FVIII in high-titre inhibitors.\n• Rec 5: Diagnostic workup of unexplained isolated prolonged APTT must include 50:50 mixing test with 2-hour incubation at 37°C (to detect time-dependent FVIII inhibitors) and DRVVT with platelet neutralization procedure (to confirm lupus anticoagulant).",
            url = "https://pubmed.ncbi.nlm.nih.gov/?term=Watson+2012+heparin-induced+thrombocytopenia+second+edition+British+Journal+of+Haematology",
            // Not yet checked against a primary source in this repo; needs consultant sign-off.
            isVerified = false
        ),
        ApprovedResource(
            id = "bsh_iron_haemochromatosis",
            title = "BSH Guideline: Diagnosis and management of genetic haemochromatosis and iron overload",
            issuingBody = "British Society for Haematology",
            year = 2018,
            citation = "Fitzsimons EJ, Cullis JO, Thomas DW, et al. Diagnosis and therapy of genetic haemochromatosis (review and 2017 update). Br J Haematol. 2018;181(3):293-303.",
            category = "General Haematology",
            summary = "FRCPath Exam Focus: Investigation of hyperferritinaemia, HFE genotyping (C282Y homozygotes vs C282Y/H63D compound heterozygotes), evaluating hepatic fibrosis risk with non-invasive biomarkers or FibroScan, and monitoring induction vs maintenance venesection endpoints.",
            keyCutoffs = "• Transferrin saturation >50% in men, >45% in women indicates iron overload\n• Induction Venesection target: Serum ferritin <50 mcg/L\n• Maintenance phase target: Serum ferritin 50-100 mcg/L\n• Ferritin >1000 mcg/L, AST/ALT elevation, or platelets <200x10^9/L requires evaluation for hepatic fibrosis/cirrhosis (FibroScan or liver biopsy)",
            stagingCriteria = "Genotype categories: Type 1 HFE (C282Y/C282Y or C282Y/H63D), Type 2 Juvenile (HJV, HAMP), Type 3 TFR2, Type 4 Ferroportin.",
            recommendations = "• Rec 1: Serum ferritin is the primary laboratory biomarker for monitoring induction venesection, with a target endpoint of <50 mcg/L (transferrin saturation fluctuates with diet and normalises late).\n• Rec 2: Measure haemoglobin prior to each venesection session; defer venesection if Hb falls below 110 g/L.\n• Rec 3: In patients with C282Y homozygosity, serum ferritin >1000 mcg/L, elevated transaminases, or platelets <200 x 10^9/L warrants evaluation for liver cirrhosis by FibroScan (transient elastography) or liver biopsy.\n• Rec 4: Maintenance venesection should aim to keep serum ferritin between 50 and 100 mcg/L, typically requiring 3–4 venesections annually.\n• Rec 5: First-degree adult relatives (siblings, parents, adult children) of confirmed C282Y homozygotes should be offered genetic counselling and cascade testing (HFE genotype and iron studies).",
            url = "https://doi.org/10.1111/bjh.15164",
            // Not yet checked against a primary source in this repo; needs consultant sign-off.
            isVerified = false
        ),
        ApprovedResource(
            id = "bsh_sickle_transfusion",
            title = "BSH Guideline: Red cell transfusion in sickle cell disease and management of alloimmunisation",
            issuingBody = "British Society for Haematology",
            year = 2017,
            citation = "Davis BA, Allard S, Qureshi A, et al. Guidelines on red cell transfusion in sickle cell disease. Part I: principles and laboratory aspects; Part II: indications for transfusion. Br J Haematol. 2017;176:179-191 and 192-209.",
            category = "Transfusion Medicine",
            summary = "FRCPath Exam Focus: Pre-operative transfusion strategies in Sickle Cell Disease (SCD), management of Acute Chest Syndrome (ACS) with automated red cell exchange, preventing and identifying delayed haemolytic transfusion reactions (DHTR) and hyperhaemolysis, and mandatory extended phenotyping.",
            keyCutoffs = "• Pre-op (HbSS/HbSbeta0): top-up transfusion to Hb ~100 g/L before medium-risk surgery (TAPS trial: fewer peri-operative complications); exchange transfusion (target HbS <30%) considered for high-risk surgery or when baseline Hb is too high for top-up\n• Avoid post-transfusion Hb above ~100-110 g/L (hyperviscosity)\n• Extended matching: Mandatory Rh (D, C, c, E, e) and Kell (K) matching for all SCD patients",
            stagingCriteria = "Indications for automated red cell exchange vs top-up: Exchange preferred for severe/progressive ACS, acute stroke, and pre-op when baseline Hb is too high for top-up.",
            recommendations = "• Rec 1: Extended Rh (D, C, c, E, e) and Kell (K) antigen-matched, HbS-negative, leucodepleted red cells must be provided for all sickle cell patients to prevent alloimmunization.\n• Rec 2: Before medium-risk surgery in HbSS/HbSbeta0, give pre-operative top-up transfusion to Hb ~100 g/L; consider exchange transfusion (HbS <30%) for high-risk surgery or when baseline Hb is high (e.g. >=90 g/L) so that top-up would exceed the Hb ceiling.\n• Rec 3: The maximum post-transfusion haemoglobin must NOT exceed 100–110 g/L to avoid precipitating life-threatening hyperviscosity and vaso-occlusive crisis.\n• Rec 4: In Acute Chest Syndrome with falling PaO2 (<9 kPa / 70 mmHg), progressive lung infiltrates, or haemodynamic instability, automated red cell exchange apheresis is life-saving and should be instituted urgently.\n• Rec 5: If hyperhaemolysis occurs (severe anaemia with Hb lower than pre-transfusion level, reticulocytopenia, and positive DAT/delayed reaction): stop all transfusions immediately; treat with high-dose intravenous methylprednisolone and IVIG +/- eculizumab.",
            url = "https://doi.org/10.1111/bjh.14346",
            // Not yet checked against a primary source in this repo; needs consultant sign-off.
            isVerified = false
        ),
        ApprovedResource(
            id = "bsh_apl_guidelines",
            title = "European LeukemiaNet (ELN) Recommendations for the Management of Acute Promyelocytic Leukaemia",
            issuingBody = "European LeukemiaNet",
            year = 2019,
            citation = "Sanz MA, Fenaux P, Tallman MS, et al. Management of acute promyelocytic leukemia: updated recommendations from an expert panel of the European LeukemiaNet. Blood. 2019;133(15):1630-1643.",
            category = "Malignant Haematology",
            summary = "FRCPath Exam Focus: Emergency management of suspected Acute Promyelocytic Leukaemia (APL) with t(15;17) PML-RARA, immediate coagulopathy resuscitation, Differentiation Syndrome prophylaxis and treatment, chemotherapy-free frontline regimens (ATRA + Arsenic Trioxide), and molecular MRD monitoring.",
            keyCutoffs = "• High-risk APL: WBC >10x10^9/L (risk of fatal early intracranial haemorrhage and differentiation syndrome)\n• Standard/Low-risk: WBC <=10x10^9/L\n• Platelet transfusion threshold: maintain >30-50x10^9/L\n• Fibrinogen threshold: maintain >1.0-1.5 g/L with cryoprecipitate or fibrinogen concentrate",
            stagingCriteria = "Sanz Risk Score (Low, Intermediate, High based on WBC and Platelet count).",
            recommendations = "• Rec 1: Administer All-Trans Retinoic Acid (ATRA 45 mg/m²/day in divided doses) IMMEDIATELY upon morphological suspicion of APL; do NOT await cytogenetic or molecular confirmation of PML-RARA.\n• Rec 2: Aggressively correct coagulopathy to prevent early fatal intracranial hemorrhage: maintain platelets >30-50 x 10^9/L and fibrinogen >1.0-1.5 g/L with regular cryoprecipitate or fibrinogen concentrate until coagulopathy resolves.\n• Rec 3: In Differentiation Syndrome (unexplained fever, weight gain >5 kg, dyspnoea, pulmonary infiltrates, pericardial effusion): immediately start IV Dexamethasone 10 mg BD at first sign; temporarily pause ATRA/ATO only if severe respiratory compromise occurs.\n• Rec 4: In standard/low-risk APL (WBC <=10 x 10^9/L), frontline chemotherapy-free treatment with ATRA plus Arsenic Trioxide (ATO) is the standard of care, achieving >95% cure rates without anthracyclines.\n• Rec 5: High-risk APL (WBC >10 x 10^9/L) requires cytoreductive therapy (Gemtuzumab ozogamicin or Idarubicin) added to ATRA + ATO, with high vigilance for fatal early coagulopathic death.",
            url = "https://pubmed.ncbi.nlm.nih.gov/?term=Sanz+2019+management+of+acute+promyelocytic+leukemia+European+LeukemiaNet",
            // Not yet checked against a primary source in this repo; needs consultant sign-off.
            isVerified = false
        ),
        ApprovedResource(
            id = "bsh_antiphospholipid",
            title = "BSH Guideline on Investigation and Management of Antiphospholipid Syndrome (APS)",
            issuingBody = "British Society for Haematology",
            year = 2012,
            citation = "Keeling D, Mackie I, Moore GW, Greer IA, Greaves M. Guidelines on the investigation and management of antiphospholipid syndrome. Br J Haematol. 2012;157(1):47-58. Classification: revised Sapporo (Sydney) criteria 2006, superseded for research classification by the 2023 ACR/EULAR APS criteria.",
            category = "Haemostasis & Thrombosis",
            summary = "FRCPath Exam Focus: Diagnostic criteria for Antiphospholipid Syndrome (APS) per Sydney consensus (and the 2023 ACR/EULAR classification criteria), interpreting the persistent triple-positive profile (LA, aCL, anti-beta2GPI), why DOACs should be avoided in arterial/triple-positive APS, obstetric APS regimens, and Catastrophic APS (CAPS) salvage.",
            keyCutoffs = "• Laboratory criteria: Positive on 2 or more occasions at least 12 weeks apart:\n  1. Lupus Anticoagulant (LA) detected per ISTH criteria (screening, mixing, confirmation)\n  2. Anticardiolipin IgG/IgM (>40 GPL/MPL or >99th centile)\n  3. Anti-beta-2-glycoprotein-I IgG/IgM (>99th centile)\n• Obstetric criteria: >=3 consecutive early miscarriages (<10w), >=1 fetal death (>=10w), or premature birth <34w with severe pre-eclampsia/placental insufficiency",
            stagingCriteria = "Triple-positive APS carries the highest thrombotic risk; DOACs are not recommended (avoid) in triple-positive APS and in arterial APS.",
            recommendations = "• Rec 1: In patients with thrombotic APS, warfarin (target INR 2.0–3.0) remains the standard of care; DOACs should be avoided in triple-positive APS (TRAPS trial: excess arterial events with rivaroxaban) and in arterial APS.\n• Rec 2: Laboratory criteria require demonstration of Lupus Anticoagulant, Anticardiolipin IgG/IgM (>40 GPL/MPL or >99th centile), or Anti-beta-2-glycoprotein-I on two or more occasions at least 12 weeks apart.\n• Rec 3: In obstetric APS (recurrent miscarriage or late fetal loss): treatment with Low-Dose Aspirin (75–150 mg daily) plus prophylactic LMWH throughout pregnancy and 6 weeks postpartum significantly improves live birth rates.\n• Rec 4: In Catastrophic APS (CAPS): emergency multimodal treatment must be initiated immediately with therapeutic anticoagulation + high-dose corticosteroids + plasma exchange +/- intravenous immunoglobulin (IVIG) or eculizumab.\n• Rec 5: Asymptomatic individuals with incidental persistent triple-positive antiphospholipid antibodies should receive primary thromboprophylaxis with low-dose aspirin (75 mg daily) and venous thromboprophylaxis during high-risk periods.",
            url = "https://pubmed.ncbi.nlm.nih.gov/?term=Keeling+2012+guidelines+investigation+management+antiphospholipid+syndrome",
            // Not yet checked against a primary source in this repo; needs consultant sign-off.
            isVerified = false
        ),
        ApprovedResource(
            id = "bsh_ttp_guidelines",
            title = "Diagnosis and Management of Thrombotic Thrombocytopenic Purpura (TTP): BSH Guideline and ISTH Guidance",
            issuingBody = "British Society for Haematology / ISTH",
            year = 2020,
            citation = "Scully M, Hunt BJ, Benjamin S, et al. Guidelines on the diagnosis and management of TTP and other thrombotic microangiopathies. Br J Haematol. 2012;158:323-335. Zheng XL, Vesely SK, Cataland SR, et al. ISTH guidelines for the diagnosis and treatment of TTP. J Thromb Haemost. 2020;18:2486-2502.",
            category = "Haemostasis & Thrombosis",
            summary = "FRCPath Exam Focus: Emergency recognition and management of immune Thrombotic Thrombocytopenic Purpura (iTTP), PLASMIC score stratification, initiation of Therapeutic Plasma Exchange (TPE) with solvent-detergent FFP, Caplacizumab anti-vWF nanobody, Rituximab, and strict avoidance of platelet transfusions.",
            keyCutoffs = "• Diagnostic criteria: Severe microangiopathic haemolytic anaemia (MAHA) + thrombocytopenia without other evident cause\n• ADAMTS13 activity <10% confirms TTP diagnosis\n• PLASMIC score 6-7 predicts high probability of severe ADAMTS13 deficiency",
            stagingCriteria = "Acute presentation vs Relapse; Congenital TTP (Upshaw-Schulman) vs Immune TTP.",
            recommendations = "• Rec 1: Suspected TTP is an extreme medical emergency: initiate Therapeutic Plasma Exchange (TPE) with solvent-detergent FFP (SD-FFP, 1.5 plasma volume) within 4–8 hours of presentation without waiting for ADAMTS13 results.\n• Rec 2: Platelet transfusion is relatively contraindicated in TTP (risk of worsening microvascular thrombosis); reserve for life-threatening haemorrhage.\n• Rec 3: Administer high-dose corticosteroids (IV Methylprednisolone 1 g/day for 3 days followed by oral prednisolone 1 mg/kg/day) immediately alongside TPE.\n• Rec 4: Caplacizumab (anti-vWF nanobody 10 mg IV stat before TPE, then 10 mg SC daily post-TPE) should be added to upfront therapy to rapidly block platelet-vWF microthrombi and prevent early death.\n• Rec 5: Rituximab (375 mg/m² weekly x4 or 1000 mg days 1 and 15) should be administered in the first week to suppress anti-ADAMTS13 antibody production, reduce refractory disease, and prevent relapse.",
            url = "https://doi.org/10.1111/jth.15006",
            // Not yet checked against a primary source in this repo; needs consultant sign-off.
            isVerified = false
        ),
        ApprovedResource(
            id = "bsh_haem_gynae_2026",
            title = "Implementation and value of providing a multidisciplinary haematology and gynaecology service for individuals with bleeding and thrombotic disorders: A British Society for Haematology & United Kingdom Haemophilia Centre Doctors' Organisation Good Practice Paper",
            issuingBody = "British Society for Haematology & UKHCDO",
            year = 2026,
            citation = "Biss T, Millen E, Warren C, Raheja P, Garside S, Motwani J, et al. Br J Haematol. 2026;209(3):919–927.",
            category = "Haemostasis & Thrombosis",
            summary = "FRCPath Exam Focus: 2026 BSH & UKHCDO Good Practice Paper on multidisciplinary haematology-gynaecology care. Key SBA points: >=10% of those presenting with heavy menstrual bleeding (HMB) have an inherited bleeding disorder (up to 33% of adolescents), anticoagulant-associated HMB (apixaban/dabigatran have a lower HMB incidence), and LNG-IUS expulsion risk.",
            keyCutoffs = "• HMB prevalence: affects up to 25% of women; >=10% have an inherited bleeding disorder (IBD), rising to 33% in adolescents\n• 55% of GWBD report HMB; up to 70% of menstruating females on anticoagulants experience HMB\n• Only 15% of gynaecologists consider an IBD at initial consultation for HMB\n• GWBD require a median of 3 strategies for optimal response\n• Good-practice point (ungraded): in treatment-resistant HMB on anticoagulation, switching to apixaban or dabigatran may help, as they have a lower incidence of HMB (vs rivaroxaban)\n• Good-practice point (ungraded): LNG-IUS expulsion rates are reportedly higher in inherited bleeding disorders; menstrual suppression may reduce this\n• Peri-operative: Pre-op iron infusion and Hb optimization; liaison regarding neuraxial anaesthesia in anticoagulated women and IBDs",
            stagingCriteria = "PALM-COEIN Classification for Abnormal Uterine Bleeding (AUB):\n• Structural: Polyp, Adenomyosis, Leiomyoma (fibroids), Malignancy and hyperplasia\n• Non-structural: Coagulopathy, Ovulatory dysfunction, Endometrial, Iatrogenic, Not yet classified\n\nTarget Populations for MDT:\n1. Inherited or acquired bleeding disorder (including ITP) with HMB\n2. Pre-pubescent females with IBD in whom HMB is anticipated at menarche\n3. Adolescent females presenting with HMB from menarche (high prevalence of underlying IBD)\n4. Individuals receiving anticoagulants/antiplatelet therapy experiencing HMB\n5. Complex thrombotic risk requiring discussion of contraceptive choice, HMB, or HRT",
            recommendations = "• Rec 1 (Grade 2C): Women and girls with bleeding/thrombotic disorders or requiring anticoagulant therapy may benefit from a multidisciplinary haematology and gynaecology service.\n• Rec 2 (Grade 2D): Eligibility criteria for referral should be clear, documented and shared with potential referrers.\n• Rec 3 (Grade 2D): Patients should be seen jointly by a haematologist and gynaecologist, supported by a clinical nurse specialist.\n• Rec 4 (Grade 2D): Management decisions and peri-procedural planning should be communicated clearly to other healthcare providers, with patients receiving copies of all correspondence.\n• Rec 5 (Grade 2D): Patient health outcomes and satisfaction should be monitored.\n• Rec 6 (Grade 2D): Opportunities for data collection and research should be optimised.\nNote: advice on DOAC switching and LNG-IUS use appears in the text as ungraded good practice, not as formal recommendations.",
            url = "https://doi.org/10.1111/bjh.70717"
        ),
        ApprovedResource(
            id = "bsh_poct_haematology_2026",
            title = "Point-of-care testing in general haematology: An update to the British Society for Haematology good practice paper",
            issuingBody = "British Society for Haematology",
            year = 2026,
            citation = "Tam J, De la Salle B, Kapuya P, Reilly-Stitt C, Smith-Straney T, Cambridge T, et al. Br J Haematol. 2026;209(4):1489–1504.",
            category = "General Haematology",
            summary = "FRCPath Exam Focus: 2026 BSH guidance update on point-of-care testing (POCT) in secondary care under ISO 15189:2022 standards. High-yield SBA concepts: handheld PT/INR meter inaccuracy above 4.0 and in APS, blood gas analyser (BGA) Hb disagreement with the central laboratory, and requiring quantitative high-sensitivity D-dimer matching STA or ELISA.",
            keyCutoffs = "• POCT PT/INR: Comparable to central lab at INR <=4.0 (stable window INR 2–4); marked discrepancies in antiphospholipid syndrome (APS); analytical strip lot bias at INR >=4.0\n• POCT D-dimer: Only high-sensitivity quantitative assays matching STA Liatest or ELISA should be used (Grade 1B); qualitative assays must NOT be used for DVT/PE rule-out\n• Blood Gas Analysers (BGA): results disagree with the central lab in BOTH directions. Some studies show BGA underestimation (risking unnecessary transfusion); one found 21% of patients who would be transfused on the lab Hct would NOT be on the BGA Hct; another found systematic overestimation (mean 8 g/L). BGAs are not interchangeable with lab analysers (CLIA criteria) and cannot detect haemolysis. Rec (1C): do not substitute for lab Hb/Hct; may aid trends/immediate judgement\n• Activated Clotting Time (ACT): Device-specific thresholds must be established due to high inter-device variability (Grade 2B)\n• Data Retention: Patient POCT results retained permanently (at least 8 years); QC logs, calibration, and competency records for at least 5 years",
            stagingCriteria = "ISO 15189:2022 Quality Management System & Governance Framework:\n• Internal Quality Control (IQC) at decision-making thresholds + External Quality Assessment (EQA / UK NEQAS)\n• Standard Operating Procedures (SOPs) covering sample collection, analytical limitations, error flags, and reflex testing (e.g. HemoCue Hb <80 g/L reflexes to lab FBC & ferritin)\n• Adverse Incident Reporting: Mandatory reporting of transfusion-related POCT errors to the Serious Hazards of Transfusion (SHOT) haemovigilance scheme (e.g. BGA sample clotting/mixing error causing inappropriate transfusion and TACO)",
            recommendations = "• Rec 1 (Grade 1A): Formal POCT policy detailing governance, quality assurance, risk management, and mandatory training/competency before device use.\n• Rec 2 (Grade 1B): Only high-sensitivity D-dimer matching STA or ELISA should be used; validation of viscoelastic clinical algorithms and reference ranges; QA framework with IQC and EQA.\n• Rec 3 (Grade 1C): Blood gas analysers should not substitute laboratory-based Hb/Hct testing; real-time LIMS interfacing to prevent transcription errors.\n• Rec 4 (Grade 1B): A no-blame incident reporting culture; transfusion-related POCT errors must be reported to SHOT.\n• Rec 5 (Grade 2B): Device-specific thresholds should be established for ACT tests given inter-device variability.",
            url = "https://doi.org/10.1111/bjh.70758"
        ),
        ApprovedResource(
            id = "bsh_ttp_laboratory_2026",
            title = "Diagnosis and monitoring of thrombotic thrombocytopenic purpura: Guidance for laboratories from the British Society for Haematology",
            issuingBody = "British Society for Haematology",
            year = 2026,
            citation = "MacDonald S, Pitchford T, Singh D, Fretwell R, Jenkins PV, Lawrence C, et al. Br J Haematol. 2026;209(3):928–938.",
            category = "Haemostasis & Thrombosis",
            summary = "FRCPath Exam Focus: 2026 BSH laboratory guidance for suspected TTP. Vital SBA targets: pre-treatment 0.109 M (3.2%) sodium citrate tube, EDTA unsuitable, blood film review (ideally within 60 minutes) for platelets <30 x 10^9/L, ADAMTS13 turnaround standards, assay-specific grey zones, and anti-ADAMTS13 antibody testing within 72h.",
            keyCutoffs = "• Diagnostic Threshold: Severe ADAMTS13 activity <10 IU/dL (<10%) confirms TTP (93% of acute iTTP present with <1 IU/dL)\n• Equivocal / Grey Zone: 10–20 IU/dL for manual FRET and ELISA assays (requires clinical correlation, exclusion of other TMAs, anti-ADAMTS13 antibody testing, and an alternative assay). A comparable grey zone has NOT been established for automated platforms\n• Blood Film Trigger: Platelet count <30 x 10^9/L with microangiopathic haemolytic anaemia (MAHA) mandates blood film review within 60 minutes and immediate clinical haematologist escalation (Grade 1C)\n• Turnaround Times: Preliminary results ideally within 1 hour of sample receipt for rapid automated assays; within 6 hours for ELISA (Grade 1C). Caution: HemosIL CLEIA has been reported to give erroneously low (<10 IU/dL) results; confirm discordant results on an alternative assay\n• Sample Tube: 0.109 M (3.2%) sodium citrate collected BEFORE treatment (Grade 1B), filled to at least 90%. EDTA cannot be used (falsely low ADAMTS13 activity; EDTA chelates the zinc/calcium the metalloprotease needs)\n• Anti-ADAMTS13 Antibodies: Mandatory within 72 hours when ADAMTS13 activity <10 IU/dL (Grade 1B)",
            stagingCriteria = "TTP Laboratory Testing & Staging Algorithm:\n• Severe Deficiency: ADAMTS13 activity <10 IU/dL -> Confirms TTP (93% of iTTP present with <1 IU/dL)\n• Equivocal / Grey Zone (manual FRET/ELISA): ADAMTS13 activity 10–20 IU/dL -> Correlate clinically, consider other TMAs, anti-ADAMTS13 antibodies, and alternative assay\n• Autoantibody Stratification: Positive anti-ADAMTS13 IgG confirms iTTP; negative antibody suggests cTTP (Upshaw-Schulman), requiring genomic analysis of ADAMTS13 gene\n• Surveillance: Longitudinal monitoring must use the same analytical platform; open conformation predicted relapse (1-year 46% vs 9%), but conformation assays are NOT recommended for routine clinical use (2C)",
            recommendations = "• Context (ungraded): Suspected TTP is a medical emergency; availability of an ADAMTS13 result is not a prerequisite for immediate management or treatment.\n• Rec 2 (Grade 1B): Samples collected into 0.109 M sodium citrate; with strong suspicion, send immediately to a regional TTP centre; anti-ADAMTS13 antibody assay must be performed within 72h when ADAMTS13 <10 IU/dL.\n• Rec 3 (Grade 1C): Documented processes so thrombocytopenic samples (especially platelets <30x10^9/L) are examined microscopically for haemolysis (ideally within 60 minutes) and escalated rapidly; preliminary ADAMTS13 activity within 1 hour for rapid automated assays or 6 hours for ELISA.\n• Rec 4 (Grade 1C): The same analytical method should be used in any single patient during monitoring to retain sensitivity for remission and relapse.\n• Rec 5 (Grade 2D): An ELISA is recommended over Bethesda-style inhibitor assays for anti-ADAMTS13 antibody detection.",
            url = "https://doi.org/10.1111/bjh.70727"
        ),
        ApprovedResource(
            id = "bsh_pmbcl_lymphoma_2026",
            title = "The diagnosis and management of primary mediastinal B-cell lymphoma: A British Society for Haematology guideline",
            issuingBody = "British Society for Haematology",
            year = 2026,
            citation = "Osborne W, Bielby H, Cwynarski K, El-Sharkawi D, Petrides G, Illidge T, et al. Br J Haematol. 2026;209(4):1505–1517.",
            category = "Malignant Haematology",
            summary = "FRCPath Exam Focus: 2026 BSH clinical guideline for PMBCL. Top SBA pearls: Safe omission of radiotherapy in Complete Metabolic Response (Deauville 1–3) based on the IELSG37 phase III trial (Grade 1A), dose-intensive induction (R-CHOP-14 or DA-R-EPOCH) over R-CHOP-21, thromboprophylaxis given a 28% thrombosis rate, and CD19 CAR T-cell therapy (liso-cel) for early relapse.",
            keyCutoffs = "• Thrombosis Rate: thrombotic complications in 28% of patients (retrospective series, n=156; most common at presentation) -> prophylactic anticoagulation recommended for patients with platelets >50 x 10^9/L (Grade 1C)\n• Immunophenotype: CD19+, CD20+, CD22+, CD79a+; CD30 usually + (weak/heterogeneous vs cHL); usually CD23+ and IRF4+; CD10+ in only 20-30%; usually CD15- (occasional cases CD15+); EBER- (EBV negative); PDL1 and especially PDL2 expressed\n• Genetics: 9p24.1 copy gain (PDL1/PDL2 and JAK2 amplification); mutations in SOCS1 (60–91%), GNA13 (47–53%), STAT6 (33–58%), NFKBIE, TNFAIP3\n• Deauville Criteria at End-of-Treatment (EOT) PET-CT (6 weeks post cycle 6):\n  - DS 1–3: Complete Metabolic Response (CMR)\n  - DS 4: Uptake above liver. Re-biopsy recommended if feasible (Grade 1B); personalized decision between serial surveillance PET (in 4–6 weeks) and consolidative RT\n  - DS 5: Uptake markedly above liver (>3x liver in many centres). Re-biopsy and systemic treatment as for relapsed disease (Grade 1B)",
            stagingCriteria = "PET-CT Response Assessment (Lugano / Deauville Criteria at 6 weeks post-treatment):\n• DS 1–3: Complete Metabolic Response (CMR) -> Omit consolidative radiotherapy (IELSG37 trial: 5-year OS 99% with no survival detriment)\n• DS 4: Uptake above liver -> Heterogeneous. MDT review; re-biopsy recommended if feasible; serial surveillance PET in 4-6 weeks or personalized consolidative RT\n• DS 5: Uptake markedly above liver (>3x in many centres) -> High failure rate. Re-biopsy and systemic salvage therapy for relapsed disease\n• Salvage CAR-T: Lisocabtagene maraleucel (Liso-cel) licensed second-line for relapse within 12 months; Axi-cel or Liso-cel third-line",
            recommendations = "• Rec 1 (Grade 1A): Do NOT offer consolidative radiotherapy for patients with Deauville Score 1–3 (CMR) following chemoimmunotherapy (IELSG37 trial: 5-year OS 99%).\n• Rec 2 (Grade 1B): Offer R-CHOP-14 x6 or DA-R-EPOCH x6; R-CHOP-21 is NOT recommended due to significantly higher DS 5 failure and inferior EFS.\n• Rec 3 (Grade 1B): Re-biopsy is recommended if feasible for all DS 4 and DS 5 patients at end of treatment.\n• Rec 4 (Grade 1C): Thromboprophylaxis is recommended in view of the high thrombotic risk (28%) in patients with platelets >50x10^9/L.\n• Rec 5 (Grade 1C): Offer CD19 CAR T-cell therapy (liso-cel) as second-line treatment if patient relapses within 12 months.",
            url = "https://doi.org/10.1111/bjh.70768"
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
            labData = "NT-proBNP: 9,450 ng/L (NR <332)\nTroponin T: 0.052 µg/L (= 52 ng/L; staging threshold 0.035 µg/L)\neGFR: 62 mL/min/1.73m²\nUrine Albumin:Creatinine Ratio: 45 mg/mmol",
            options = listOf(
                "Stage II; Median survival not reached at 5 years",
                "Stage IIIA; Median survival approximately 76 months",
                "Stage IIIB; Median survival approximately 26 months",
                "Stage IIIC; Median survival approximately 4 months",
                "Stage IV; Median survival less than 1 month"
            ),
            correctIndex = 3,
            rationale = "According to the 2026 BSH AL Amyloidosis Guideline (Garg et al., Table 2) and the newly adopted AL-ISS system (Khwaja et al. 2026):\n• Stage IIIB is defined by NT-proBNP >8500 ng/L and Troponin T >=0.035 µg/L (median survival 26 months).\n• Stage IIIC defines ultra-poor risk, requiring NT-proBNP >8500 ng/L, Troponin T >=0.035 µg/L AND abnormal global longitudinal strain (GLS) >-9% (median survival 4 months).\n(The guideline table prints the troponin unit as 'ng/L'; 0.035 µg/L = 35 ng/L.)\nBecause this patient has NT-proBNP 9450, TnT 0.052, and GLS -7% (which is >-9%), she fulfills Stage IIIC criteria.",
            sourceResourceId = "bsh_amyloidosis_2026",
            guidelineSection = "Table 2: Staging of cardiac amyloidosis (AL-ISS 2026)",
            evidenceGrade = "Prognostic staging table (ungraded)",
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
            rationale = "Answer key: author consensus (the RCPath sample paper does not publish answers). Standard immunophenotypic staging:\n• Pro-B ALL: CD19+, HLA-DR+, TdT+, CD10-, Cyt Ig-, SmIg-\n• Common ALL: CD19+, CD10+ (CALLA), TdT+, Cyt Ig negative, SmIg-\n• Pre-B ALL: Characterized by presence of cytoplasmic mu heavy chains (Cytoplasmic Ig positive), CD19+, CD10+, with surface Ig (SmIg) negative.\n• SmIg-positive, TdT-negative blasts indicate a mature B-cell neoplasm (e.g. Burkitt), which WHO-HAEM5 does not classify as B-ALL (the old 'mature B-ALL / FAB L3' term).\nTherefore, the expression of Cyt Ig with negative SmIg defines Pre-B ALL.",
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
            rationale = "Answer key: author consensus (the RCPath sample paper does not publish answers). Per the BSH haemochromatosis guideline:\n• The target for induction venesection is reduction of serum ferritin to <50 mcg/L (ng/mL).\n• Serum transferrin saturation normalises late in the course of de-ironing and fluctuates substantially with dietary intake; it is not a reliable endpoint for venesection frequency during induction.\n• Liver MRI is useful for quantifying iron overload at baseline but is not required to monitor routine venesection.\nHence, optimal venesection is assessed by normalisation of serum ferritin.",
            sourceResourceId = "bsh_iron_haemochromatosis",
            guidelineSection = "Section: Monitoring of venesection therapy",
            evidenceGrade = "Not verified",
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
            rationale = "Answer key: author consensus (the RCPath sample paper does not publish answers). Per UKHCDO guidance on acquired coagulation inhibitors:\n• Both lupus anticoagulant and acquired factor VIII inhibitors cause an isolated prolonged APTT that fails to correct immediately on standard 50:50 mixing tests.\n• The DRVVT with platelet neutralisation procedure (or confirmatory high-phospholipid reagent) directly demonstrates phospholipid dependence: excess freeze-thawed platelets or hexagonal phase phosphatidylethanolamine will neutralize the lupus anticoagulant and shorten the clotting time, but will NOT neutralize a factor VIII inhibitor.\n• While a one-stage Factor VIII assay will show low FVIII in acquired haemophilia, lupus anticoagulant can also cause artifactual low FVIII levels due to inhibitor interference in one-stage clotting assays (requiring chromogenic assay or multiple dilutions).",
            sourceResourceId = "ukhcdo_haemostasis_2022",
            guidelineSection = "Laboratory Differential: Lupus Anticoagulant vs Specific Inhibitors",
            evidenceGrade = "Not verified",
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
            rationale = "According to BSH Guidelines on Transfusion & Management of Bleeding on Antithrombotic Agents:\n• For immediate reversal of life-threatening bleeding (such as intracranial haemorrhage) in patients on Warfarin, Prothrombin Complex Concentrate (PCC 25-50 IU/kg) plus intravenous Vitamin K (5-10 mg) is the treatment of choice.\n• PCC rapidly normalises clotting factor levels within 10-15 minutes without causing circulatory volume overload, whereas FFP takes hours to infuse and achieves sub-therapeutic factor recovery.",
            sourceResourceId = "bsh_transfusion_components",
            guidelineSection = "Emergency Warfarin Reversal Protocol",
            evidenceGrade = "Not verified",
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
            rationale = "According to BSH Guidelines on the Prevention of Transfusion-Associated Graft-versus-Host Disease (TA-GVHD):\n• All patients treated with purine analogues (e.g., Fludarabine, Cladribine, Pentostatin) must receive irradiated cellular blood components (red cells and platelets) indefinitely to prevent fatal TA-GVHD.\n• Leucocyte depletion alone does not prevent TA-GVHD; gamma or X-ray irradiation (25 Gy) is essential to inactivate donor T-lymphocytes.\n• CMV testing is not routinely required for an immunocompromised elderly adult on purine analogues in the UK unless undergoing allogeneic BMT.",
            sourceResourceId = "bsh_transfusion_components",
            guidelineSection = "Indications for Irradiated Cellular Components",
            evidenceGrade = "Not verified",
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
            rationale = "According to UKHCDO and BSH Guidelines for von Willebrand Disease:\n• Patients with severe Type 3 VWD lack both VWF and Factor VIII. Desmopressin (DDAVP) is ineffective due to lack of endogenous stores.\n• Replacement therapy requires a plasma-derived intermediate-purity Factor VIII concentrate that contains high amounts of functional von Willebrand factor (e.g., Haemate-P, Wilate).\n• Cryoprecipitate is reserved only for situations where virally-inactivated factor concentrates are unavailable.",
            sourceResourceId = "bsh_transfusion_components",
            guidelineSection = "Management of von Willebrand Disease in Obstetrics",
            evidenceGrade = "Not verified",
            yearPaper = "RCPath Sample EMQ Q6"
        ),

        // Question 9: RCPath Sample EMQ 4 - TTP Post-Partum Plasma Exchange
        Question(
            id = "q_emq_transfusion_04",
            domain = "Transfusion Medicine",
            questionType = "EMQ",
            title = "Blood Product Selection: Post-Partum Microangiopathic Haemolytic Anaemia",
            stem = "A 35-year-old woman develops microangiopathic haemolytic anaemia at 40 weeks gestation. The platelet count continues to fall 24 hours after delivery to 18x10^9/L. Her coagulation screen is completely normal. Spherocytes and red cell fragments are present in the peripheral blood film. Therapeutic plasma exchange is urgently commenced.\n\nFrom the standard blood component options, select the most suitable replacement fluid for plasma exchange.",
            labData = "Platelets: 18x10^9/L\nHb: 76 g/L\nLDH: 1450 IU/L\nPT/APTT: Normal\nBlood Film: Spherocytes and red cell fragments",
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
            rationale = "According to BSH Guidelines on the Diagnosis and Management of Thrombotic Thrombocytopenic Purpura (TTP):\n• Therapeutic Plasma Exchange (TPE) with Solvent Detergent Fresh Frozen Plasma (SD-FFP, Octaplas) is the gold-standard treatment of choice for TTP.\n• SD-FFP replenishes functional ADAMTS13 protease while removing ADAMTS13 autoantibodies and ultra-large VWF multimers.\n• In the UK, SD-FFP is preferred over standard single-donor FFP due to its reduced risk of pathogen transmission and lower incidence of allergic/TRALI reactions.\n• Platelet transfusions are contraindicated in TTP unless life-threatening haemorrhage is present.",
            sourceResourceId = "bsh_ttp_guidelines",
            guidelineSection = "TTP Therapeutic Plasma Exchange Replacement Fluids",
            evidenceGrade = "Not verified",
            yearPaper = "RCPath Sample EMQ Q7"
        ),

        // Question 10: RCPath Sample EMQ 5 - Hypofibrinogenaemia Surgery
        Question(
            id = "q_emq_transfusion_05",
            domain = "Transfusion Medicine",
            questionType = "EMQ",
            title = "Blood Product Selection: Hypofibrinogenaemia Elective Surgery",
            stem = "A 28-year-old man with inherited hypo/dysfibrinogenaemia requires an elective mastoidectomy. Baseline fibrinogen level is 0.6 g/L.\n\nFrom the standard blood component options, select the most suitable product to achieve surgical hemostasis.",
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
            rationale = "According to BSH & UKHCDO Guidelines on the Management of Rare Inherited Bleeding Disorders:\n• In congenital hypo- or afibrinogenaemia requiring prophylaxis for elective surgery, pasteurized virally-inactivated Fibrinogen Concentrate (e.g., Riastap / Fibryga) is the treatment of choice.\n• Target fibrinogen trough level is typically >=1.5 g/L to 2.0 g/L for major surgery.\n• Fibrinogen concentrate provides precise dosing with minimal volume and carries a vastly lower risk of viral transmission and allergic reactions compared to cryoprecipitate.",
            sourceResourceId = "bsh_transfusion_components",
            guidelineSection = "Treatment of Congenital Fibrinogen Disorders",
            evidenceGrade = "Not verified",
            yearPaper = "RCPath Sample EMQ Q8"
        ),

        // Question 11: RCPath 2019 Paper 1 Essay - Multiple Myeloma Acute Management & Spinal Cord Compression
        Question(
            id = "q_essay_myeloma_2019",
            domain = "Haemato-Oncology",
            questionType = "ESSAY",
            title = "Multiple Myeloma Presentation, Acute Renal Injury & Cord Compression",
            stem = "A previously fit and healthy 62-year-old man presents with fatigue and back pain and is found to have a Hb of 82 g/L and creatinine of 350 umol/L. A diagnosis of multiple myeloma is suspected.\n\n(a) Discuss the initial investigations and urgent acute management that is required. (10 marks)\n(b) What are the recommended options for imaging in newly diagnosed patients with myeloma? (4 marks)\n(c) Discuss the options for therapy for this man and the factors that would guide your decisions. (6 marks)\n(d) 28 months later, he presents with rapid onset pain and weakness in his left leg associated with urinary incontinence. Discuss the likely diagnosis and initial plans for management. (5 marks)\n\n[RCPath Paper 1, September 2019, Question 2 - 25 marks]",
            labData = "Hb: 82 g/L\nCreatinine: 350 umol/L",
            options = emptyList(),
            correctIndex = -1,
            rationale = "This is a past FRCPath Part 1 essay question examining urgent acute myeloma management, imaging modalities, frontline therapy, and metastatic spinal cord compression (MSCC). Grounded in BSH Myeloma and IMWG guidelines.",
            sourceResourceId = "imwg_myeloma_2021",
            guidelineSection = "Section: Myeloma Emergencies & SLiM-CRAB Criteria",
            evidenceGrade = "Not verified",
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
            stem = "A 54-year-old male with Hb SS, recurrent crises, history of acute chest syndrome, and pulmonary hypertension is admitted following a mechanical fall with a fractured neck of femur. He needs urgent orthopaedic surgery. He is blood group B RhD negative. Hb S was 84% 4 months ago, and Hb at admission is 90 g/L. He is reluctant to accept transfusion due to blood safety concerns and his wife is a Jehovah's Witness.\n\n(a) Discuss how you will manage his concerns and what you need to do to optimise him for surgery. Discuss potential complications in this patient including those related to treatment. (20 marks)\n(b) Describe the specification of blood components, volume required for exchange transfusion and targets for treatment. (5 marks)\n\n[RCPath Paper 1, September 2019, Question 1 - 25 marks]",
            labData = "Hb: 90 g/L\nHb S: 84%\nBlood Group: B RhD Negative\nDiagnosis: Right neck of femur fracture; pulmonary hypertension",
            options = emptyList(),
            correctIndex = -1,
            rationale = "FRCPath Part 1 essay testing haemoglobinopathy peri-operative optimization, red cell exchange vs simple transfusion, blood safety ethics, extended red cell phenotyping, and transfusion targets per BSH Sickle Cell Guidelines.",
            sourceResourceId = "bsh_sickle_transfusion",
            guidelineSection = "BSH Guidelines: Transfusion in Sickle Cell Disease",
            evidenceGrade = "Not verified",
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
            stem = "A 22-year-old woman presents with a 2-week history of bruising on her limbs and epistaxis. Full blood count reveals Hb 96 g/L, WBC 135x10^9/L, Platelets 20x10^9/L. Blood film demonstrates numerous immature myeloid cells with folded nuclei, heavy granulation, and infrequent Auer rods. FISH confirms PML-RARA rearrangement.\n\nPart 1: What is the diagnosis? What immediate additional tests are required? Describe your initial management. What complications of treatment would you advise the patient about? (12 marks)\nPart 2: 5 days after starting treatment she is increasingly breathless, has gained 5 kg, and CXR shows patchy bilateral opacification. What is the differential diagnosis, and how would you manage each cause? (8 marks)\nPart 3: She achieved CR; 18 months after finishing treatment there is molecular relapse (Hb 112 g/L, WCC 1.3, platelets 60). How would you manage her now? (5 marks)\n\n[RCPath Paper 1, September 2018, Question 1 - 25 marks]",
            labData = "WBC: 135x10^9/L (Hyperleukocytic)\nHb: 96 g/L\nPlatelets: 20x10^9/L\nGenetics: PML-RARA positive",
            options = emptyList(),
            correctIndex = -1,
            rationale = "High-yield FRCPath Part 1 essay covering oncologic emergency management of high-risk APL, differentiation syndrome diagnostic criteria and treatment, and molecular relapse salvage.",
            sourceResourceId = "bsh_apl_guidelines",
            guidelineSection = "ELN 2019 Recommendations: Acute Promyelocytic Leukaemia",
            evidenceGrade = "Not verified",
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
    * Transfuse platelets to maintain count >30-50x10^9/L (even without active bleeding).
    * Cryoprecipitate or fibrinogen concentrate to maintain fibrinogen >1.0-1.5 g/L.
    * Fresh frozen plasma to correct prolonged PT/APTT.
    * Monitor coags 2-3 times daily until coagulopathy resolves.
  - Allopurinol or rasburicase with IV hydration for TLS.
• Complications of Treatment:
  - Differentiation syndrome (fever, weight gain, dyspnoea, pleural effusions).
  - QTc prolongation and ventricular arrhythmias (with Arsenic Trioxide).
  - Severe bleeding / catastrophic fatal intracranial haemorrhage.
  - Hyperleukocytosis.
  - Pseudotumor cerebri (headache, papilloedema from ATRA).
  - Hepatotoxicity; ATO-associated hypokalaemia and hypomagnesaemia, which increase QTc/arrhythmia risk (keep K+ and Mg2+ replete; monitor ECG).

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
            stem = "A 65-year-old male presents with lightheadedness on standing, chronic nocturnal diarrhoea, and extensive spontaneous periorbital ecchymoses ('raccoon eyes'). Lying BP is 138/84 mmHg, which falls to 102/64 mmHg at 3 minutes of standing with a heart rate change from 68 to 72 bpm. Coagulation screen shows prolongation of both PT (19s) and APTT (46s) with normal fibrinogen and platelets; Factor X activity is 18%.\n\nAccording to the 2026 BSH AL Amyloidosis Guideline, what is the underlying mechanism of the ecchymoses and postural hypotension?",
            labData = "Lying BP: 138/84 mmHg\nStanding BP (3 min): 102/64 mmHg (HR 72 bpm)\nPT: 19s (Control 12s)\nAPTT: 46s (Control 30s)\nFactor X activity: 18%\nPlatelets: 215x10^9/L",
            options = listOf(
                "Primary idiopathic thrombocytopenic purpura with vasovagal syncope",
                "Factor X deficiency due to binding by amyloid fibrils and neurogenic orthostatic hypotension from autonomic neuropathy",
                "Scurvy with adrenal insufficiency",
                "Warfarin toxicity with diabetic autonomic neuropathy",
                "Severe Von Willebrand disease with sympathetic hyperreactivity"
            ),
            correctIndex = 1,
            rationale = "According to the 2026 BSH Guideline on AL Amyloidosis (Garg et al.):\n• Periorbital ecchymoses (purpura) and bleeding in AL amyloidosis are classic hallmark signs caused by vascular amyloid fragility and acquired Factor X deficiency (caused by rapid clearance and adsorption of circulating Factor X onto amyloid fibrils in tissues). Factor X sits in the common pathway, so its deficiency prolongs BOTH the PT and the APTT.\n• Neurogenic orthostatic hypotension is defined as a sustained fall in blood pressure >=20/10 mmHg within 3 minutes of standing with blunted compensatory heart rate elevation (<10 bpm), characteristic of autonomic amyloid infiltration.\n• Both bleeding due to Factor X deficiency and postural drop >20 mmHg are explicit criteria for urgent haematology clinic referral.",
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
            stem = "A 71-year-old woman develops a fall in platelet count from 240x10^9/L to 65x10^9/L on post-operative day 7 following hip replacement surgery while receiving prophylactic Low-Molecular-Weight Heparin (enoxaparin). Ultrasound confirms a new extensive femoral deep vein thrombosis. Her calculated 4Ts score is 7 (High Probability).\n\nAccording to BSH guidance, what is the most appropriate immediate management?",
            labData = "Pre-op Platelets: 240x10^9/L\nDay 7 Platelets: 65x10^9/L (drop >50%)\nCoagulation Screen: Normal\nDoppler: Acute right femoral DVT",
            options = listOf(
                "Switch enoxaparin to therapeutic unfractionated heparin infusion and recheck platelets in 24 hours",
                "Stop enoxaparin, immediately start therapeutic non-heparin anticoagulation (e.g., Argatroban or Fondaparinux), and request anti-PF4/heparin antibodies",
                "Administer 2 adult doses of irradiated platelets and start Warfarin 10 mg stat",
                "Continue enoxaparin and insert an inferior vena cava (IVC) filter",
                "Discontinue enoxaparin and observe without any anticoagulation until antibody results return"
            ),
            correctIndex = 1,
            rationale = "According to the BSH HIT guideline (Watson et al. 2012):\n• In patients with intermediate or high 4Ts scores (score >=4), ALL heparin products must be discontinued immediately.\n• HIT is a paradoxical prothrombotic emergency; therapeutic-dose non-heparin anticoagulation (argatroban or danaparoid; fondaparinux off-label) should be started immediately without waiting for laboratory confirmation.\n• Platelet transfusions are contraindicated because they can fuel further thrombosis.\n• Warfarin is strictly contraindicated in the acute phase of HIT due to the risk of microvascular thrombosis and coumarin-induced skin necrosis/limb gangrene resulting from rapid protein C depletion.",
            sourceResourceId = "ukhcdo_haemostasis_2022",
            guidelineSection = "Section: Immediate Management of Suspected HIT",
            evidenceGrade = "Not verified",
            yearPaper = "Theme: RCPath Paper 1 March 2018 Q4 (HIT)"
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
            evidenceGrade = "Not verified",
            yearPaper = "Adapted from RCPath Paper 1 March 2019 Q4 (essay)"
        ),

        // Questions for Biss et al. (2026) - Gynaecology & Bleeding Disorders
        Question(
            id = "q_gynae_01",
            domain = "Haemostasis & Thrombosis",
            questionType = "MCQ",
            title = "Biss 2026: Anticoagulation-Induced Heavy Menstrual Bleeding",
            stem = "A 28-year-old woman receiving therapeutic anticoagulation with rivaroxaban for unprovoked deep vein thrombosis develops severe heavy menstrual bleeding requiring repeated hospital attendance despite first-line measures. According to the 2026 BSH/UKHCDO Good Practice Paper on multidisciplinary haematology-gynaecology care, which anticoagulant modification is recommended to reduce HMB while maintaining therapeutic anticoagulation?",
            options = listOf(
                "Switch anticoagulation to apixaban or dabigatran, which have a lower incidence of HMB",
                "Permanently discontinue all anticoagulation without haematological review",
                "Increase the rivaroxaban dose during menses to promote clot stabilization",
                "Switch to low-dose unfractionated heparin without monitoring",
                "Add high-dose aspirin alongside rivaroxaban"
            ),
            correctIndex = 0,
            rationale = "According to the BSH/UKHCDO 2026 Good Practice Paper (Biss et al., Section on Individuals receiving anti-coagulant therapy), oral anticoagulant agents may need to be changed in treatment-resistant HMB. Apixaban or dabigatran have a lower incidence of heavy menstrual bleeding compared to rivaroxaban (an ungraded good-practice point in the text, not a formal recommendation). Complete cessation of anticoagulation carries unacceptable risk of recurrent VTE.",
            sourceResourceId = "bsh_haem_gynae_2026",
            guidelineSection = "Section: Individuals receiving anti-coagulant therapy",
            evidenceGrade = "Consensus"
        ),
        Question(
            id = "q_gynae_02",
            domain = "Haemostasis & Thrombosis",
            questionType = "MCQ",
            title = "Biss 2026: Intrauterine System Expulsion in Bleeding Disorders",
            stem = "A 17-year-old girl with severe Type 1 von Willebrand disease is considering a levonorgestrel-releasing intrauterine system (LNS-IUS / Mirena) for persistent heavy menstrual bleeding. Which clinical counseling point is emphasized in the 2026 BSH/UKHCDO guideline?",
            options = listOf(
                "Women with inherited bleeding disorders have higher IUS expulsion rates, and menstrual suppression can reduce this risk",
                "LNS-IUS is strictly contraindicated in all women with inherited bleeding disorders",
                "The device must only be inserted after full surgical hysterectomy",
                "Coagulation factor concentrates are contraindicated during intrauterine procedures",
                "Expulsion rates are zero in adolescent bleeding disorder populations"
            ),
            correctIndex = 0,
            rationale = "The BSH/UKHCDO 2026 guideline highlights that there are reports of increased intrauterine device expulsion rates in women with inherited bleeding disorders (IBDs). The role of medical menstrual suppression to decrease this expulsion risk has been described and should be discussed with the patient.",
            sourceResourceId = "bsh_haem_gynae_2026",
            guidelineSection = "Section: Individuals with inherited bleeding disorders",
            evidenceGrade = "Consensus"
        ),
        Question(
            id = "q_gynae_03",
            domain = "Haemostasis & Thrombosis",
            questionType = "EMQ",
            title = "Biss 2026: PALM-COEIN Classification for Abnormal Uterine Bleeding",
            stem = "A 22-year-old female presents to the combined clinic with menorrhagia since menarche and family history of easy bruising. According to the FIGO PALM-COEIN classification adopted by BSH/UKHCDO 2026, which category does an underlying von Willebrand disease represent?",
            options = listOf(
                "Coagulopathy (Non-structural cause)",
                "Adenomyosis (Structural cause)",
                "Leiomyoma (Structural cause)",
                "Endometrial (Structural cause)",
                "Iatrogenic (Structural cause)"
            ),
            correctIndex = 0,
            rationale = "In the FIGO PALM-COEIN system adopted in the BSH/UKHCDO guideline, PALM represents structural etiologies (Polyp, Adenomyosis, Leiomyoma, Malignancy), whereas COEIN represents non-structural etiologies (Coagulopathy, Ovulatory dysfunction, Endometrial, Iatrogenic, Not yet classified). Inherited bleeding disorders fall under Coagulopathy.",
            sourceResourceId = "bsh_haem_gynae_2026",
            guidelineSection = "Figure 1: PALM-COEIN Classification",
            evidenceGrade = "Consensus"
        ),

        // Questions for Tam et al. (2026) - POCT in General Haematology
        Question(
            id = "q_poct_01",
            domain = "General Haematology",
            questionType = "MCQ",
            title = "Tam 2026: Point-of-Care D-Dimer for VTE Exclusion",
            stem = "An ambulatory care unit is evaluating a bedside point-of-care test for D-dimer to aid in the exclusion of deep vein thrombosis. According to the 2026 BSH Point-of-Care Testing guideline (Tam et al.), what is the specific recommendation regarding POCT D-dimer assays?",
            options = listOf(
                "Only high-sensitivity D-dimer tests with performance matching STA or ELISA laboratory assays should be used (Grade 1B)",
                "Qualitative POCT D-dimer dipstick assays are sufficient to exclude PE in high-risk patients",
                "Any POCT D-dimer device may be used without comparison to laboratory reference methods",
                "D-dimer testing should be replaced entirely by bedside ultrasound without clinical pre-test probability scoring",
                "POCT D-dimer has higher sensitivity than automated central laboratory latex immunoassays"
            ),
            correctIndex = 0,
            rationale = "According to the 2026 BSH POCT guideline recommendation (Grade 1B), only high-sensitivity D-dimer tests with performance matching STA or ELISA laboratory assays should be used. Meta-analyses have shown that qualitative POCT D-dimer assays have limited utility due to low sensitivity and must not be used to rule out VTE.",
            sourceResourceId = "bsh_poct_haematology_2026",
            guidelineSection = "Recommendations: Point-of-care D-dimer testing",
            evidenceGrade = "Grade 1B"
        ),
        Question(
            id = "q_poct_02",
            domain = "General Haematology",
            questionType = "MCQ",
            title = "Tam 2026: Blood Gas Analysers vs Central Laboratory Haemoglobin",
            stem = "In an emergency department, a patient with upper GI bleeding has a blood gas analyser (BGA) haemoglobin of 72 g/L. The central laboratory full blood count returns a haemoglobin of 84 g/L. According to the 2026 BSH Point-of-Care Testing guideline, what is the key clinical guidance regarding BGAs?",
            options = listOf(
                "Blood gas analysers should not substitute laboratory-based testing of Hb/Hct but may aid in monitoring trends (Grade 1C)",
                "BGAs are fully interchangeable with central laboratory analysers under CLIA criteria",
                "BGA haemoglobin measurements are unaffected by haemolysis or lipaemia",
                "Blood transfusion should always be triggered solely by BGA without laboratory verification",
                "Centrifugal BGA methods consistently overestimate haemoglobin compared to central laboratories"
            ),
            correctIndex = 0,
            rationale = "The BSH 2026 guideline explicitly recommends that blood gas analysers should not substitute laboratory-based testing of Hb/Hct (Grade 1C). Studies disagree in both directions: BGA underestimation can trigger unnecessary transfusion, while one study found 21% of patients who would be transfused on the lab Hct would not be on the BGA Hct, and another found systematic BGA overestimation (mean 8 g/L). BGAs are therefore not interchangeable with lab analysers.",
            sourceResourceId = "bsh_poct_haematology_2026",
            guidelineSection = "Recommendations: Blood gas Hb/haematocrit measurement",
            evidenceGrade = "Grade 1C"
        ),
        Question(
            id = "q_poct_03",
            domain = "General Haematology",
            questionType = "MCQ",
            title = "Tam 2026: POCT PT/INR Limitations in Antiphospholipid Syndrome",
            stem = "A 42-year-old woman with triple-positive antiphospholipid syndrome on warfarin attends a clinic where an electromechanical POCT PT/INR meter (CoaguChek) reports an INR of 2.4. Which limitation of POCT PT/INR devices is highlighted in the 2026 BSH guideline?",
            options = listOf(
                "Clinically significant discrepancies occur in patients with antiphospholipid syndrome receiving warfarin, requiring central laboratory monitoring",
                "POCT PT/INR devices are completely immune to lupus anticoagulant interference",
                "POCT meters are accurate up to INR 12.0 without lot-to-lot bias",
                "POCT PT/INR is licensed for direct oral anticoagulant (DOAC) therapeutic drug monitoring",
                "Capillary INR is identical to venous laboratory INR in all thrombophilia conditions"
            ),
            correctIndex = 0,
            rationale = "The 2026 BSH guideline notes that despite widespread use, portable PT/INR devices have notable limitations: clinically significant discrepancies have been observed in patients with antiphospholipid syndrome receiving warfarin. In addition, at INRs >=4, analytical bias exists between test strip lots.",
            sourceResourceId = "bsh_poct_haematology_2026",
            guidelineSection = "Section: Point-of-care PT/INR testing for anticoagulation management",
            evidenceGrade = "Grade 1B"
        ),

        // Questions for MacDonald et al. (2026) - TTP & ADAMTS13
        Question(
            id = "q_ttp_01",
            domain = "Haemostasis & Thrombosis",
            questionType = "MCQ",
            title = "MacDonald 2026: Emergency Suspicion & Sample Requirements for TTP",
            stem = "A 34-year-old previously well woman presents to the acute medical unit with confusion, petechiae, a platelet count of 18 x 10^9/L, and microangiopathic haemolytic anaemia with abundant schistocytes. Which statement accurately reflects the 2026 BSH laboratory guideline (MacDonald et al.)?",
            options = listOf(
                "Suspected TTP is a medical emergency: ADAMTS13 activity availability is not a prerequisite for initiating treatment, and pre-treatment blood must be collected in 0.109 M sodium citrate",
                "Plasma exchange must be withheld until quantitative ADAMTS13 activity confirmation is received",
                "Blood samples for ADAMTS13 activity should be collected into EDTA tubes to stabilize metalloproteinase cleavage",
                "Blood film review for thrombocytopenia with haemolysis should be deferred until the next routine laboratory shift",
                "An ADAMTS13 activity of 25 IU/dL confirms acute immune-mediated TTP"
            ),
            correctIndex = 0,
            rationale = "The BSH 2026 guideline emphasizes that suspected TTP is a medical emergency where availability of ADAMTS13 activity is NOT a prerequisite for immediate treatment (stated as context, ungraded). Samples should be collected into 0.109 M (3.2%) sodium citrate BEFORE treatment (Grade 1B). EDTA cannot be used because it chelates divalent cations (zinc and calcium), causing falsely low ADAMTS13 activity.",
            sourceResourceId = "bsh_ttp_laboratory_2026",
            guidelineSection = "Section: Laboratory Investigation for TTP & Pre-analytical considerations",
            evidenceGrade = "Grade 1B"
        ),
        Question(
            id = "q_ttp_02",
            domain = "Haemostasis & Thrombosis",
            questionType = "MCQ",
            title = "MacDonald 2026: ADAMTS13 Activity Cutoff & Turnaround Standards",
            stem = "According to the 2026 BSH laboratory guideline for TTP, which ADAMTS13 activity threshold confirms the diagnosis of TTP, and what is the recommended turnaround time for rapid automated assays in suspected cases?",
            options = listOf(
                "ADAMTS13 activity <10 IU/dL confirms TTP; preliminary results should be available ideally within 1 hour of sample receipt (Grade 1C)",
                "ADAMTS13 activity <50 IU/dL confirms TTP; preliminary results within 24 hours",
                "ADAMTS13 activity <30 IU/dL confirms TTP; preliminary results within 12 hours",
                "ADAMTS13 activity <5 IU/dL confirms TTP; results are only valid if tested by manual full-length VWF multimer assay",
                "ADAMTS13 activity <20 IU/dL confirms TTP; turnaround times of up to 48 hours are acceptable"
            ),
            correctIndex = 0,
            rationale = "The 2026 BSH guideline states that ADAMTS13 activity <10 IU/dL is the diagnostic cut-off for TTP (10–20 IU/dL is an equivocal grey zone for manual FRET/ELISA assays; not established for automated platforms). In suspected TTP, preliminary results should be available as soon as possible, ideally within 1 hour of sample receipt for rapid automated assays, or 6 hours for ELISA (Grade 1C).",
            sourceResourceId = "bsh_ttp_laboratory_2026",
            guidelineSection = "Section: Turnaround Times & Testing for ADAMTS13 Activity",
            evidenceGrade = "Grade 1C"
        ),
        Question(
            id = "q_ttp_03",
            domain = "Haemostasis & Thrombosis",
            questionType = "MCQ",
            title = "MacDonald 2026: Anti-ADAMTS13 Antibody Timing and Methodology",
            stem = "In a patient with confirmed severe ADAMTS13 deficiency (<10 IU/dL), what does the 2026 BSH guideline mandate regarding anti-ADAMTS13 antibody testing?",
            options = listOf(
                "Anti-ADAMTS13 antibody assays must be performed within 72 hours of presentation, and an ELISA is recommended over Bethesda-style inhibitor assays (Grade 1B/2D)",
                "Anti-ADAMTS13 antibody testing is unnecessary in acute TTP",
                "Bethesda-style inhibitor assays are superior to ELISA for detecting non-neutralising antibodies",
                "Antibody testing should only be performed after 6 months of clinical remission",
                "Presence of anti-ADAMTS13 antibodies confirms congenital TTP (Upshaw-Schulman syndrome)"
            ),
            correctIndex = 0,
            rationale = "The 2026 BSH guideline recommends that anti-ADAMTS13 antibody assays must be performed within 72h of presentation when ADAMTS13 activity is <10 IU/dL (Grade 1B). An ELISA is recommended over non-standardised Bethesda-style inhibitor assays or mixing studies because ELISA detects both neutralising and non-neutralising IgG antibodies (Grade 2D).",
            sourceResourceId = "bsh_ttp_laboratory_2026",
            guidelineSection = "Recommendations: Testing for antibodies against ADAMTS13",
            evidenceGrade = "Grade 1B"
        ),

        // Questions for Osborne et al. (2026) - PMBCL
        Question(
            id = "q_pmbcl_01",
            domain = "Haemato-Oncology",
            questionType = "MCQ",
            title = "Osborne 2026: Omission of Radiotherapy in Complete Metabolic Response",
            stem = "A 26-year-old woman with newly diagnosed bulky Primary Mediastinal B-cell Lymphoma (PMBCL) completes 6 cycles of DA-R-EPOCH. An end-of-treatment PET-CT scan performed at 6 weeks demonstrates Deauville Score 2 (Complete Metabolic Response). According to the 2026 BSH guideline (Osborne et al.) and the IELSG37 prospective trial, what is the recommended management?",
            options = listOf(
                "Do not offer consolidative radiotherapy; observation only is recommended (Grade 1A)",
                "Mandatory consolidative involved-site radiotherapy to 36 Gy regardless of PET response",
                "Immediate autologous stem cell transplantation as consolidation",
                "Maintenance rituximab for 2 years",
                "High-dose salvage chemotherapy with R-ICE"
            ),
            correctIndex = 0,
            rationale = "The 2026 BSH PMBCL guideline states: 'Do not offer radiotherapy for patients with DS 1–3 following chemoimmunotherapy (Grade 1A)'. This is supported by the landmark IELSG37 trial, which randomised patients in complete metabolic response (DS 1–3) to observation vs radiotherapy and showed no survival detriment with observation (5-year OS 99% in both arms).",
            sourceResourceId = "bsh_pmbcl_lymphoma_2026",
            guidelineSection = "Recommendations: Radiotherapy consolidation after first-line chemoimmunotherapy",
            evidenceGrade = "Grade 1A"
        ),
        Question(
            id = "q_pmbcl_02",
            domain = "Haemato-Oncology",
            questionType = "MCQ",
            title = "Osborne 2026: First-Line Chemoimmunotherapy Regimens",
            stem = "A 32-year-old man is diagnosed with anterior mediastinal PMBCL with superior vena cava compression. Which first-line chemotherapy approach is recommended by the 2026 BSH guideline?",
            options = listOf(
                "Offer dose-intensive R-CHOP-14 x6 or DA-R-EPOCH x6; R-CHOP-21 is not recommended (Grade 1B)",
                "R-CHOP-21 for 6 cycles is the preferred national standard of care",
                "Single-agent rituximab with involved-field radiation therapy",
                "CHOP-21 chemotherapy without anti-CD20 monoclonal antibody",
                "Immediate upfront CAR T-cell therapy without prior chemoimmunotherapy"
            ),
            correctIndex = 0,
            rationale = "The 2026 BSH guideline recommends dose-intensive chemoimmunotherapy: offer R-CHOP-14 x6 or DA-R-EPOCH x6 (Grade 1B). In the IELSG37 trial (not powered to compare induction regimens), patients given R-CHOP-21 had a significantly higher rate of Deauville 5 response (23.8% vs 8.2%) and inferior EFS compared with dose-intensive regimens; R-CHOP-21 is therefore not recommended (Grade 1B). A LYSA study found no efficacy difference between R-CHOP-14 and DA-R-EPOCH but more toxicity with DA-R-EPOCH.",
            sourceResourceId = "bsh_pmbcl_lymphoma_2026",
            guidelineSection = "Recommendations: First-Line Treatment",
            evidenceGrade = "Grade 1B"
        ),
        Question(
            id = "q_pmbcl_03",
            domain = "Haemato-Oncology",
            questionType = "MCQ",
            title = "Osborne 2026: Thromboprophylaxis & Second-Line CAR T-Cell Therapy",
            stem = "Which pair of recommendations is explicitly supported in the 2026 BSH PMBCL guideline?",
            options = listOf(
                "Thromboprophylaxis is recommended at presentation in patients with platelets >50 x 10^9/L (Grade 1C), and CD19 CAR T-cell therapy (liso-cel) is recommended for relapse within 12 months (Grade 1C)",
                "Anticoagulation is strictly contraindicated in PMBCL due to pericardial effusion risk, and autologous transplant is superior to CAR-T in primary refractory disease",
                "Surveillance PET-CT imaging should be performed every 3 months for 5 years in asymptomatic patients",
                "Central nervous system prophylaxis is mandatory in all PMBCL patients",
                "R-CHOP-21 is superior to DA-R-EPOCH in women under 40"
            ),
            correctIndex = 0,
            rationale = "The 2026 BSH PMBCL guideline emphasizes two crucial points: (1) Thrombotic complications occur in 28% of patients (most often around presentation); thromboprophylaxis is recommended if platelets >50 x 10^9/L (Grade 1C). (2) In patients with relapsed or refractory disease within 12 months, second-line high-dose chemotherapy has very poor outcomes; CD19 CAR T-cell therapy (liso-cel, TRANSFORM trial) is licensed and recommended second-line (Grade 1C). Routine surveillance imaging in asymptomatic patients in remission is not recommended (Grade 1B).",
            sourceResourceId = "bsh_pmbcl_lymphoma_2026",
            guidelineSection = "Recommendations: Presentation & Relapsed PMBCL",
            evidenceGrade = "Grade 1C"
        )
    )

    fun getFlashcards(): List<Flashcard> = listOf(
        Flashcard(
            id = "fc_01",
            domain = "Haemato-Oncology",
            topic = "AL Amyloidosis Staging",
            sourceResourceId = "bsh_amyloidosis_2026",
            front = "What defines AL-ISS Stage IIIC cardiac amyloidosis and what is its median survival?",
            back = "• NT-proBNP >8500 ng/L\n• Troponin T >=0.035 µg/L (35 ng/L; printed as 'ng/L' in the guideline table)\n• Global Longitudinal Strain (GLS) >-9%\n\nMedian Survival: Approximately 4 months (Ultra-poor risk group).",
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
            front = "How do you distinguish Common ALL from Pre-B ALL, and both from a mature B-cell (Burkitt) leukaemia, on flow cytometry?",
            back = "• Common ALL: CD10+ (CALLA), CD19+, TdT+, Cytoplasmic Ig negative, SmIg negative\n• Pre-B ALL: CD10+, CD19+, TdT+, Cytoplasmic Ig (mu heavy chain) POSITIVE, SmIg negative\n• Surface Ig POSITIVE, TdT negative: mature B-cell neoplasm (e.g. Burkitt). WHO-HAEM5 does not call this B-ALL; 'mature B-ALL / FAB L3' is obsolete.",
            guidelineRef = "WHO 5th Edition Haematolymphoid Tumours"
        ),
        Flashcard(
            id = "fc_08",
            domain = "Haemato-Oncology",
            topic = "APL Differentiation Syndrome",
            sourceResourceId = "bsh_apl_guidelines",
            front = "What is the clinical hallmark and immediate treatment of APL Differentiation Syndrome?",
            back = "• Hallmarks: Unexplained fever, weight gain (>5 kg), dyspnoea, bilateral pulmonary infiltrates, pleural/pericardial effusions.\n• Treatment: Dexamethasone 10 mg IV twice daily IMMEDIATELY at the earliest sign. Temporarily hold ATRA/ATO if severe.",
            guidelineRef = "ELN 2019 APL Recommendations"
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
            sourceResourceId = "bsh_sickle_transfusion",
            front = "What is the BSH approach to pre-operative transfusion in HbSS/HbSbeta0 sickle cell disease?",
            back = "• Medium-risk surgery: top-up transfusion to Hb ~100 g/L (TAPS trial)\n• High-risk surgery, or baseline Hb too high for top-up: consider exchange transfusion, target HbS <30%\n• Avoid post-transfusion Hb above ~100-110 g/L (hyperviscosity)",
            guidelineRef = "BSH Sickle Cell Disease Transfusion Guidelines"
        ),
        Flashcard(
            id = "fc_12",
            domain = "Haemostasis & Thrombosis",
            topic = "HIT 4Ts Score & Anticoagulation",
            sourceResourceId = "ukhcdo_haemostasis_2022",
            front = "In suspected HIT (4Ts >=4), what 3 key drug rules must be followed immediately?",
            back = "1. Stop ALL heparin immediately (including catheter line flushes).\n2. Start a therapeutic-dose non-heparin anticoagulant immediately (argatroban or danaparoid; fondaparinux off-label).\n3. Do NOT start Warfarin until platelet count has recovered >150x10^9/L (risk of venous limb gangrene from protein C depletion).",
            guidelineRef = "BSH HIT Guideline (Watson et al. 2012)"
        ),
        Flashcard(
            id = "fc_13",
            domain = "Haemostasis & Thrombosis",
            topic = "Factor VIII Inhibitor vs Lupus Anticoagulant",
            sourceResourceId = "ukhcdo_haemostasis_2022",
            front = "How do you distinguish an acquired Factor VIII inhibitor from a Lupus Anticoagulant on mixing studies?",
            back = "• Both cause an isolated prolonged APTT.\n• Lupus anticoagulant: immediate-acting, usually fails to correct on the immediate 50:50 mix; phospholipid-dependent, so the clotting time shortens with excess phospholipid (DRVVT confirm / platelet neutralisation).\n• FVIII inhibitor: time- and temperature-dependent; may correct immediately, then prolongs after 1-2 h incubation at 37°C; low FVIII on chromogenic or multi-dilution assay.",
            guidelineRef = "UKHCDO Guideline on Acquired Coagulation Inhibitors (Collins et al. 2013)"
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
            back = "High-sensitivity multiparameter flow cytometry showing loss of GPI-anchored markers: FLAER + CD24 on neutrophils and FLAER + CD14 on monocytes, with CD59 on red cells to type the RBC clone (CD55 alone is not recommended).",
            guidelineRef = "ICCS high-sensitivity PNH flow cytometry guidance"
        ),

        // Flashcards for Biss et al. (2026) - Gynaecology & Bleeding Disorders
        Flashcard(
            id = "fc_gynae_01",
            domain = "Haemostasis & Thrombosis",
            topic = "PALM-COEIN Classification & AUB",
            sourceResourceId = "bsh_haem_gynae_2026",
            front = "What is the FIGO PALM-COEIN classification for abnormal uterine bleeding (AUB) adopted in the 2026 BSH/UKHCDO guideline?",
            back = "PALM-COEIN Classification:\n• Structural (PALM):\n  - P: Polyp\n  - A: Adenomyosis\n  - L: Leiomyoma (fibroids)\n  - M: Malignancy and hyperplasia\n• Non-structural (COEIN):\n  - C: Coagulopathy (e.g. VWD, platelet disorders)\n  - O: Ovulatory dysfunction\n  - E: Endometrial\n  - I: Iatrogenic\n  - N: Not yet classified",
            guidelineRef = "Biss 2026 (FIGO FIG. 1)"
        ),
        Flashcard(
            id = "fc_gynae_02",
            domain = "Haemostasis & Thrombosis",
            topic = "Anticoagulants & HMB",
            sourceResourceId = "bsh_haem_gynae_2026",
            front = "In females on therapeutic anticoagulation with treatment-resistant heavy menstrual bleeding, which anticoagulant switch does BSH/UKHCDO 2026 describe?",
            back = "Switch to Apixaban or Dabigatran:\n• Both apixaban and dabigatran have a lower incidence of heavy menstrual bleeding compared to rivaroxaban (ungraded good-practice point).\n• Avoid complete interruption of anticoagulation without haematological risk-benefit appraisal due to recurrent VTE hazard.",
            guidelineRef = "Biss 2026 (Section: Anticoagulants)"
        ),
        Flashcard(
            id = "fc_gynae_03",
            domain = "Haemostasis & Thrombosis",
            topic = "GWBD & Intrauterine Systems",
            sourceResourceId = "bsh_haem_gynae_2026",
            front = "What key complication occurs with levonorgestrel-releasing intrauterine systems (LNS-IUS / Mirena) in women with bleeding disorders, and how is it mitigated?",
            back = "Key issue: Increased device expulsion rates in women with inherited bleeding disorders (IBDs).\n\nMitigation: Menstrual suppression has been described as reducing expulsion risk. Insertion may require sedation/anaesthesia and specialist technique in younger/adolescent patients.",
            guidelineRef = "Biss 2026 (Section: IBD Management)"
        ),

        // Flashcards for Tam et al. (2026) - POCT in General Haematology
        Flashcard(
            id = "fc_poct_01",
            domain = "General Haematology",
            topic = "POCT D-Dimer & VTE Exclusion",
            sourceResourceId = "bsh_poct_haematology_2026",
            front = "What is the BSH 2026 Grade 1B recommendation regarding point-of-care D-dimer testing in secondary care?",
            back = "Recommendation: Only high-sensitivity D-dimer tests with performance matching STA® or ELISA laboratory assays should be used (Grade 1B).\n\nClinical Pearl: Qualitative POCT D-dimer assays have poor sensitivity and should NOT be used to rule out DVT or PE in acute care.",
            guidelineRef = "Tam 2026 (Grade 1B)"
        ),
        Flashcard(
            id = "fc_poct_02",
            domain = "General Haematology",
            topic = "Blood Gas Analysers & Transfusion Triggers",
            sourceResourceId = "bsh_poct_haematology_2026",
            front = "Why are blood gas analysers (BGAs) not interchangeable with central laboratory analysers for Hb/Hct measurement?",
            back = "BGA vs Central Lab:\n• Disagreement runs in BOTH directions: some studies show BGA underestimation (unnecessary transfusion risk); one found 21% of patients who would be transfused on the lab Hct would not be on the BGA Hct; another found systematic overestimation (mean 8 g/L).\n• Not interchangeable under CLIA criteria; BGAs usually cannot detect haemolysis in whole blood.\n• BSH Rec (Grade 1C): Blood gas analysers should not substitute laboratory-based Hb/Hct testing, but may aid in monitoring trends.",
            guidelineRef = "Tam 2026 (Grade 1C)"
        ),
        Flashcard(
            id = "fc_poct_03",
            domain = "General Haematology",
            topic = "POCT PT/INR & Antiphospholipid Syndrome",
            sourceResourceId = "bsh_poct_haematology_2026",
            front = "What critical diagnostic trap applies to handheld electromechanical PT/INR meters (e.g. CoaguChek) in patients with antiphospholipid syndrome (APS)?",
            back = "Diagnostic Trap:\n• Clinically significant discrepancies occur in patients with antiphospholipid syndrome receiving warfarin due to variable lupus anticoagulant interference with thromboplastin reagents on test strips.\n• Central laboratory venous INR measurement must be used to verify therapeutic control in APS.\n• Also note: Strip lot-to-lot bias is significant when INR >= 4.0.",
            guidelineRef = "Tam 2026 (Section: PT/INR)"
        ),

        // Flashcards for MacDonald et al. (2026) - TTP & ADAMTS13
        Flashcard(
            id = "fc_ttp_01",
            domain = "Haemostasis & Thrombosis",
            topic = "ADAMTS13 Thresholds & Emergency Rule",
            sourceResourceId = "bsh_ttp_laboratory_2026",
            front = "What is the diagnostic threshold for ADAMTS13 activity in TTP, and what is the emergency rule regarding treatment initiation?",
            back = "Diagnostic Cutoffs:\n• ADAMTS13 activity <10 IU/dL is the diagnostic cut-off (93% of iTTP in the UK registry presented with <1 IU/dL).\n• 10–20 IU/dL is an equivocal grey zone for manual FRET/ELISA assays (not established for automated platforms; HemosIL CLEIA can read falsely low).\n\nEmergency Rule (context, ungraded): Availability of ADAMTS13 activity is NOT a prerequisite for starting treatment. In suspected TTP, start plasma exchange, steroids, and caplacizumab immediately without waiting for laboratory results.",
            guidelineRef = "MacDonald 2026"
        ),
        Flashcard(
            id = "fc_ttp_02",
            domain = "Haemostasis & Thrombosis",
            topic = "Pre-analytical Tube & Turnaround Standards",
            sourceResourceId = "bsh_ttp_laboratory_2026",
            front = "What sample tube is mandatory for ADAMTS13 testing, which tube is strictly forbidden, and what are the required turnaround times?",
            back = "Sample Requirements:\n• Tube: 0.109 M (3.2%) Sodium Citrate filled >=90% (Grade 1B).\n• FORBIDDEN: EDTA tubes cannot be used (EDTA chelates zinc and calcium, causing falsely low ADAMTS13 activity).\n\nTurnaround Times (Grade 1C):\n• Rapid automated assays: within 1 hour of sample receipt.\n• ELISA assays: within 6 hours of sample receipt.\n• Platelet count <30 x 10^9/L: blood film examined for haemolysis, ideally within 60 minutes, with rapid escalation (1C).",
            guidelineRef = "MacDonald 2026 (Grade 1B/1C)"
        ),
        Flashcard(
            id = "fc_ttp_03",
            domain = "Haemostasis & Thrombosis",
            topic = "Anti-ADAMTS13 Antibodies & Surveillance",
            sourceResourceId = "bsh_ttp_laboratory_2026",
            front = "What is the timing for anti-ADAMTS13 antibody testing, and how does the open ADAMTS13 conformation index predict relapse?",
            back = "Antibody Assay Timing:\n• Must be performed within 72 hours of presentation when ADAMTS13 activity <10 IU/dL (Grade 1B).\n• ELISA is recommended over Bethesda assays (detects neutralizing and non-neutralizing IgG).\n\nOpen Conformation Index:\n• Predicts clinical relapse before activity drops.\n• At peak activity (>60%), patients with open conformation had 46% 1-year relapse vs 9% with closed conformation.\n• BUT: conformation assays are NOT recommended for routine clinical use (2C); regional TTP centres only.",
            guidelineRef = "MacDonald 2026 (Section: Conformation)"
        ),

        // Flashcards for Osborne et al. (2026) - PMBCL
        Flashcard(
            id = "fc_pmbcl_01",
            domain = "Haemato-Oncology",
            topic = "IELSG37 Trial & RT Omission",
            sourceResourceId = "bsh_pmbcl_lymphoma_2026",
            front = "What is the landmark Grade 1A recommendation from BSH 2026 regarding radiotherapy in PMBCL patients in Complete Metabolic Response (DS 1–3)?",
            back = "BSH Recommendation (Grade 1A):\n• Do NOT offer consolidative radiotherapy for patients with Deauville Score 1–3 following first-line chemoimmunotherapy.\n\nEvidence: IELSG37 phase III trial showed 5-year OS was 99% in both observation and RT arms, confirming radiotherapy can be safely omitted in CMR without survival detriment.",
            guidelineRef = "Osborne 2026 (IELSG37, Grade 1A)"
        ),
        Flashcard(
            id = "fc_pmbcl_02",
            domain = "Haemato-Oncology",
            topic = "First-Line Induction & Why R-CHOP-21 is NOT Recommended",
            sourceResourceId = "bsh_pmbcl_lymphoma_2026",
            front = "Which first-line regimens are recommended for PMBCL in BSH 2026, and why is standard R-CHOP-21 explicitly not recommended?",
            back = "First-line Regimens (Grade 1B):\n• Recommended: Dose-intensive R-CHOP-14 x6 or DA-R-EPOCH x6.\n• NOT Recommended: R-CHOP-21 is not recommended.\n\nRationale: In IELSG37 (not powered to compare regimens), R-CHOP-21 had inferior EFS and roughly 3-fold more Deauville 5 responses (23.8% vs 8.2% with other regimens).",
            guidelineRef = "Osborne 2026 (Grade 1B)"
        ),
        Flashcard(
            id = "fc_pmbcl_03",
            domain = "Haemato-Oncology",
            topic = "Thrombosis & CAR T-Cell Salvage",
            sourceResourceId = "bsh_pmbcl_lymphoma_2026",
            front = "What are the specific BSH 2026 recommendations for (1) thrombosis prophylaxis and (2) relapsed PMBCL within 12 months?",
            back = "1. Thromboprophylaxis (Grade 1C):\n• Thrombotic complications in 28% of PMBCL patients (most often around presentation).\n• Prophylactic anticoagulation is recommended if platelet count >50 x 10^9/L.\n\n2. Early Relapse Salvage (Grade 1C):\n• Second-line high-dose chemotherapy has poor response in PMBCL.\n• CD19 CAR T-cell therapy with Lisocabtagene maraleucel (Liso-cel) is licensed and recommended second-line for relapse within 12 months (TRANSFORM trial).",
            guidelineRef = "Osborne 2026 (Grade 1C)"
        ),

        // Dedicated Guideline-Highlighting Flashcards across Core Guidelines
        Flashcard(
            id = "fc_guideline_apl_emergency",
            domain = "Haemato-Oncology",
            topic = "BSH/ELN APL Guideline: Emergency Treatment",
            sourceResourceId = "bsh_apl_guidelines",
            front = "ELN recommendation: What are the emergency steps for suspected Acute Promyelocytic Leukaemia (APL)?",
            back = "1. Start All-Trans Retinoic Acid (ATRA 45 mg/m²/day) IMMEDIATELY upon morphological suspicion; do NOT await cytogenetics or PML-RARA PCR.\n2. Coagulopathy targets: platelets >30-50x10^9/L and fibrinogen >1.0-1.5 g/L with cryoprecipitate or fibrinogen concentrate.\n3. Differentiation syndrome: start IV dexamethasone 10 mg BD at the first sign.",
            guidelineRef = "ELN 2019 APL Recommendations (Sanz et al.)"
        ),
        Flashcard(
            id = "fc_guideline_aps_doac",
            domain = "Haemostasis & Thrombosis",
            topic = "BSH Antiphospholipid Guideline: Anticoagulation Choice",
            sourceResourceId = "bsh_antiphospholipid",
            front = "Why should Direct Oral Anticoagulants (DOACs) be avoided in triple-positive Antiphospholipid Syndrome (APS)?",
            back = "• The TRAPS trial (rivaroxaban vs warfarin in triple-positive APS) was stopped early for excess arterial events (notably stroke) with rivaroxaban.\n• Warfarin (target INR 2.0–3.0) remains first-line for thrombotic APS; DOACs are not recommended in triple-positive or arterial APS.",
            guidelineRef = "BSH APS guidance / TRAPS trial"
        ),
        Flashcard(
            id = "fc_guideline_ttp_emergency_tpe",
            domain = "Haemostasis & Thrombosis",
            topic = "BSH TTP Guideline: Emergency Management & Platelets",
            sourceResourceId = "bsh_ttp_guidelines",
            front = "BSH guidance: What are the two emergency rules regarding (1) TPE initiation and (2) platelet transfusions in suspected TTP?",
            back = "1. TPE: start therapeutic plasma exchange with SD-FFP (initially 1.5 plasma volumes) ideally within 4–8 hours of presentation, without waiting for ADAMTS13 results.\n2. Platelets: relatively contraindicated (may worsen microvascular thrombosis); reserve for life-threatening haemorrhage.",
            guidelineRef = "BSH TTP Guideline (Scully et al. 2012)"
        ),
        Flashcard(
            id = "fc_guideline_vwd_type3",
            domain = "Transfusion Medicine",
            topic = "BSH Transfusion Guideline: Type 3 VWD in Emergency Bleeding",
            sourceResourceId = "bsh_transfusion_components",
            front = "What product is indicated for major haemorrhage or surgery in Type 3 Von Willebrand Disease (VWD), and why is DDAVP ineffective?",
            back = "Product of Choice:\n• Intermediate-purity plasma-derived Factor VIII/VWF concentrate (e.g., Haemate-P, Wilate).\n\nWhy DDAVP is Ineffective:\n• Type 3 VWD represents complete absence of VWF synthesis and storage; Desmopressin (DDAVP) cannot release endogenous VWF and provides zero haemostatic benefit.",
            guidelineRef = "UKHCDO VWD guidance"
        ),
        Flashcard(
            id = "fc_guideline_who_aml_genetics",
            domain = "Haemato-Oncology",
            topic = "WHO 5th Edition: AML Blast Threshold Exceptions",
            sourceResourceId = "who_haem_5th",
            front = "WHO-HAEM5: Which AML types still need >=20% blasts, and which defining genetic abnormalities allow an AML diagnosis below 20%?",
            back = "Below 20% blasts is allowed for AML with defining genetic abnormalities, e.g.:\n• PML::RARA, RUNX1::RUNX1T1, CBFB::MYH11\n• KMT2A rearrangement, DEK::NUP214, MECOM rearrangement, NUP98 rearrangement, NPM1 mutation\n\nStill require >=20%: AML with BCR::ABL1 and AML with CEBPA mutation.\n\nContrast: the ICC 2022 uses a >=10% threshold for most of these.",
            guidelineRef = "WHO Classification 5th Edition (Khoury et al. 2022)"
        ),
        Flashcard(
            id = "fc_guideline_sickle_hyperhaemolysis",
            domain = "Transfusion Medicine",
            topic = "BSH Sickle Cell Guideline: Hyperhaemolysis Syndrome",
            sourceResourceId = "bsh_sickle_transfusion",
            front = "BSH Guideline Recommendation: What is the clinical presentation and mandatory immediate management of Hyperhaemolysis Syndrome in Sickle Cell Disease?",
            back = "Clinical Hallmarks:\n• Severe post-transfusion drop in Hb to lower than pre-transfusion baseline.\n• Severe reticulocytopenia with destruction of both donor and autologous red cells.\n\nManagement:\n1. STOP ALL further blood transfusions where possible immediately (further blood triggers catastrophic hemolysis).\n2. Initiate high-dose IV methylprednisolone + IVIG (1 g/kg/day for 2 days).\n3. Consider Eculizumab for refractory hyperhaemolysis.",
            guidelineRef = "BSH Sickle Cell Transfusion Guideline (Davis et al. 2017)"
        ),
        Flashcard(
            id = "fc_guideline_myeloma_imaging",
            domain = "Haemato-Oncology",
            topic = "IMWG / BSH Multiple Myeloma Guideline: Primary Imaging",
            sourceResourceId = "imwg_myeloma_2021",
            front = "What is the first-line imaging modality for suspected Multiple Myeloma, and why?",
            back = "First-Line Imaging:\n• Whole-Body Low-Dose CT (WBLDCT) is the primary recommendation.\n\nClinical Rationale:\n• Conventional skeletal survey requires >30–50% trabecular bone loss to visualize a lesion, missing osteolytic disease in up to 30% of patients.\n• Whole-spine MRI is required if spinal cord compression is suspected.",
            guidelineRef = "IMWG / BSH / NICE myeloma imaging guidance"
        ),
        Flashcard(
            id = "fc_guideline_gynae_anticoag_switch",
            domain = "Haemostasis & Thrombosis",
            topic = "BSH/UKHCDO 2026: DOAC Switch for Heavy Menstrual Bleeding",
            sourceResourceId = "bsh_haem_gynae_2026",
            front = "BSH/UKHCDO 2026: In females on therapeutic anticoagulation with treatment-resistant heavy menstrual bleeding, what medication adjustment does the paper suggest?",
            back = "Good-practice point (ungraded, in the text; not a formal recommendation):\n• Consider changing the anticoagulant, e.g. to apixaban or dabigatran, which have a lower incidence of HMB (vs rivaroxaban).\n\nClinical Pearl:\n• Do not stop anticoagulation without haematology review (recurrent VTE risk).",
            guidelineRef = "Biss 2026 (Section: Individuals receiving anti-coagulant therapy)"
        ),
        Flashcard(
            id = "fc_guideline_poct_iso15189",
            domain = "General Haematology",
            topic = "BSH 2026 POCT Guideline: ISO 15189 Governance & SHOT Reporting",
            sourceResourceId = "bsh_poct_haematology_2026",
            front = "BSH 2026 Guideline Recommendation: What are the hospital laboratory governance and incident reporting requirements for Point-of-Care Testing (POCT)?",
            back = "Guideline Governance Standards (Grades 1A-1B):\n1. Hospital Central Laboratory Oversight: All POCT devices must operate under a formal hospital POCT policy and ISO 15189:2022 quality framework.\n2. Internal & External QA: Regular IQC at decision thresholds and enrollment in UK NEQAS/EQA schemes.\n3. Mandatory SHOT Reporting: All transfusion-related POCT adverse events (e.g. incorrect BGA Hb causing inappropriate transfusion) must be reported to SHOT.",
            guidelineRef = "Tam 2026 POCT Guideline"
        ),
        Flashcard(
            id = "fc_guideline_pmbcl_ielsg37",
            domain = "Haemato-Oncology",
            topic = "BSH 2026 PMBCL Guideline: Omission of Radiotherapy",
            sourceResourceId = "bsh_pmbcl_lymphoma_2026",
            front = "BSH 2026 Guideline Recommendation: What is the Grade 1A recommendation regarding consolidative mediastinal radiotherapy in PMBCL?",
            back = "Guideline Recommendation (Grade 1A):\n• Do NOT offer consolidative mediastinal radiotherapy for PMBCL patients with Deauville Score 1–3 (Complete Metabolic Response) on end-of-treatment PET-CT at 6 weeks.\n\nEvidence Base:\n• IELSG37 phase III trial showed 5-year Overall Survival of 99% in both observation and RT arms, sparing young patients long-term radiation-induced cardiovascular and secondary malignancy morbidity.",
            guidelineRef = "BSH 2026 PMBCL Guideline (Rec 1A; IELSG37 Trial)"
        )
    )
}

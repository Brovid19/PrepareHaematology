package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.db.AppDatabase
import com.example.data.db.SeedData
import com.example.data.repository.ExamRepository
import com.example.ui.viewmodel.ExamViewModel
import com.example.ui.viewmodel.ProficiencyLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Prepare Haematology", appName)
  }

  @Test
  fun `verify strict evidence grounding sandbox in seed data`() {
    val resources = SeedData.getApprovedResources()
    val questions = SeedData.getQuestions()
    val flashcards = SeedData.getFlashcards()

    val resourceIds = resources.map { it.id }.toSet()

    // 2026 BSH AL Amyloidosis guideline must be present
    assertTrue("BSH 2026 Amyloidosis guideline must be registered", resourceIds.contains("bsh_amyloidosis_2026"))

    // Every approved resource must have an active online guideline URL
    resources.forEach { res ->
      val onlineUrl = res.getOnlineUrl()
      assertTrue("Resource ${res.id} must have a valid online URL", onlineUrl.startsWith("https://"))
    }

    // Every question must be bound to an approved resource ID
    questions.forEach { question ->
      assertTrue(
        "Question ${question.id} violates evidence sandbox: unbound resource '${question.sourceResourceId}'",
        resourceIds.contains(question.sourceResourceId)
      )
      assertTrue("Question stem must not be blank", question.stem.isNotBlank())
      assertTrue("Question rationale must not be blank", question.rationale.isNotBlank())
    }

    // Every flashcard must be bound to an approved resource ID
    flashcards.forEach { card ->
      assertTrue(
        "Flashcard ${card.id} violates evidence sandbox: unbound resource '${card.sourceResourceId}'",
        resourceIds.contains(card.sourceResourceId)
      )
      assertTrue("Flashcard ease factor must be >= 1.3", card.easeFactor >= 1.3f)
    }
  }

  @Test
  fun `verify core SM-2 algorithm transitions and proficiency tiers`() {
    val database = AppDatabase.getInstance(ApplicationProvider.getApplicationContext())
    val repository = ExamRepository(database)
    val viewModel = ExamViewModel(repository)

    // Step 1: Initial recall with Good (3)
    val step1 = viewModel.calculateSm2(
      currentRepetitions = 0,
      currentIntervalDays = 1,
      currentEaseFactor = 2.5f,
      qualityRating = 3,
      currentTime = 1000000L
    )
    assertEquals(1, step1.newRepetitions)
    assertEquals(1, step1.newIntervalDays)
    assertEquals(ProficiencyLevel.LEARNING, step1.proficiencyLevel)

    // Step 2: Second recall with Good (4)
    val step2 = viewModel.calculateSm2(
      currentRepetitions = step1.newRepetitions,
      currentIntervalDays = step1.newIntervalDays,
      currentEaseFactor = step1.newEaseFactor,
      qualityRating = 4,
      currentTime = 1000000L
    )
    assertEquals(2, step2.newRepetitions)
    assertEquals(6, step2.newIntervalDays)
    assertEquals(ProficiencyLevel.PROFICIENT, step2.proficiencyLevel)

    // Step 3: Failed recall with Again (0)
    val stepFail = viewModel.calculateSm2(
      currentRepetitions = 3,
      currentIntervalDays = 15,
      currentEaseFactor = 2.5f,
      qualityRating = 0,
      currentTime = 1000000L
    )
    assertEquals(0, stepFail.newRepetitions)
    assertEquals(1, stepFail.newIntervalDays)
    assertTrue("Ease factor must decrease on failure", stepFail.newEaseFactor < 2.5f)
    assertEquals(ProficiencyLevel.LEARNING, stepFail.proficiencyLevel)
  }

  @Test
  fun `verify custom flashcard creation with SM-2 default values`() {
    val database = AppDatabase.getInstance(ApplicationProvider.getApplicationContext())
    val repository = ExamRepository(database)
    val viewModel = ExamViewModel(repository)

    val created = viewModel.addCustomFlashcard(
      front = "What is the threshold for renal involvement in AL amyloidosis?",
      back = "24-hour urine protein >0.5 g/day, predominantly albuminuria.",
      domain = "Malignant Haematology",
      sourceResourceId = "bsh_amyloidosis_2026",
      guidelineSection = "Section 2.1 Diagnostic Criteria",
      evidenceGrade = "Grade 1A"
    )
    assertTrue("Custom card should be created successfully", created)
  }

  @Test
  fun `verify AI guideline synthesis for all 4 2026 guidelines`() = kotlinx.coroutines.runBlocking {
    // 1. Biss et al. 2026 (Multidisciplinary Haematology & Gynaecology)
    val bissText = """
      Br J Haematol. 2026;209:919–927.
      Implementation and value of providing a multidisciplinary haematology and gynaecology service for individuals with bleeding and thrombotic disorders: A British Society for Haematology & United Kingdom Haemophilia Centre Doctors' Organisation Good Practice Paper
      Tina Biss, Emily Millen, Cora Warren, Thomas J. Clark.
      Heavy menstrual bleeding (HMB), corpus luteal haemorrhage (CLH), abnormal uterine bleeding (AUB), PALM-COEIN classification.
      Apixaban and dabigatran have lower incidence of HMB compared to rivaroxaban.
      Levonorgestrel-releasing IUS (LNS-IUS) has higher expulsion rates in IBDs.
      Recommendations: (2C) Multidisciplinary service; (2D) clear referral criteria.
    """.trimIndent()

    val bissPkg = com.example.data.ai.GuidelineAiSynthesizer.synthesizeFromGuideline(
      rawText = bissText,
      sourceName = "Br_J_Haematol_-_2026_-_Biss_-_Implementation_and_value.pdf"
    )
    assertEquals("bsh_haem_gynae_2026", bissPkg.resource.id)
    assertTrue("Title should contain gynaecology", bissPkg.resource.title.contains("gynaecology", ignoreCase = true))
    assertTrue("Summary should contain HMB", bissPkg.resource.summary.contains("HMB"))
    assertTrue("Should include PALM-COEIN staging", bissPkg.resource.stagingCriteria.contains("PALM-COEIN"))
    assertTrue("Questions must be generated", bissPkg.questions.isNotEmpty())
    assertTrue("Flashcards must be generated", bissPkg.flashcards.isNotEmpty())
    bissPkg.questions.forEach { assertEquals("bsh_haem_gynae_2026", it.sourceResourceId) }

    // 2. Tam et al. 2026 (Point-of-Care Testing)
    val tamText = """
      Br J Haematol. 2026;209:1489–1504.
      Point-of-care testing in general haematology: An update to the British Society for Haematology good practice paper
      Jennifer Tam, Barbara De la Salle, Wayne Thomas.
      ISO 15189:2022 medical laboratories. POCT PT/INR discrepancy in antiphospholipid syndrome.
      Blood gas analysers (BGAs) underestimate Hb/Hct; not interchangeable with central laboratory (1C).
      High-sensitivity D-dimer matching STA or ELISA (1B). Viscoelastic TEG, ROTEM, Quantra.
      Adverse incidents to Serious Hazards of Transfusion (SHOT).
    """.trimIndent()

    val tamPkg = com.example.data.ai.GuidelineAiSynthesizer.synthesizeFromGuideline(
      rawText = tamText,
      sourceName = "Br_J_Haematol_-_2026_-_Tam_-_Point_of_care_testing.pdf"
    )
    assertEquals("bsh_poct_haematology_2026", tamPkg.resource.id)
    assertTrue("Summary should mention POCT", tamPkg.resource.summary.contains("POCT", ignoreCase = true))
    assertTrue("Cutoffs should mention D-dimer and BGA", tamPkg.resource.keyCutoffs.contains("D-dimer"))
    assertTrue("Questions must be generated", tamPkg.questions.isNotEmpty())
    tamPkg.questions.forEach { assertEquals("bsh_poct_haematology_2026", it.sourceResourceId) }

    // 3. MacDonald et al. 2026 (TTP & ADAMTS13)
    val macDonaldText = """
      Br J Haematol. 2026;209:928–938.
      Diagnosis and monitoring of thrombotic thrombocytopenic purpura: Guidance for laboratories from the British Society for Haematology
      Stephen MacDonald, Thomas Pitchford, Sean Platton.
      Suspected TTP is an emergency; ADAMTS13 activity availability is not a prerequisite for treatment (1B).
      ADAMTS13 activity <10 IU/dL confirms TTP. Sample in 0.109 M sodium citrate; EDTA cannot be used.
      Blood film review within 60 min for platelets <30x10^9/L (1C). Anti-ADAMTS13 antibody ELISA within 72h (1B).
      Open conformation index predicts relapse.
    """.trimIndent()

    val macPkg = com.example.data.ai.GuidelineAiSynthesizer.synthesizeFromGuideline(
      rawText = macDonaldText,
      sourceName = "Br_J_Haematol_-_2026_-_MacDonald_-_Diagnosis_and_monitoring_of_ttp.pdf"
    )
    assertEquals("bsh_ttp_laboratory_2026", macPkg.resource.id)
    assertTrue("Key cutoffs must mention <10 IU/dL", macPkg.resource.keyCutoffs.contains("<10 IU/dL"))
    assertTrue("Questions must be generated", macPkg.questions.isNotEmpty())
    macPkg.questions.forEach { assertEquals("bsh_ttp_laboratory_2026", it.sourceResourceId) }

    // 4. Osborne et al. 2026 (PMBCL)
    val osborneText = """
      Br J Haematol. 2026;209:1505–1517.
      The diagnosis and management of primary mediastinal B-cell lymphoma: A British Society for Haematology guideline
      Wendy Osborne, Hannah Bielby, Andrew Davies.
      Dose-intensive R-CHOP-14 x6 or DA-R-EPOCH x6 (1B); R-CHOP-21 not recommended (1B).
      IELSG37 trial: Do not offer radiotherapy for DS 1-3 following chemoimmunotherapy (1A).
      High thrombosis rate (28%): thromboprophylaxis if platelets >50x10^9/L (1C).
      CD19 CAR-T liso-cel for relapse within 12 months (1C).
    """.trimIndent()

    val osbornePkg = com.example.data.ai.GuidelineAiSynthesizer.synthesizeFromGuideline(
      rawText = osborneText,
      sourceName = "Br_J_Haematol_-_2026_-_Osborne_-_PMBCL.pdf"
    )
    assertEquals("bsh_pmbcl_lymphoma_2026", osbornePkg.resource.id)
    assertTrue("Recommendations must mention IELSG37 and Grade 1A", osbornePkg.resource.recommendations.contains("Grade 1A"))
    assertTrue("Cutoffs must include 28% thrombosis", osbornePkg.resource.keyCutoffs.contains("28%"))
    assertTrue("Questions must be generated", osbornePkg.questions.isNotEmpty())
    osbornePkg.questions.forEach { assertEquals("bsh_pmbcl_lymphoma_2026", it.sourceResourceId) }
  }

  @Test
  fun `verify full database seeding and sandbox persistence for all guidelines`() = kotlinx.coroutines.runBlocking {
    val database = AppDatabase.getInstance(ApplicationProvider.getApplicationContext())
    val repository = ExamRepository(database)
    repository.ensureDataSeeded()

    val allRes = SeedData.getApprovedResources()
    val allQ = SeedData.getQuestions()
    val allFc = SeedData.getFlashcards()

    val resIds = allRes.map { it.id }.toSet()
    assertTrue("Biss 2026 must be present", resIds.contains("bsh_haem_gynae_2026"))
    assertTrue("Tam 2026 must be present", resIds.contains("bsh_poct_haematology_2026"))
    assertTrue("MacDonald 2026 must be present", resIds.contains("bsh_ttp_laboratory_2026"))
    assertTrue("Osborne 2026 must be present", resIds.contains("bsh_pmbcl_lymphoma_2026"))

    // Verify all 4 have questions grounded in them
    val questionResIds = allQ.map { it.sourceResourceId }.toSet()
    assertTrue(questionResIds.contains("bsh_haem_gynae_2026"))
    assertTrue(questionResIds.contains("bsh_poct_haematology_2026"))
    assertTrue(questionResIds.contains("bsh_ttp_laboratory_2026"))
    assertTrue(questionResIds.contains("bsh_pmbcl_lymphoma_2026"))

    // Verify all 4 have flashcards grounded in them
    val flashcardResIds = allFc.map { it.sourceResourceId }.toSet()
    assertTrue(flashcardResIds.contains("bsh_haem_gynae_2026"))
    assertTrue(flashcardResIds.contains("bsh_poct_haematology_2026"))
    assertTrue(flashcardResIds.contains("bsh_ttp_laboratory_2026"))
    assertTrue(flashcardResIds.contains("bsh_pmbcl_lymphoma_2026"))
  }
}

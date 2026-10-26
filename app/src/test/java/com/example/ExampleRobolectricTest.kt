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
  fun `verify AI guideline synthesis from PDF text generates grounded package`() = kotlinx.coroutines.runBlocking {
    val sampleGuidelineText = """
      British Society for Haematology Guidelines 2026
      Guidelines on the Early Diagnosis and Management of AL Amyloidosis
      Summary: AL amyloidosis is caused by deposition of misfolded monoclonal immunoglobulin light chains.
      Diagnostic cutoffs: NT-proBNP >332 ng/L, involved-uninvolved free light chain difference >50 mg/L.
      Staging: AL-ISS 2026 stages I to IIIC.
      Recommendations: Grade 1A Dara-CyBorD induction for transplant-ineligible patients.
    """.trimIndent()

    val pkg = com.example.data.ai.GuidelineAiSynthesizer.synthesizeFromGuideline(
      rawText = sampleGuidelineText,
      sourceName = "BSH_Amyloidosis_2026_Guideline.pdf"
    )

    assertNotNull("Synthesized package must not be null", pkg)
    assertTrue("Resource title must contain AL Amyloidosis", pkg.resource.title.contains("Amyloidosis", ignoreCase = true))
    assertEquals("British Society for Haematology", pkg.resource.issuingBody)
    assertTrue("Should generate at least 2 mock questions", pkg.questions.size >= 2)
    assertTrue("Should generate at least 3 flashcards", pkg.flashcards.size >= 3)

    // Check evidence grounding
    pkg.questions.forEach { q ->
      assertEquals(pkg.resource.id, q.sourceResourceId)
      assertTrue("Question stem must not be blank", q.stem.isNotBlank())
      assertTrue("Question rationale must not be blank", q.rationale.isNotBlank())
    }
    pkg.flashcards.forEach { card ->
      assertEquals(pkg.resource.id, card.sourceResourceId)
      assertEquals("SM-2 initial interval must be 1 day", 1, card.intervalDays)
      assertEquals("SM-2 initial ease factor must be 2.5", 2.5f, card.easeFactor)
    }
  }
}

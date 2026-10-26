package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.db.SeedData
import com.example.data.model.ApprovedResource
import com.example.data.model.Flashcard
import com.example.data.model.Question
import com.example.data.model.UserAnswerHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class ExamRepository(private val db: AppDatabase) {

    val allResources: Flow<List<ApprovedResource>> = db.approvedResourceDao().getAllResources()
    val allQuestions: Flow<List<Question>> = db.questionDao().getAllQuestions()
    val allFlashcards: Flow<List<Flashcard>> = db.flashcardDao().getAllFlashcards()
    val allAnswers: Flow<List<UserAnswerHistory>> = db.userAnswerDao().getAllAnswers()
    val bookmarkedQuestionIds: Flow<List<String>> = db.userAnswerDao().getBookmarkedQuestionIds()
    val incorrectQuestionIds: Flow<List<String>> = db.userAnswerDao().getIncorrectQuestionIds()

    suspend fun ensureDataSeeded() = withContext(Dispatchers.IO) {
        val existing = db.approvedResourceDao().getAllResources().firstOrNull()
        if (existing.isNullOrEmpty()) {
            AppDatabase.seedDatabase(db)
        }
    }

    suspend fun recordAnswer(
        questionId: String,
        selectedOptionIndex: Int,
        isCorrect: Boolean,
        notes: String = ""
    ) = withContext(Dispatchers.IO) {
        db.userAnswerDao().insertAnswer(
            UserAnswerHistory(
                questionId = questionId,
                selectedOptionIndex = selectedOptionIndex,
                isCorrect = isCorrect,
                timestamp = System.currentTimeMillis(),
                notes = notes,
                isBookmarked = false
            )
        )
    }

    suspend fun toggleBookmark(questionId: String, isBookmarked: Boolean) = withContext(Dispatchers.IO) {
        db.userAnswerDao().setBookmark(questionId, isBookmarked)
    }

    /**
     * SuperMemo SM-2 Spaced Repetition Scheduling
     * quality: 0 (Again), 2 (Hard), 3 (Good), 5 (Easy)
     */
    suspend fun processFlashcardReview(flashcard: Flashcard, qualityScore: Int) = withContext(Dispatchers.IO) {
        val q = qualityScore.coerceIn(0, 5)
        var newRepetition = flashcard.repetitionCount
        var newInterval: Int
        var newEase = flashcard.easeFactor

        if (q >= 3) {
            when (newRepetition) {
                0 -> newInterval = 1
                1 -> newInterval = 6
                else -> newInterval = (flashcard.intervalDays * newEase).roundToInt().coerceAtLeast(1)
            }
            newRepetition += 1
        } else {
            newRepetition = 0
            newInterval = 1
        }

        // SM-2 formula: EF' = EF + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02))
        val delta = 0.1f - (5 - q) * (0.08f + (5 - q) * 0.02f)
        newEase = (newEase + delta).coerceAtLeast(1.3f)

        val nextDue = System.currentTimeMillis() + (newInterval * 24L * 60L * 60L * 1000L)

        val updated = flashcard.copy(
            intervalDays = newInterval,
            repetitionCount = newRepetition,
            easeFactor = newEase,
            dueDateTimestamp = nextDue,
            lastReviewedTimestamp = System.currentTimeMillis()
        )
        db.flashcardDao().updateFlashcard(updated)
    }

    suspend fun updateFlashcard(flashcard: Flashcard) = withContext(Dispatchers.IO) {
        db.flashcardDao().updateFlashcard(flashcard)
    }

    /**
     * Strict Evidence Grounding Sandbox:
     * Enforces that every question and explanation must reference an approved database resource.
     */
    suspend fun validateAndInsertQuestion(question: Question): Result<Unit> = withContext(Dispatchers.IO) {
        val resource = db.approvedResourceDao().getResourceById(question.sourceResourceId)
        if (resource == null) {
            return@withContext Result.failure(
                IllegalArgumentException("Evidence Sandbox Violation: Resource '${question.sourceResourceId}' is not registered in the Approved Guidelines Database. Questions must be strictly grounded in an approved guideline.")
            )
        }
        if (question.stem.isBlank() || question.rationale.isBlank()) {
            return@withContext Result.failure(
                IllegalArgumentException("Validation Error: Question stem and evidence-based rationale cannot be empty.")
            )
        }
        db.questionDao().insertQuestion(question)
        Result.success(Unit)
    }

    suspend fun insertFlashcard(flashcard: Flashcard) = withContext(Dispatchers.IO) {
        db.flashcardDao().insertFlashcard(flashcard)
    }

    suspend fun insertFlashcards(flashcards: List<Flashcard>) = withContext(Dispatchers.IO) {
        db.flashcardDao().insertFlashcards(flashcards)
    }

    suspend fun insertQuestions(questions: List<Question>) = withContext(Dispatchers.IO) {
        db.questionDao().insertQuestions(questions)
    }

    /**
     * Resource Ingestion Pathway:
     * Adds an approved clinical guideline (e.g. BSH, IMWG, WHO, NICE, RCPath) into the local registry.
     */
    suspend fun ingestApprovedResource(resource: ApprovedResource) = withContext(Dispatchers.IO) {
        db.approvedResourceDao().insertResource(resource)
    }

    /**
     * Atomically ingest an approved guideline together with AI-generated flashcards and mock questions.
     */
    suspend fun ingestFullGuidelinePackage(
        resource: ApprovedResource,
        questions: List<Question>,
        flashcards: List<Flashcard>
    ) = withContext(Dispatchers.IO) {
        db.approvedResourceDao().insertResource(resource)
        if (questions.isNotEmpty()) {
            db.questionDao().insertQuestions(questions)
        }
        if (flashcards.isNotEmpty()) {
            db.flashcardDao().insertFlashcards(flashcards)
        }
    }
}

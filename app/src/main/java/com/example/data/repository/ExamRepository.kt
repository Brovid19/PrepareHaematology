package com.example.data.repository

import androidx.room.withTransaction
import com.example.data.db.AppDatabase
import com.example.data.db.SeedData
import com.example.data.model.ApprovedResource
import com.example.data.model.Flashcard
import com.example.data.model.Question
import com.example.data.model.UserAnswerHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ExamRepository(private val db: AppDatabase) {

    val allResources: Flow<List<ApprovedResource>> = db.approvedResourceDao().getAllResources()
    val allQuestions: Flow<List<Question>> = db.questionDao().getAllQuestions()
    val allFlashcards: Flow<List<Flashcard>> = db.flashcardDao().getAllFlashcards()
    val allAnswers: Flow<List<UserAnswerHistory>> = db.userAnswerDao().getAllAnswers()
    val bookmarkedQuestionIds: Flow<List<String>> = db.userAnswerDao().getBookmarkedQuestionIds()
    val incorrectQuestionIds: Flow<List<String>> = db.userAnswerDao().getIncorrectQuestionIds()

    /**
     * Seeds a fresh install AND pushes corrected/updated seed content to existing installs.
     *
     * Content fields (text, answers, sources) are always overwritten from [SeedData] so that
     * corrections reach learners. Learner state is preserved:
     *  - flashcard scheduling fields (interval, repetitions, ease, due, last reviewed) are kept;
     *  - answer history and bookmarks live in a separate table and are never touched;
     *  - upserts update rows in place, so foreign-key cascades never fire.
     */
    suspend fun ensureDataSeeded() = withContext(Dispatchers.IO) {
        db.withTransaction {
            db.approvedResourceDao().insertResources(SeedData.getApprovedResources())
            db.questionDao().insertQuestions(SeedData.getQuestions())
            upsertFlashcardsPreservingProgress(SeedData.getFlashcards())
        }
    }

    /**
     * Upserts flashcard content while keeping any existing spaced-repetition progress.
     */
    private suspend fun upsertFlashcardsPreservingProgress(cards: List<Flashcard>) {
        if (cards.isEmpty()) return
        val existing = db.flashcardDao().getFlashcardsByIds(cards.map { it.id }).associateBy { it.id }
        val merged = cards.map { incoming ->
            val current = existing[incoming.id]
            if (current == null) {
                incoming
            } else {
                incoming.copy(
                    intervalDays = current.intervalDays,
                    repetitionCount = current.repetitionCount,
                    easeFactor = current.easeFactor,
                    dueDateTimestamp = current.dueDateTimestamp,
                    lastReviewedTimestamp = current.lastReviewedTimestamp
                )
            }
        }
        db.flashcardDao().insertFlashcards(merged)
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
        db.withTransaction {
            db.approvedResourceDao().insertResource(resource)
            if (questions.isNotEmpty()) {
                db.questionDao().insertQuestions(questions)
            }
            upsertFlashcardsPreservingProgress(flashcards)
        }
    }
}

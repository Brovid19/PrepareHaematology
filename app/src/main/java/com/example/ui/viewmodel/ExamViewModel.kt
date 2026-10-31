package com.example.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.TtsItemType
import com.example.audio.TtsPlaybackState
import com.example.audio.TtsPlaylistItem
import com.example.audio.TtsPlayer
import com.example.audio.TtsTextFormatter
import com.example.data.ai.GeneratedGuidelinePackage
import com.example.data.ai.GuidelineAiSynthesizer
import com.example.data.db.SeedData
import com.example.data.model.ApprovedResource
import com.example.data.model.Flashcard
import com.example.data.model.Question
import com.example.data.model.UserAnswerHistory
import com.example.data.repository.AppSettings
import com.example.data.repository.ExamRepository
import com.example.data.repository.SettingsManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class Screen {
    DASHBOARD,
    PRACTICE,
    SPACED_REPETITION,
    MOCK_EXAM,
    RESOURCE_REGISTRY,
    SUMMARISED_GUIDELINES,
    WEAK_SPOTS,
    SETTINGS
}

enum class ProficiencyLevel {
    LEARNING,
    PROFICIENT,
    MASTERED
}

enum class FlashcardSessionMode {
    ALL_CARDS,
    DUE_FOR_REVIEW,
    DIFFICULT_DRILL
}

data class Sm2CalculationResult(
    val newRepetitions: Int,
    val newIntervalDays: Int,
    val newEaseFactor: Float,
    val nextDueTimestamp: Long,
    val proficiencyLevel: ProficiencyLevel
)

/**
 * SuperMemo SM-2. Quality ratings used by the UI buttons:
 *   Again = 1 (failed recall), Hard = 3 (correct with difficulty),
 *   Good = 4 (correct after some thought), Easy = 5 (instant, perfect recall).
 * Ratings < 3 reset the card; ratings >= 3 are passes (Hard is a pass, as in SM-2).
 */
fun computeSm2(
    currentRepetitions: Int,
    currentIntervalDays: Int,
    currentEaseFactor: Float,
    qualityRating: Int,
    currentTime: Long = System.currentTimeMillis()
): Sm2CalculationResult {
    val q = qualityRating.coerceIn(0, 5)
    var repetitions = currentRepetitions
    val interval: Int

    if (q >= 3) {
        interval = when (repetitions) {
            0 -> 1
            1 -> 6
            else -> (currentIntervalDays * currentEaseFactor).roundToInt().coerceAtLeast(1)
        }
        repetitions += 1
    } else {
        // Failed recall: reset repetitions and review again tomorrow
        repetitions = 0
        interval = 1
    }

    // Standard SM-2 Easiness Factor Formula:
    // EF' = EF + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02))
    val delta = 0.1f - (5 - q) * (0.08f + (5 - q) * 0.02f)
    val ease = (currentEaseFactor + delta).coerceAtLeast(1.3f)

    val nextDueTimestamp = currentTime + (interval * 86_400_000L)

    val proficiency = when {
        interval >= 21 && repetitions >= 4 && ease >= 2.3f -> ProficiencyLevel.MASTERED
        interval >= 6 && repetitions >= 2 -> ProficiencyLevel.PROFICIENT
        else -> ProficiencyLevel.LEARNING
    }

    return Sm2CalculationResult(
        newRepetitions = repetitions,
        newIntervalDays = interval,
        newEaseFactor = ease,
        nextDueTimestamp = nextDueTimestamp,
        proficiencyLevel = proficiency
    )
}

data class Sm2ReviewOutcome(
    val cardId: String,
    val rating: Int,
    val oldInterval: Int,
    val newInterval: Int,
    val oldEaseFactor: Float,
    val newEaseFactor: Float,
    val newRepetitions: Int,
    val nextDueDate: Long,
    val proficiency: ProficiencyLevel
)

data class SpacedReviewSchedule(
    val dueNowCount: Int = 0,
    val dueTomorrowCount: Int = 0,
    val dueIn2To3DaysCount: Int = 0,
    val dueThisWeekCount: Int = 0,
    val dueLaterCount: Int = 0,
    val totalMastered: Int = 0,
    val totalProficient: Int = 0,
    val totalLearning: Int = 0,
    val averageEaseFactor: Float = 2.5f,
    val estimatedRetentionRate: Int = 85,
    val nextSessionTimestamp: Long? = null
)

data class ExamUiState(
    val currentScreen: Screen = Screen.DASHBOARD,
    val selectedDomainFilter: String = "All",
    val selectedTypeFilter: String = "All",
    val practiceQuestionIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val candidateEssayNotes: String = "",
    val isEssayModelRevealed: Boolean = false,
    val isGroundingDrawerOpen: Boolean = false,
    val activeResourceDetail: ApprovedResource? = null,
    // Spaced repetition state
    val flashcardIndex: Int = 0,
    val isCardFlipped: Boolean = false,
    val selectedFlashcardResourceFilter: String = "All",
    val flashcardSessionMode: FlashcardSessionMode = FlashcardSessionMode.DUE_FOR_REVIEW,
    val lastSm2Outcome: Sm2ReviewOutcome? = null,
    val isSpacedSessionCompleted: Boolean = false,
    val reviewedInCurrentSessionCount: Int = 0,
    val spacedRepetitionMessage: String? = null,
    val isCreateCardDialogOpen: Boolean = false,
    // Mock Exam state
    val isMockActive: Boolean = false,
    val mockQuestions: List<Question> = emptyList(),
    val mockCurrentIndex: Int = 0,
    val mockAnswers: Map<String, Int> = emptyMap(), // questionId -> selectedOption
    val mockFlaggedIds: Set<String> = emptySet(),
    val mockSecondsRemaining: Int = 20 * 60, // 20 minutes default
    val isMockFinished: Boolean = false,
    val mockScorePercentage: Int = 0,
    // Resource Ingestion dialog
    val isIngestDialogOpen: Boolean = false,
    val sandboxErrorMessage: String? = null,
    val sandboxSuccessMessage: String? = null,
    // AI PDF Ingestion & Synthesis State
    val isAiAnalyzingPdf: Boolean = false,
    val isAiReviewDialogOpen: Boolean = false,
    val aiGeneratedPackage: GeneratedGuidelinePackage? = null,
    // Settings state
    val settingsSavedMessage: String? = null
)

class ExamViewModel(
    private val repository: ExamRepository,
    context: Context? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExamUiState())
    val uiState: StateFlow<ExamUiState> = _uiState.asStateFlow()

    private val settingsManager = SettingsManager(context)
    val appSettings: StateFlow<AppSettings> = settingsManager.settingsState

    private val _ttsState = MutableStateFlow(
        TtsPlaybackState(speechRate = settingsManager.settingsState.value.defaultSpeechRate)
    )
    val ttsState: StateFlow<TtsPlaybackState> = _ttsState.asStateFlow()

    private var ttsPlayer: TtsPlayer? = null

    val allResources: StateFlow<List<ApprovedResource>> = repository.allResources
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allQuestions: StateFlow<List<Question>> = repository.allQuestions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFlashcards: StateFlow<List<Flashcard>> = repository.allFlashcards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAnswers: StateFlow<List<UserAnswerHistory>> = repository.allAnswers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarkedIds: StateFlow<List<String>> = repository.bookmarkedQuestionIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val incorrectIds: StateFlow<List<String>> = repository.incorrectQuestionIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Spaced Review Schedule & Forecast derived reactively from all flashcards
    val scheduleForecast: StateFlow<SpacedReviewSchedule> = repository.allFlashcards
        .map { cards -> computeSpacedSchedule(cards) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SpacedReviewSchedule())

    private var timerJob: Job? = null

    companion object {
        const val MOCK_QUESTION_COUNT = 10
    }

    init {
        val startupSettings = settingsManager.settingsState.value
        val startupMode = runCatching { FlashcardSessionMode.valueOf(startupSettings.defaultSessionMode) }
            .getOrDefault(FlashcardSessionMode.DUE_FOR_REVIEW)
        _uiState.value = _uiState.value.copy(
            flashcardSessionMode = startupMode,
            selectedDomainFilter = startupSettings.defaultPracticeDomain
        )
        viewModelScope.launch {
            repository.ensureDataSeeded()
        }
        if (context != null) {
            ttsPlayer = TtsPlayer(
                context = context,
                onInitResult = { success ->
                    _ttsState.value = _ttsState.value.copy(isInitialized = success)
                },
                onUtteranceFinished = { utteranceId ->
                    handleUtteranceFinished(utteranceId)
                },
                onUtteranceError = { utteranceId ->
                    handleUtteranceError(utteranceId)
                }
            )
        }
    }

    fun navigateTo(screen: Screen) {
        _uiState.value = _uiState.value.copy(
            currentScreen = screen,
            isAnswerSubmitted = false,
            selectedOptionIndex = null,
            candidateEssayNotes = "",
            isEssayModelRevealed = false,
            isCardFlipped = false
        )
    }

    fun setDomainFilter(domain: String) {
        _uiState.value = _uiState.value.copy(
            selectedDomainFilter = domain,
            practiceQuestionIndex = 0,
            selectedOptionIndex = null,
            isAnswerSubmitted = false
        )
    }

    fun setTypeFilter(type: String) {
        _uiState.value = _uiState.value.copy(
            selectedTypeFilter = type,
            practiceQuestionIndex = 0,
            selectedOptionIndex = null,
            isAnswerSubmitted = false
        )
    }

    /** Opens a specific question in Practice regardless of the current filters. */
    fun openPracticeQuestion(questionId: String, allQuestions: List<Question>) {
        val index = allQuestions.indexOfFirst { it.id == questionId }.coerceAtLeast(0)
        _uiState.value = _uiState.value.copy(
            selectedDomainFilter = "All",
            selectedTypeFilter = "All",
            practiceQuestionIndex = index,
            selectedOptionIndex = null,
            isAnswerSubmitted = false,
            candidateEssayNotes = "",
            isEssayModelRevealed = false,
            currentScreen = Screen.PRACTICE
        )
    }

    fun setPracticeQuestionIndex(index: Int) {
        _uiState.value = _uiState.value.copy(
            practiceQuestionIndex = index,
            selectedOptionIndex = null,
            isAnswerSubmitted = false,
            candidateEssayNotes = "",
            isEssayModelRevealed = false
        )
    }

    fun selectOption(optionIndex: Int) {
        if (!_uiState.value.isAnswerSubmitted) {
            _uiState.value = _uiState.value.copy(selectedOptionIndex = optionIndex)
        }
    }

    fun submitAnswer(question: Question) {
        val selected = _uiState.value.selectedOptionIndex ?: return
        val isCorrect = selected == question.correctIndex
        _uiState.value = _uiState.value.copy(isAnswerSubmitted = true)

        viewModelScope.launch {
            repository.recordAnswer(
                questionId = question.id,
                selectedOptionIndex = selected,
                isCorrect = isCorrect
            )
        }
    }

    fun updateEssayNotes(notes: String) {
        _uiState.value = _uiState.value.copy(candidateEssayNotes = notes)
    }

    fun toggleEssayModelRevealed() {
        _uiState.value = _uiState.value.copy(
            isEssayModelRevealed = !_uiState.value.isEssayModelRevealed
        )
    }

    fun toggleBookmark(questionId: String, currentBookmarked: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(questionId, !currentBookmarked)
        }
    }

    fun openGroundingDrawer(resource: ApprovedResource?) {
        _uiState.value = _uiState.value.copy(
            isGroundingDrawerOpen = true,
            activeResourceDetail = resource
        )
    }

    fun closeGroundingDrawer() {
        _uiState.value = _uiState.value.copy(
            isGroundingDrawerOpen = false,
            activeResourceDetail = null
        )
    }

    // =========================================================================
    // Core Spaced Repetition (SuperMemo SM-2) Implementation in ViewModel
    // =========================================================================

    /**
     * Core SM-2 Spaced Repetition Algorithm
     *
     * Computes the subsequent repetition count, interval (days), and easiness factor (EF)
     * based on user response quality (0 to 5):
     * 0 - Blackout / Complete failure
     * 1 - Incorrect response
     * 2 - Hard / Correct response with hesitation
     * 3 - Good / Correct response with moderate effort
     * 4 - Good response with little effort
     * 5 - Easy / Perfect immediate recall
     */
    fun calculateSm2(
        currentRepetitions: Int,
        currentIntervalDays: Int,
        currentEaseFactor: Float,
        qualityRating: Int,
        currentTime: Long = System.currentTimeMillis()
    ): Sm2CalculationResult = computeSm2(
        currentRepetitions = currentRepetitions,
        currentIntervalDays = currentIntervalDays,
        currentEaseFactor = currentEaseFactor,
        qualityRating = qualityRating,
        currentTime = currentTime
    )

    fun rateFlashcard(card: Flashcard, qualityScore: Int, currentFilteredCards: List<Flashcard>) {
        val sm2Result = calculateSm2(
            currentRepetitions = card.repetitionCount,
            currentIntervalDays = card.intervalDays,
            currentEaseFactor = card.easeFactor,
            qualityRating = qualityScore
        )

        val outcome = Sm2ReviewOutcome(
            cardId = card.id,
            rating = qualityScore,
            oldInterval = card.intervalDays,
            newInterval = sm2Result.newIntervalDays,
            oldEaseFactor = card.easeFactor,
            newEaseFactor = sm2Result.newEaseFactor,
            newRepetitions = sm2Result.newRepetitions,
            nextDueDate = sm2Result.nextDueTimestamp,
            proficiency = sm2Result.proficiencyLevel
        )

        val updatedCard = card.copy(
            intervalDays = sm2Result.newIntervalDays,
            repetitionCount = sm2Result.newRepetitions,
            easeFactor = sm2Result.newEaseFactor,
            dueDateTimestamp = sm2Result.nextDueTimestamp,
            lastReviewedTimestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.updateFlashcard(updatedCard)
        }

        val nextReviewedCount = _uiState.value.reviewedInCurrentSessionCount + 1
        val isDueMode = _uiState.value.flashcardSessionMode == FlashcardSessionMode.DUE_FOR_REVIEW

        // In Due mode every rating schedules the card at least a day ahead, so it drops out of
        // the due list and the next card slides into the current index. Advancing the index as
        // well would skip a card, and comparing against the shrinking list size would end the
        // session halfway. The session is complete when the card just rated was the last one due.
        val isCompleted = isDueMode && currentFilteredCards.size <= 1

        val nextIndex = when {
            currentFilteredCards.isEmpty() -> 0
            isDueMode -> _uiState.value.flashcardIndex.coerceAtMost((currentFilteredCards.size - 2).coerceAtLeast(0))
            else -> (_uiState.value.flashcardIndex + 1) % currentFilteredCards.size
        }

        _uiState.value = _uiState.value.copy(
            flashcardIndex = nextIndex,
            isCardFlipped = false,
            lastSm2Outcome = outcome,
            reviewedInCurrentSessionCount = nextReviewedCount,
            isSpacedSessionCompleted = isCompleted
        )
    }

    fun flipCard() {
        _uiState.value = _uiState.value.copy(isCardFlipped = !_uiState.value.isCardFlipped)
    }

    fun setFlashcardResourceFilter(filter: String) {
        _uiState.value = _uiState.value.copy(
            selectedFlashcardResourceFilter = filter,
            flashcardIndex = 0,
            isCardFlipped = false,
            isSpacedSessionCompleted = false,
            reviewedInCurrentSessionCount = 0
        )
    }

    fun setFlashcardSessionMode(mode: FlashcardSessionMode) {
        _uiState.value = _uiState.value.copy(
            flashcardSessionMode = mode,
            flashcardIndex = 0,
            isCardFlipped = false,
            isSpacedSessionCompleted = false,
            reviewedInCurrentSessionCount = 0
        )
    }

    fun dismissSessionCompletion() {
        _uiState.value = _uiState.value.copy(
            isSpacedSessionCompleted = false,
            reviewedInCurrentSessionCount = 0,
            flashcardIndex = 0
        )
    }

    fun nextFlashcard(totalFilteredCards: Int) {
        if (totalFilteredCards > 0) {
            val nextIndex = (_uiState.value.flashcardIndex + 1) % totalFilteredCards
            _uiState.value = _uiState.value.copy(
                flashcardIndex = nextIndex,
                isCardFlipped = false
            )
        }
    }

    fun prevFlashcard(totalFilteredCards: Int) {
        if (totalFilteredCards > 0) {
            val prevIndex = if (_uiState.value.flashcardIndex > 0) _uiState.value.flashcardIndex - 1 else totalFilteredCards - 1
            _uiState.value = _uiState.value.copy(
                flashcardIndex = prevIndex,
                isCardFlipped = false
            )
        }
    }

    /**
     * Aggregates and forecasts upcoming spaced review sessions for the user
     */
    private fun computeSpacedSchedule(cards: List<Flashcard>): SpacedReviewSchedule {
        if (cards.isEmpty()) return SpacedReviewSchedule()

        val now = System.currentTimeMillis()
        val oneDayMs = 86_400_000L

        var dueNow = 0
        var dueTomorrow = 0
        var dueIn2To3Days = 0
        var dueThisWeek = 0
        var dueLater = 0

        var mastered = 0
        var proficient = 0
        var learning = 0
        var totalEase = 0f
        var earliestFutureDue: Long? = null

        cards.forEach { card ->
            totalEase += card.easeFactor
            val due = card.dueDateTimestamp

            if (due <= now) {
                dueNow++
            } else if (due <= now + oneDayMs) {
                dueTomorrow++
                if (earliestFutureDue == null || due < earliestFutureDue!!) earliestFutureDue = due
            } else if (due <= now + (3 * oneDayMs)) {
                dueIn2To3Days++
                if (earliestFutureDue == null || due < earliestFutureDue!!) earliestFutureDue = due
            } else if (due <= now + (7 * oneDayMs)) {
                dueThisWeek++
                if (earliestFutureDue == null || due < earliestFutureDue!!) earliestFutureDue = due
            } else {
                dueLater++
                if (earliestFutureDue == null || due < earliestFutureDue!!) earliestFutureDue = due
            }

            // Proficiency categorization
            if (card.intervalDays >= 21 && card.repetitionCount >= 4 && card.easeFactor >= 2.3f) {
                mastered++
            } else if (card.intervalDays >= 6 && card.repetitionCount >= 2) {
                proficient++
            } else {
                learning++
            }
        }

        val avgEase = totalEase / cards.size
        val retention = (((mastered * 95) + (proficient * 82) + (learning * 60)) / cards.size).coerceIn(50, 99)

        return SpacedReviewSchedule(
            dueNowCount = dueNow,
            dueTomorrowCount = dueTomorrow,
            dueIn2To3DaysCount = dueIn2To3Days,
            dueThisWeekCount = dueThisWeek,
            dueLaterCount = dueLater,
            totalMastered = mastered,
            totalProficient = proficient,
            totalLearning = learning,
            averageEaseFactor = avgEase,
            estimatedRetentionRate = retention,
            nextSessionTimestamp = earliestFutureDue
        )
    }

    // =========================================================================
    // Mock Exam Methods
    // =========================================================================

    fun startMockExam(questions: List<Question>) {
        timerJob?.cancel()
        val settings = settingsManager.settingsState.value
        // Essays (correctIndex = -1) cannot be auto-marked, so they are excluded from timed mocks.
        val markable = questions.filter { it.questionType != "ESSAY" && it.correctIndex >= 0 && it.options.isNotEmpty() }
        val ordered = if (settings.shuffleQuestions) markable.shuffled() else markable
        val examQuestions = ordered.take(MOCK_QUESTION_COUNT)
        val durationSeconds = settings.defaultMockDurationMinutes.coerceIn(1, 180) * 60
        _uiState.value = _uiState.value.copy(
            currentScreen = Screen.MOCK_EXAM,
            isMockActive = true,
            mockQuestions = examQuestions,
            mockCurrentIndex = 0,
            mockAnswers = emptyMap(),
            mockFlaggedIds = emptySet(),
            mockSecondsRemaining = durationSeconds,
            isMockFinished = false,
            mockScorePercentage = 0
        )
        startTimer()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (_uiState.value.mockSecondsRemaining > 0 && _uiState.value.isMockActive && !_uiState.value.isMockFinished) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    mockSecondsRemaining = _uiState.value.mockSecondsRemaining - 1
                )
            }
            if (_uiState.value.mockSecondsRemaining <= 0) {
                finishMockExam()
            }
        }
    }

    fun selectMockAnswer(questionId: String, optionIndex: Int) {
        val updated = _uiState.value.mockAnswers.toMutableMap()
        updated[questionId] = optionIndex
        _uiState.value = _uiState.value.copy(mockAnswers = updated)
    }

    fun toggleMockFlag(questionId: String) {
        val flags = _uiState.value.mockFlaggedIds.toMutableSet()
        if (flags.contains(questionId)) flags.remove(questionId) else flags.add(questionId)
        _uiState.value = _uiState.value.copy(mockFlaggedIds = flags)
    }

    fun setMockIndex(index: Int) {
        _uiState.value = _uiState.value.copy(mockCurrentIndex = index)
    }

    fun finishMockExam() {
        timerJob?.cancel()
        val questions = _uiState.value.mockQuestions
        var correctCount = 0
        questions.forEach { q ->
            val userPick = _uiState.value.mockAnswers[q.id]
            if (userPick != null && userPick == q.correctIndex) {
                correctCount++
            }
            // Record in history. Unanswered questions count as incorrect so they surface in Weak Spots.
            viewModelScope.launch {
                repository.recordAnswer(
                    questionId = q.id,
                    selectedOptionIndex = userPick ?: -1,
                    isCorrect = userPick != null && userPick == q.correctIndex,
                    notes = if (userPick == null) "Timed Mock Exam (unanswered)" else "Timed Mock Exam"
                )
            }
        }
        val percentage = if (questions.isNotEmpty()) (correctCount * 100) / questions.size else 0
        _uiState.value = _uiState.value.copy(
            isMockFinished = true,
            isMockActive = false,
            mockScorePercentage = percentage
        )
    }

    // =========================================================================
    // Ingestion Pathway & Grounding Sandbox Actions
    // =========================================================================

    fun openIngestDialog() {
        _uiState.value = _uiState.value.copy(
            isIngestDialogOpen = true,
            sandboxErrorMessage = null,
            sandboxSuccessMessage = null
        )
    }

    fun closeIngestDialog() {
        _uiState.value = _uiState.value.copy(
            isIngestDialogOpen = false,
            sandboxErrorMessage = null,
            sandboxSuccessMessage = null
        )
    }

    fun ingestGuideline(
        title: String,
        issuingBody: String,
        year: Int,
        citation: String,
        category: String,
        summary: String,
        keyCutoffs: String,
        stagingCriteria: String,
        recommendations: String,
        url: String = ""
    ) {
        if (title.isBlank() || citation.isBlank() || summary.isBlank()) {
            _uiState.value = _uiState.value.copy(
                sandboxErrorMessage = "Guideline title, citation, and summary are mandatory."
            )
            return
        }
        val id = "guideline_" + title.lowercase().replace(Regex("[^a-z0-9]"), "_").take(30) + "_$year"
        val resource = ApprovedResource(
            id = id,
            title = title.trim(),
            issuingBody = issuingBody.trim(),
            year = year,
            citation = citation.trim(),
            category = category,
            summary = summary.trim(),
            keyCutoffs = keyCutoffs.trim(),
            stagingCriteria = stagingCriteria.trim(),
            recommendations = recommendations.trim(),
            url = url.trim(),
            // Entered by the user, not checked by an editor.
            isVerified = false
        )
        viewModelScope.launch {
            repository.ingestApprovedResource(resource)
            _uiState.value = _uiState.value.copy(
                isIngestDialogOpen = false,
                sandboxSuccessMessage = "Successfully ingested '${resource.title}' into the Approved Guidelines Registry!"
            )
        }
    }

    fun dismissMessage() {
        _uiState.value = _uiState.value.copy(
            sandboxErrorMessage = null,
            sandboxSuccessMessage = null
        )
    }

    // =========================================================================
    // Custom Flashcard Creation Pathway (Spaced Repetition)
    // =========================================================================

    fun openCreateCardDialog() {
        _uiState.value = _uiState.value.copy(
            isCreateCardDialogOpen = true,
            spacedRepetitionMessage = null
        )
    }

    fun closeCreateCardDialog() {
        _uiState.value = _uiState.value.copy(
            isCreateCardDialogOpen = false
        )
    }

    fun dismissSpacedRepetitionMessage() {
        _uiState.value = _uiState.value.copy(
            spacedRepetitionMessage = null
        )
    }

    fun addCustomFlashcard(
        front: String,
        back: String,
        domain: String,
        sourceResourceId: String,
        guidelineSection: String,
        evidenceGrade: String
    ): Boolean {
        if (front.isBlank() || back.isBlank()) {
            _uiState.value = _uiState.value.copy(
                spacedRepetitionMessage = "Both front prompt and back answer must be provided."
            )
            return false
        }
        val now = System.currentTimeMillis()
        val cardId = "custom_card_${now}_${(100..999).random()}"
        val card = Flashcard(
            id = cardId,
            domain = domain,
            topic = guidelineSection.trim().ifBlank { "Candidate Custom Notes" },
            sourceResourceId = sourceResourceId.ifBlank { allResources.value.firstOrNull()?.id ?: SeedData.getApprovedResources().first().id },
            front = front.trim(),
            back = back.trim(),
            guidelineRef = "${guidelineSection.trim().ifBlank { "Candidate Notes" }} ($evidenceGrade)",
            intervalDays = 1,
            repetitionCount = 0,
            easeFactor = 2.5f,
            dueDateTimestamp = now,
            lastReviewedTimestamp = 0L
        )

        viewModelScope.launch {
            repository.insertFlashcard(card)
            _uiState.value = _uiState.value.copy(
                isCreateCardDialogOpen = false,
                spacedRepetitionMessage = "Custom flashcard added to deck with SM-2 scheduling!"
            )
        }
        return true
    }

    // =========================================================================
    // PDF Upload & AI Guideline Synthesis Pathway
    // =========================================================================

    fun analyzeGuidelinePdf(context: Context, uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isAiAnalyzingPdf = true,
                sandboxErrorMessage = null
            )
            try {
                val extractedText = GuidelineAiSynthesizer.extractTextFromUri(context, uri)
                val fileName = uri.lastPathSegment ?: "Clinical Guideline PDF"
                val generatedPackage = GuidelineAiSynthesizer.synthesizeFromGuideline(extractedText, fileName)
                _uiState.value = _uiState.value.copy(
                    isAiAnalyzingPdf = false,
                    isAiReviewDialogOpen = true,
                    aiGeneratedPackage = generatedPackage
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isAiAnalyzingPdf = false,
                    sandboxErrorMessage = "Failed to analyze PDF document: ${e.localizedMessage ?: "Unknown parsing error"}"
                )
            }
        }
    }

    fun analyzeSampleGuideline(title: String, rawContent: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isAiAnalyzingPdf = true,
                sandboxErrorMessage = null
            )
            try {
                val generatedPackage = GuidelineAiSynthesizer.synthesizeFromGuideline(rawContent, title)
                _uiState.value = _uiState.value.copy(
                    isAiAnalyzingPdf = false,
                    isAiReviewDialogOpen = true,
                    aiGeneratedPackage = generatedPackage
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isAiAnalyzingPdf = false,
                    sandboxErrorMessage = "Failed to synthesize guideline: ${e.localizedMessage}"
                )
            }
        }
    }

    fun updateAiGeneratedPackage(updatedPackage: GeneratedGuidelinePackage) {
        _uiState.value = _uiState.value.copy(aiGeneratedPackage = updatedPackage)
    }

    fun dismissAiReviewDialog() {
        _uiState.value = _uiState.value.copy(
            isAiReviewDialogOpen = false,
            aiGeneratedPackage = null
        )
    }

    fun commitAiGeneratedPackage() {
        val pkg = _uiState.value.aiGeneratedPackage ?: return
        viewModelScope.launch {
            repository.ingestFullGuidelinePackage(
                resource = pkg.resource,
                questions = pkg.questions,
                flashcards = pkg.flashcards
            )
            _uiState.value = _uiState.value.copy(
                isAiReviewDialogOpen = false,
                aiGeneratedPackage = null,
                isIngestDialogOpen = false,
                sandboxSuccessMessage = "Successfully ingested '${pkg.resource.title}' along with ${pkg.flashcards.size} SM-2 flashcards and ${pkg.questions.size} mock exam questions into the database sandbox!"
            )
        }
    }

    // ==========================================
    // TEXT-TO-SPEECH (TTS) & AUDIO PLAYLIST
    // ==========================================

    private fun handleUtteranceFinished(utteranceId: String) {
        viewModelScope.launch {
            val state = _ttsState.value
            if (state.isPlaying && state.playlist.isNotEmpty()) {
                val nextIndex = state.currentItemIndex + 1
                if (nextIndex < state.playlist.size) {
                    delay(650) // Natural breathing pause between playlist items
                    playPlaylistItem(nextIndex)
                } else {
                    _ttsState.value = state.copy(
                        isPlaying = false,
                        isPaused = false,
                        currentSpeakingItem = null,
                        statusMessage = "Audio playlist completed"
                    )
                }
            } else {
                _ttsState.value = state.copy(
                    isPlaying = false,
                    isPaused = false,
                    currentSpeakingItem = null
                )
            }
        }
    }

    private fun handleUtteranceError(utteranceId: String) {
        _ttsState.value = _ttsState.value.copy(
            isPlaying = false,
            isPaused = false,
            currentSpeakingItem = null,
            statusMessage = "Audio playback interrupted"
        )
    }

    fun speakSingleSummary(resource: ApprovedResource) {
        val text = TtsTextFormatter.formatSummaryForSpeech(resource)
        val item = TtsPlaylistItem(
            id = "summary_${resource.id}",
            type = TtsItemType.SUMMARY,
            title = resource.title,
            subtitle = "${resource.issuingBody} (${resource.year})",
            spokenText = text,
            referenceId = resource.id
        )
        playDirectItem(item)
    }

    fun addSummaryToPlaylist(resource: ApprovedResource) {
        val text = TtsTextFormatter.formatSummaryForSpeech(resource)
        val item = TtsPlaylistItem(
            id = "summary_${resource.id}",
            type = TtsItemType.SUMMARY,
            title = resource.title,
            subtitle = "${resource.issuingBody} (${resource.year})",
            spokenText = text,
            referenceId = resource.id
        )
        addToPlaylistInternal(item)
    }

    fun addAllSummariesToPlaylist(resources: List<ApprovedResource>) {
        val items = resources.map { res ->
            TtsPlaylistItem(
                id = "summary_${res.id}",
                type = TtsItemType.SUMMARY,
                title = res.title,
                subtitle = "${res.issuingBody} (${res.year})",
                spokenText = TtsTextFormatter.formatSummaryForSpeech(res),
                referenceId = res.id
            )
        }
        val currentList = _ttsState.value.playlist.toMutableList()
        var addedCount = 0
        items.forEach { item ->
            if (currentList.none { it.id == item.id }) {
                currentList.add(item)
                addedCount++
            }
        }
        _ttsState.value = _ttsState.value.copy(
            playlist = currentList,
            statusMessage = "Added $addedCount guideline summaries to audio playlist (${currentList.size} total in queue)"
        )
    }

    fun speakSingleFlashcard(card: Flashcard, resource: ApprovedResource?) {
        val text = TtsTextFormatter.formatFlashcardForSpeech(card, resource)
        val item = TtsPlaylistItem(
            id = "card_${card.id}",
            type = TtsItemType.FLASHCARD,
            title = card.front,
            subtitle = "${card.domain} • ${card.guidelineRef}",
            spokenText = text,
            referenceId = card.id
        )
        playDirectItem(item)
    }

    fun addFlashcardToPlaylist(card: Flashcard, resource: ApprovedResource?) {
        val text = TtsTextFormatter.formatFlashcardForSpeech(card, resource)
        val item = TtsPlaylistItem(
            id = "card_${card.id}",
            type = TtsItemType.FLASHCARD,
            title = card.front,
            subtitle = "${card.domain} • ${card.guidelineRef}",
            spokenText = text,
            referenceId = card.id
        )
        addToPlaylistInternal(item)
    }

    fun addAllDueFlashcardsToPlaylist(cards: List<Flashcard>, resources: List<ApprovedResource>) {
        val now = System.currentTimeMillis()
        val dueCards = cards.filter { it.dueDateTimestamp <= now }
        if (dueCards.isEmpty()) {
            _ttsState.value = _ttsState.value.copy(statusMessage = "No cards currently due for review")
            return
        }
        val resMap = resources.associateBy { it.id }
        val items = dueCards.map { card ->
            TtsPlaylistItem(
                id = "card_${card.id}",
                type = TtsItemType.FLASHCARD,
                title = card.front,
                subtitle = "${card.domain} • Due (${card.intervalDays}d)",
                spokenText = TtsTextFormatter.formatFlashcardForSpeech(card, resMap[card.sourceResourceId]),
                referenceId = card.id
            )
        }
        val currentList = _ttsState.value.playlist.toMutableList()
        var addedCount = 0
        items.forEach { item ->
            if (currentList.none { it.id == item.id }) {
                currentList.add(item)
                addedCount++
            }
        }
        _ttsState.value = _ttsState.value.copy(
            playlist = currentList,
            statusMessage = "Queued $addedCount due flashcards to audio playlist (${currentList.size} total in queue)"
        )
    }

    fun addAllFlashcardsToPlaylist(cards: List<Flashcard>, resources: List<ApprovedResource>) {
        val resMap = resources.associateBy { it.id }
        val items = cards.map { card ->
            TtsPlaylistItem(
                id = "card_${card.id}",
                type = TtsItemType.FLASHCARD,
                title = card.front,
                subtitle = "${card.domain} • ${card.guidelineRef}",
                spokenText = TtsTextFormatter.formatFlashcardForSpeech(card, resMap[card.sourceResourceId]),
                referenceId = card.id
            )
        }
        val currentList = _ttsState.value.playlist.toMutableList()
        var addedCount = 0
        items.forEach { item ->
            if (currentList.none { it.id == item.id }) {
                currentList.add(item)
                addedCount++
            }
        }
        _ttsState.value = _ttsState.value.copy(
            playlist = currentList,
            statusMessage = "Queued $addedCount flashcards to audio playlist (${currentList.size} total in queue)"
        )
    }

    private fun addToPlaylistInternal(item: TtsPlaylistItem) {
        val currentList = _ttsState.value.playlist.toMutableList()
        val existingIndex = currentList.indexOfFirst { it.id == item.id }
        if (existingIndex >= 0) {
            _ttsState.value = _ttsState.value.copy(
                statusMessage = "'${item.title.take(28)}...' is already in the audio playlist"
            )
        } else {
            currentList.add(item)
            _ttsState.value = _ttsState.value.copy(
                playlist = currentList,
                statusMessage = "Added to audio playlist (${currentList.size} items queued)"
            )
        }
    }

    private fun playDirectItem(item: TtsPlaylistItem) {
        ttsPlayer?.stop()
        _ttsState.value = _ttsState.value.copy(
            isPlaying = true,
            isPaused = false,
            currentSpeakingItem = item,
            statusMessage = "Playing: ${item.title.take(35)}..."
        )
        ttsPlayer?.speak(item.spokenText, item.id)
    }

    fun playTtsPlaylist(startIndex: Int? = null) {
        val state = _ttsState.value
        if (state.playlist.isEmpty()) {
            _ttsState.value = state.copy(statusMessage = "Audio playlist is empty. Add summaries or flashcards to play.")
            return
        }
        val indexToPlay = startIndex ?: state.currentItemIndex.coerceIn(0, state.playlist.size - 1)
        playPlaylistItem(indexToPlay)
    }

    private fun playPlaylistItem(index: Int) {
        val playlist = _ttsState.value.playlist
        if (index !in playlist.indices) return
        val item = playlist[index]
        ttsPlayer?.stop()
        _ttsState.value = _ttsState.value.copy(
            currentItemIndex = index,
            isPlaying = true,
            isPaused = false,
            currentSpeakingItem = item,
            statusMessage = "Playing (${index + 1}/${playlist.size}): ${item.title.take(30)}..."
        )
        ttsPlayer?.speak(item.spokenText, item.id)
    }

    fun pauseTtsPlaylist() {
        ttsPlayer?.stop()
        _ttsState.value = _ttsState.value.copy(
            isPlaying = false,
            isPaused = true,
            statusMessage = "Playback paused"
        )
    }

    fun resumeTtsPlaylist() {
        if (_ttsState.value.playlist.isNotEmpty()) {
            playPlaylistItem(_ttsState.value.currentItemIndex)
        } else if (_ttsState.value.currentSpeakingItem != null) {
            playDirectItem(_ttsState.value.currentSpeakingItem!!)
        }
    }

    fun stopTts() {
        ttsPlayer?.stop()
        _ttsState.value = _ttsState.value.copy(
            isPlaying = false,
            isPaused = false,
            currentSpeakingItem = null,
            statusMessage = null
        )
    }

    fun skipTtsNext() {
        val state = _ttsState.value
        if (state.playlist.isEmpty()) return
        val nextIndex = (state.currentItemIndex + 1) % state.playlist.size
        playPlaylistItem(nextIndex)
    }

    fun skipTtsPrevious() {
        val state = _ttsState.value
        if (state.playlist.isEmpty()) return
        val prevIndex = if (state.currentItemIndex - 1 < 0) state.playlist.size - 1 else state.currentItemIndex - 1
        playPlaylistItem(prevIndex)
    }

    fun removeTtsItem(index: Int) {
        val currentList = _ttsState.value.playlist.toMutableList()
        if (index in currentList.indices) {
            val isCurrent = index == _ttsState.value.currentItemIndex
            currentList.removeAt(index)
            var newIndex = _ttsState.value.currentItemIndex
            if (newIndex >= currentList.size) {
                newIndex = (currentList.size - 1).coerceAtLeast(0)
            }
            if (isCurrent && _ttsState.value.isPlaying) {
                if (currentList.isNotEmpty()) {
                    _ttsState.value = _ttsState.value.copy(playlist = currentList, currentItemIndex = newIndex)
                    playPlaylistItem(newIndex)
                } else {
                    stopTts()
                    _ttsState.value = _ttsState.value.copy(playlist = emptyList())
                }
            } else {
                _ttsState.value = _ttsState.value.copy(playlist = currentList, currentItemIndex = newIndex)
            }
        }
    }

    fun clearTtsPlaylist() {
        stopTts()
        _ttsState.value = _ttsState.value.copy(
            playlist = emptyList(),
            currentItemIndex = 0,
            statusMessage = "Audio playlist cleared"
        )
    }

    fun setTtsSpeechRate(rate: Float) {
        ttsPlayer?.setSpeechRate(rate)
        _ttsState.value = _ttsState.value.copy(speechRate = rate)
    }

    fun toggleTtsPlaylistSheet() {
        _ttsState.value = _ttsState.value.copy(isPlaylistSheetOpen = !_ttsState.value.isPlaylistSheetOpen)
    }

    fun closeTtsPlaylistSheet() {
        _ttsState.value = _ttsState.value.copy(isPlaylistSheetOpen = false)
    }

    fun dismissTtsMessage() {
        _ttsState.value = _ttsState.value.copy(statusMessage = null)
    }

    // ==========================================
    // SETTINGS MANAGEMENT
    // ==========================================

    fun updateSettings(newSettings: AppSettings) {
        settingsManager.saveSettings(newSettings)
        setTtsSpeechRate(newSettings.defaultSpeechRate)
        _uiState.value = _uiState.value.copy(
            settingsSavedMessage = "Default settings updated and saved successfully!"
        )
    }

    fun resetSettingsToDefaults() {
        val defaults = settingsManager.resetToDefaults()
        setTtsSpeechRate(defaults.defaultSpeechRate)
        _uiState.value = _uiState.value.copy(
            settingsSavedMessage = "All preferences reset to factory defaults."
        )
    }

    fun resetSpacedRepetitionProgress() {
        viewModelScope.launch {
            val cards = allFlashcards.value
            val defaultEase = settingsManager.settingsState.value.defaultEaseFactor
            cards.forEach { card ->
                val resetCard = card.copy(
                    intervalDays = 1,
                    repetitionCount = 0,
                    easeFactor = defaultEase,
                    dueDateTimestamp = System.currentTimeMillis()
                )
                repository.updateFlashcard(resetCard)
            }
            _uiState.value = _uiState.value.copy(
                settingsSavedMessage = "Spaced repetition intervals and proficiency reset for all ${cards.size} flashcards."
            )
        }
    }

    fun dismissSettingsMessage() {
        _uiState.value = _uiState.value.copy(settingsSavedMessage = null)
    }

    override fun onCleared() {
        super.onCleared()
        ttsPlayer?.shutdown()
    }
}


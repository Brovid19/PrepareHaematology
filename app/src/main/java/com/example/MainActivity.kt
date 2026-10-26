package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.db.AppDatabase
import com.example.data.db.SeedData
import com.example.data.model.Question
import com.example.data.repository.ExamRepository
import com.example.ui.components.GroundingSourceModal
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.MockExamScreen
import com.example.ui.screens.PracticeScreen
import com.example.ui.screens.ResourceRegistryScreen
import com.example.ui.screens.SpacedRepetitionScreen
import com.example.ui.screens.SummarisedGuidelinesScreen
import com.example.ui.screens.WeakSpotsScreen
import com.example.ui.theme.CardBorderStroke
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.DeepNavyBg
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SlateNavySurface
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.ExamUiState
import com.example.ui.viewmodel.ExamViewModel
import com.example.ui.viewmodel.ExamViewModelFactory
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = androidx.activity.SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = androidx.activity.SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        val database = AppDatabase.getInstance(applicationContext)
        val repository = ExamRepository(database)
        val viewModelFactory = ExamViewModelFactory(repository)

        setContent {
            MyApplicationTheme {
                val examViewModel: ExamViewModel = viewModel(factory = viewModelFactory)
                MainAppScreen(viewModel = examViewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: ExamViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resources by viewModel.allResources.collectAsStateWithLifecycle()
    val questions by viewModel.allQuestions.collectAsStateWithLifecycle()
    val flashcards by viewModel.allFlashcards.collectAsStateWithLifecycle()
    val answers by viewModel.allAnswers.collectAsStateWithLifecycle()
    val bookmarkedIds by viewModel.bookmarkedIds.collectAsStateWithLifecycle()
    val incorrectIds by viewModel.incorrectIds.collectAsStateWithLifecycle()
    val scheduleForecast by viewModel.scheduleForecast.collectAsStateWithLifecycle()

    // Android System BackHandler to safely pop to Dashboard
    if (uiState.currentScreen != Screen.DASHBOARD) {
        BackHandler {
            viewModel.navigateTo(Screen.DASHBOARD)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DeepNavyBg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (!uiState.isMockActive || uiState.isMockFinished) {
                PrepareBottomNavigationBar(
                    currentScreen = uiState.currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentScreen) {
                Screen.DASHBOARD -> {
                    DashboardScreen(
                        questions = questions,
                        flashcards = flashcards,
                        resources = resources,
                        answers = answers,
                        incorrectQuestionIds = incorrectIds,
                        onNavigate = { viewModel.navigateTo(it) },
                        onStartMockExam = { viewModel.startMockExam(questions) },
                        onOpenResourceModal = { viewModel.openGroundingDrawer(it) }
                    )
                }

                Screen.SUMMARISED_GUIDELINES -> {
                    SummarisedGuidelinesScreen(
                        resources = resources,
                        questions = questions,
                        flashcards = flashcards,
                        onPracticeGuideline = { resId ->
                            val matchIndex = questions.indexOfFirst { it.sourceResourceId == resId }
                            if (matchIndex >= 0) {
                                viewModel.setDomainFilter("All")
                                viewModel.setTypeFilter("All")
                                viewModel.setPracticeQuestionIndex(matchIndex)
                            }
                            viewModel.navigateTo(Screen.PRACTICE)
                        },
                        onRecallGuideline = { resId ->
                            val filterName = when (resId) {
                                "bsh_amyloidosis_2026" -> "BSH 2026 Amyloidosis"
                                "imwg_myeloma_2021" -> "IMWG Myeloma"
                                "who_haem_5th" -> "WHO Classification"
                                "bsh_transfusion_components" -> "Transfusion & Safety"
                                "ukhcdo_haemostasis_2022" -> "Haemostasis & HIT"
                                else -> "All"
                            }
                            viewModel.setFlashcardResourceFilter(filterName)
                            viewModel.navigateTo(Screen.SPACED_REPETITION)
                        },
                        onBackToDashboard = { viewModel.navigateTo(Screen.DASHBOARD) }
                    )
                }

                Screen.PRACTICE -> {
                    PracticeScreen(
                        questions = questions,
                        resources = resources,
                        currentIndex = uiState.practiceQuestionIndex,
                        selectedOption = uiState.selectedOptionIndex,
                        isSubmitted = uiState.isAnswerSubmitted,
                        candidateNotes = uiState.candidateEssayNotes,
                        isModelRevealed = uiState.isEssayModelRevealed,
                        bookmarkedIds = bookmarkedIds,
                        selectedDomain = uiState.selectedDomainFilter,
                        selectedType = uiState.selectedTypeFilter,
                        onDomainSelected = { viewModel.setDomainFilter(it) },
                        onTypeSelected = { viewModel.setTypeFilter(it) },
                        onQuestionIndexChanged = { viewModel.setPracticeQuestionIndex(it) },
                        onOptionSelected = { viewModel.selectOption(it) },
                        onSubmitAnswer = { viewModel.submitAnswer(it) },
                        onUpdateNotes = { viewModel.updateEssayNotes(it) },
                        onToggleModelRevealed = { viewModel.toggleEssayModelRevealed() },
                        onToggleBookmark = { id, current -> viewModel.toggleBookmark(id, current) },
                        onOpenResourceModal = { viewModel.openGroundingDrawer(it) },
                        onBackToDashboard = { viewModel.navigateTo(Screen.DASHBOARD) }
                    )
                }

                Screen.SPACED_REPETITION -> {
                    SpacedRepetitionScreen(
                        flashcards = flashcards,
                        resources = resources,
                        currentIndex = uiState.flashcardIndex,
                        isFlipped = uiState.isCardFlipped,
                        selectedFilter = uiState.selectedFlashcardResourceFilter,
                        sessionMode = uiState.flashcardSessionMode,
                        scheduleForecast = scheduleForecast,
                        lastSm2Outcome = uiState.lastSm2Outcome,
                        isSessionCompleted = uiState.isSpacedSessionCompleted,
                        reviewedInSessionCount = uiState.reviewedInCurrentSessionCount,
                        isCreateDialogOpen = uiState.isCreateCardDialogOpen,
                        statusMessage = uiState.spacedRepetitionMessage,
                        onFilterChanged = { viewModel.setFlashcardResourceFilter(it) },
                        onSessionModeChanged = { viewModel.setFlashcardSessionMode(it) },
                        onFlipCard = { viewModel.flipCard() },
                        onRateCard = { card, rating, list -> viewModel.rateFlashcard(card, rating, list) },
                        onNextCard = { viewModel.nextFlashcard(it) },
                        onPrevCard = { viewModel.prevFlashcard(it) },
                        onOpenCreateDialog = { viewModel.openCreateCardDialog() },
                        onCloseCreateDialog = { viewModel.closeCreateCardDialog() },
                        onDismissMessage = { viewModel.dismissSpacedRepetitionMessage() },
                        onCreateCustomCard = { f, b, d, r, s, e -> viewModel.addCustomFlashcard(f, b, d, r, s, e) },
                        onDismissSessionCompleted = { viewModel.dismissSessionCompletion() },
                        onOpenResourceModal = { viewModel.openGroundingDrawer(it) },
                        onBackToDashboard = { viewModel.navigateTo(Screen.DASHBOARD) }
                    )
                }

                Screen.MOCK_EXAM -> {
                    MockExamScreen(
                        questions = uiState.mockQuestions,
                        resources = resources,
                        currentIndex = uiState.mockCurrentIndex,
                        userAnswers = uiState.mockAnswers,
                        flaggedIds = uiState.mockFlaggedIds,
                        secondsRemaining = uiState.mockSecondsRemaining,
                        isFinished = uiState.isMockFinished,
                        scorePercentage = uiState.mockScorePercentage,
                        onSelectAnswer = { qId, opt -> viewModel.selectMockAnswer(qId, opt) },
                        onToggleFlag = { viewModel.toggleMockFlag(it) },
                        onSelectIndex = { viewModel.setMockIndex(it) },
                        onFinishExam = { viewModel.finishMockExam() },
                        onRestartExam = { viewModel.startMockExam(questions) },
                        onOpenResourceModal = { viewModel.openGroundingDrawer(it) },
                        onBackToDashboard = { viewModel.navigateTo(Screen.DASHBOARD) }
                    )
                }

                Screen.RESOURCE_REGISTRY -> {
                    ResourceRegistryScreen(
                        resources = resources,
                        isIngestDialogOpen = uiState.isIngestDialogOpen,
                        errorMessage = uiState.sandboxErrorMessage,
                        successMessage = uiState.sandboxSuccessMessage,
                        isAiAnalyzingPdf = uiState.isAiAnalyzingPdf,
                        isAiReviewDialogOpen = uiState.isAiReviewDialogOpen,
                        aiGeneratedPackage = uiState.aiGeneratedPackage,
                        onOpenIngestDialog = { viewModel.openIngestDialog() },
                        onCloseIngestDialog = { viewModel.closeIngestDialog() },
                        onIngestGuideline = { title, body, year, cite, cat, sum, cut, stag, rec, url ->
                            viewModel.ingestGuideline(title, body, year, cite, cat, sum, cut, stag, rec, url)
                        },
                        onUploadPdf = { uri -> viewModel.analyzeGuidelinePdf(context, uri) },
                        onLoadSamplePdf = {
                            val firstRes = resources.firstOrNull() ?: SeedData.getApprovedResources().first()
                            viewModel.analyzeSampleGuideline(
                                "BSH Guidelines on the Early Diagnosis and Management of AL Amyloidosis",
                                firstRes.summary + "\n" + firstRes.keyCutoffs + "\n" + firstRes.stagingCriteria + "\n" + firstRes.recommendations
                            )
                        },
                        onUpdateAiPackage = { pkg -> viewModel.updateAiGeneratedPackage(pkg) },
                        onCommitAiPackage = { viewModel.commitAiGeneratedPackage() },
                        onDismissAiReview = { viewModel.dismissAiReviewDialog() },
                        onOpenResourceModal = { viewModel.openGroundingDrawer(it) },
                        onDismissMessage = { viewModel.dismissMessage() },
                        onBackToDashboard = { viewModel.navigateTo(Screen.DASHBOARD) }
                    )
                }

                Screen.WEAK_SPOTS -> {
                    WeakSpotsScreen(
                        incorrectQuestionIds = incorrectIds,
                        allQuestions = questions,
                        resources = resources,
                        onPracticeQuestion = { q ->
                            val targetIndex = questions.indexOf(q).coerceAtLeast(0)
                            viewModel.setPracticeQuestionIndex(targetIndex)
                            viewModel.navigateTo(Screen.PRACTICE)
                        },
                        onOpenResourceModal = { viewModel.openGroundingDrawer(it) },
                        onBackToDashboard = { viewModel.navigateTo(Screen.DASHBOARD) }
                    )
                }
            }

            // Evidence Grounding Bottom Sheet Modal
            if (uiState.isGroundingDrawerOpen) {
                val activeQ = if (uiState.currentScreen == Screen.PRACTICE) {
                    val filtered = questions.filter { q ->
                        (uiState.selectedDomainFilter == "All" || q.domain == uiState.selectedDomainFilter) &&
                        (uiState.selectedTypeFilter == "All" || q.questionType == uiState.selectedTypeFilter)
                    }
                    filtered.getOrNull(uiState.practiceQuestionIndex)
                } else null

                GroundingSourceModal(
                    resource = uiState.activeResourceDetail,
                    activeQuestion = activeQ,
                    onDismiss = { viewModel.closeGroundingDrawer() }
                )
            }
        }
    }
}

@Composable
fun PrepareBottomNavigationBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = SlateNavySurface,
        contentColor = TextPrimary,
        tonalElevation = 8.dp,
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("prepare_bottom_navigation")
    ) {
        NavigationBarItem(
            selected = currentScreen == Screen.DASHBOARD,
            onClick = { onNavigate(Screen.DASHBOARD) },
            icon = { Icon(imageVector = Icons.Default.Dashboard, contentDescription = "Dashboard", modifier = Modifier.size(20.dp)) },
            label = { Text("Hub", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
            colors = navigationBarColors()
        )
        NavigationBarItem(
            selected = currentScreen == Screen.SUMMARISED_GUIDELINES,
            onClick = { onNavigate(Screen.SUMMARISED_GUIDELINES) },
            icon = { Icon(imageVector = Icons.Default.MenuBook, contentDescription = "Summarised Guidelines", modifier = Modifier.size(20.dp)) },
            label = { Text("Summaries", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
            colors = navigationBarColors()
        )
        NavigationBarItem(
            selected = currentScreen == Screen.PRACTICE,
            onClick = { onNavigate(Screen.PRACTICE) },
            icon = { Icon(imageVector = Icons.Default.Quiz, contentDescription = "Practice Bank", modifier = Modifier.size(20.dp)) },
            label = { Text("Practice", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
            colors = navigationBarColors()
        )
        NavigationBarItem(
            selected = currentScreen == Screen.SPACED_REPETITION,
            onClick = { onNavigate(Screen.SPACED_REPETITION) },
            icon = { Icon(imageVector = Icons.Default.Psychology, contentDescription = "Spaced Recall", modifier = Modifier.size(20.dp)) },
            label = { Text("Recall", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
            colors = navigationBarColors()
        )
        NavigationBarItem(
            selected = currentScreen == Screen.RESOURCE_REGISTRY,
            onClick = { onNavigate(Screen.RESOURCE_REGISTRY) },
            icon = { Icon(imageVector = Icons.Default.Shield, contentDescription = "Guidelines Sandbox", modifier = Modifier.size(20.dp)) },
            label = { Text("Sandbox", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
            colors = navigationBarColors()
        )
    }
}

@Composable
fun navigationBarColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = DeepNavyBg,
    unselectedIconColor = TextSecondary,
    selectedTextColor = CyanPrimary,
    unselectedTextColor = TextMuted,
    indicatorColor = CyanPrimary
)

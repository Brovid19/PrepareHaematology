package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ApprovedResource
import com.example.data.model.Flashcard
import com.example.data.model.Question
import com.example.data.model.UserAnswerHistory
import com.example.ui.components.CircularMasteryRing
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CardBorderStroke
import com.example.ui.theme.CardElevatedBg
import com.example.ui.theme.CrimsonError
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.DeepNavyBg
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.SlateNavySurface
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.Screen

@Composable
fun DashboardScreen(
    questions: List<Question>,
    flashcards: List<Flashcard>,
    resources: List<ApprovedResource>,
    answers: List<UserAnswerHistory>,
    incorrectQuestionIds: List<String>,
    onNavigate: (Screen) -> Unit,
    onStartMockExam: (List<Question>) -> Unit,
    onOpenResourceModal: (ApprovedResource) -> Unit
) {
    val totalQuestions = questions.size
    val answeredCount = answers.map { it.questionId }.distinct().size
    val correctCount = answers.filter { it.isCorrect }.map { it.questionId }.distinct().size
    val accuracy = if (answeredCount > 0) ((correctCount.toFloat() / answeredCount) * 100).toInt() else 0
    val readinessPercentage = if (totalQuestions > 0) ((correctCount.toFloat() / totalQuestions) * 100).coerceIn(0f, 100f) else 0f

    val currentTime = System.currentTimeMillis()
    val dueFlashcardsCount = flashcards.count { it.dueDateTimestamp <= currentTime }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyBg)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Platform Header (Prepare Haematology)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(CyanPrimary, TealAccent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = DeepNavyBg,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "PREPARE HAEMATOLOGY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanPrimary,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(EmeraldSuccess.copy(alpha = 0.2f))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "FRCPath Part 1",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldSuccess
                        )
                    }
                }
                Text(
                    text = "Exam Revision & Recall Hub",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            IconButton(
                onClick = { onNavigate(Screen.SETTINGS) },
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SlateNavySurface)
                    .border(1.dp, CardBorderStroke, RoundedCornerShape(8.dp))
                    .testTag("dashboard_settings_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Preferences and Defaults",
                    tint = TextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Readiness & Stats Card
        Card(
            colors = CardDefaults.cardColors(containerColor = SlateNavySurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CardBorderStroke, RoundedCornerShape(16.dp))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                CircularMasteryRing(
                    percentage = readinessPercentage,
                    title = "Readiness"
                )
                Spacer(modifier = Modifier.width(18.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Curriculum Progress",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    StatRow(label = "Answered", value = "$answeredCount / $totalQuestions")
                    StatRow(label = "Accuracy", value = "$accuracy%")
                    StatRow(label = "Cards Due Today", value = "$dueFlashcardsCount due", valueColor = if (dueFlashcardsCount > 0) AmberWarning else EmeraldSuccess)
                    StatRow(label = "Approved Guidelines", value = "${resources.size} Active", valueColor = CyanPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Quick Launch Actions Header
        Text(
            text = "REVISION MODES",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Summarised Guidelines Feature Card
        Card(
            colors = CardDefaults.cardColors(containerColor = SlateNavySurface),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, TealAccent.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                .clickable { onNavigate(Screen.SUMMARISED_GUIDELINES) }
                .testTag("launch_summarised_guidelines")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(TealAccent.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = TealAccent,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Summarised Guidelines",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CyanPrimary.copy(alpha = 0.2f))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "High Yield",
                                color = CyanPrimary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Clinical cutoffs, staging tables, and exam cheat sheets",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = TealAccent,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2x2 Launch Grid
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            DashboardActionCard(
                title = "Spaced Recall",
                subtitle = if (dueFlashcardsCount > 0) "$dueFlashcardsCount cards ready" else "Deck up to date",
                icon = Icons.Default.Psychology,
                accentColor = TealAccent,
                modifier = Modifier
                    .weight(1f)
                    .testTag("launch_spaced_recall"),
                onClick = { onNavigate(Screen.SPACED_REPETITION) }
            )
            DashboardActionCard(
                title = "Question Bank",
                subtitle = "MCQs, EMQs & Essays",
                icon = Icons.Default.Quiz,
                accentColor = CyanPrimary,
                modifier = Modifier
                    .weight(1f)
                    .testTag("launch_question_bank"),
                onClick = { onNavigate(Screen.PRACTICE) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            DashboardActionCard(
                title = "Mock Exam",
                subtitle = "Timed simulation (20m)",
                icon = Icons.Default.HourglassTop,
                accentColor = AmberWarning,
                modifier = Modifier
                    .weight(1f)
                    .testTag("launch_mock_exam"),
                onClick = { onStartMockExam(questions) }
            )
            DashboardActionCard(
                title = "Weak Spots Drill",
                subtitle = if (incorrectQuestionIds.isNotEmpty()) "${incorrectQuestionIds.size} questions to fix" else "No weak spots!",
                icon = Icons.Default.Warning,
                accentColor = if (incorrectQuestionIds.isNotEmpty()) CrimsonError else EmeraldSuccess,
                modifier = Modifier
                    .weight(1f)
                    .testTag("launch_weak_spots"),
                onClick = { onNavigate(Screen.WEAK_SPOTS) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Evidence Sandbox Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CardElevatedBg)
                .border(1.dp, CyanPrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .clickable { onNavigate(Screen.RESOURCE_REGISTRY) }
                .padding(14.dp)
                .testTag("launch_resource_registry")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CyanPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = CyanPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Approved Guidelines & Sandbox",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "BSH 2026, IMWG, WHO • Ingest & inspect literature",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = CyanPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // High-Yield Guideline Highlight: BSH 2026 AL Amyloidosis
        val amyloidGuideline = resources.find { it.id == "bsh_amyloidosis_2026" }
        if (amyloidGuideline != null) {
            Text(
                text = "FEATURED 2026 BSH GUIDELINE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TealAccent,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateNavySurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorderStroke, RoundedCornerShape(12.dp))
                    .clickable { onOpenResourceModal(amyloidGuideline) }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CyanPrimary.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "BSH 2026",
                                color = CyanPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Br J Haematol 2026 (Garg et al.)",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Early Diagnosis of AL Amyloidosis Across Clinics",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Features AL-ISS 2026 cardiac staging with GLS, urine ACR/PCR thresholds, and non-invasive biopsy sequences.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Explore Criteria & Staging Table →",
                            color = TealAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Domain Mastery Breakdown
        Text(
            text = "CURRICULUM DOMAINS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        val domains = listOf(
            "Haemato-Oncology" to CyanPrimary,
            "Haemostasis & Thrombosis" to EmeraldSuccess,
            "Transfusion Medicine" to CrimsonError,
            "General Haematology" to TealAccent
        )

        domains.forEach { (domainName, color) ->
            val domainQuestions = questions.filter { it.domain == domainName }
            val domainAnswered = answers.filter { a ->
                domainQuestions.any { q -> q.id == a.questionId }
            }
            val domainCorrect = domainAnswered.filter { it.isCorrect }.map { it.questionId }.distinct().size
            val domainTotal = domainQuestions.size
            val domainProgress = if (domainTotal > 0) (domainCorrect.toFloat() / domainTotal).coerceIn(0f, 1f) else 0f

            Card(
                colors = CardDefaults.cardColors(containerColor = SlateNavySurface),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, CardBorderStroke, RoundedCornerShape(10.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = domainName,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "$domainCorrect / $domainTotal mastered",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { domainProgress },
                        color = color,
                        trackColor = CardBorderStroke,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun StatRow(label: String, value: String, valueColor: Color = TextPrimary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextSecondary, fontSize = 12.sp)
        Text(text = value, color = valueColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun DashboardActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SlateNavySurface),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .border(1.dp, CardBorderStroke, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 11.sp,
                maxLines = 1
            )
        }
    }
}

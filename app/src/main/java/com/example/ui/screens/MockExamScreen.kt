package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ApprovedResource
import com.example.data.model.Question
import com.example.ui.components.CircularMasteryRing
import com.example.ui.components.DomainChip
import com.example.ui.components.LabDataTable
import com.example.ui.components.QuestionTypeBadge
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

@Composable
fun MockExamScreen(
    questions: List<Question>,
    resources: List<ApprovedResource>,
    currentIndex: Int,
    userAnswers: Map<String, Int>,
    flaggedIds: Set<String>,
    secondsRemaining: Int,
    isFinished: Boolean,
    scorePercentage: Int,
    onSelectAnswer: (String, Int) -> Unit,
    onToggleFlag: (String) -> Unit,
    onSelectIndex: (Int) -> Unit,
    onFinishExam: () -> Unit,
    onRestartExam: () -> Unit,
    onOpenResourceModal: (ApprovedResource) -> Unit,
    onBackToDashboard: () -> Unit
) {
    var showFinishConfirm by remember { mutableStateOf(false) }

    val activeQuestion = questions.getOrNull(currentIndex)
    val activeResource = resources.find { it.id == activeQuestion?.sourceResourceId }

    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    val timerText = String.format("%02d:%02d", minutes, seconds)
    val timerColor = if (secondsRemaining < 300) CrimsonError else TealAccent

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyBg)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onBackToDashboard,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "RCPath PART 1 MOCK SIMULATION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberWarning,
                    letterSpacing = 1.sp
                )
                Text(
                    text = if (isFinished) "Exam Report & Review" else "Question ${currentIndex + 1} of ${questions.size}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            if (!isFinished) {
                // Timer Chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(timerColor.copy(alpha = 0.15f))
                        .border(1.dp, timerColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Alarm,
                            contentDescription = null,
                            tint = timerColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = timerText,
                            color = timerColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (isFinished) {
            // Exam Report Screen
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateNavySurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorderStroke, RoundedCornerShape(16.dp))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    val passed = scorePercentage >= 65
                    val statusColor = if (passed) EmeraldSuccess else CrimsonError

                    CircularMasteryRing(
                        percentage = scorePercentage.toFloat(),
                        title = if (passed) "PASS" else "REFER",
                        size = 120.dp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (passed) "Well done! You met the FRCPath Part 1 Pass Standard (>=65%)."
                        else "Pass standard not achieved. Target weak areas and review rationales below.",
                        color = statusColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onRestartExam,
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, tint = DeepNavyBg)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Retake Mock", color = DeepNavyBg, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onBackToDashboard,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Dashboard", color = TextPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "DETAILED QUESTION REVIEW & EVIDENCE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Review questions list
            questions.forEachIndexed { idx, q ->
                val userPick = userAnswers[q.id]
                val isCorrect = userPick != null && userPick == q.correctIndex

                Card(
                    colors = CardDefaults.cardColors(containerColor = SlateNavySurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(1.dp, if (isCorrect) EmeraldSuccess.copy(alpha = 0.5f) else CrimsonError.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Q${idx + 1}: ${q.title}",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = if (isCorrect) "CORRECT" else "INCORRECT",
                                color = if (isCorrect) EmeraldSuccess else CrimsonError,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = q.stem,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Correct Answer: ${q.options.getOrElse(q.correctIndex) { "Model Answer" }}",
                            color = EmeraldSuccess,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = q.rationale,
                            color = TextPrimary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        } else {
            // Active Exam Screen
            // Question Grid Navigator Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                questions.forEachIndexed { idx, q ->
                    val isCurrent = idx == currentIndex
                    val isAnswered = userAnswers.containsKey(q.id)
                    val isFlagged = flaggedIds.contains(q.id)

                    val bgColor = when {
                        isCurrent -> CyanPrimary
                        isFlagged -> AmberWarning.copy(alpha = 0.2f)
                        isAnswered -> EmeraldSuccess.copy(alpha = 0.2f)
                        else -> CardElevatedBg
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(bgColor)
                            .border(1.dp, if (isFlagged) AmberWarning else if (isCurrent) CyanPrimary else CardBorderStroke, RoundedCornerShape(8.dp))
                            .clickable { onSelectIndex(idx) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${idx + 1}",
                            color = if (isCurrent) DeepNavyBg else TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (activeQuestion != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SlateNavySurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorderStroke, RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            DomainChip(domain = activeQuestion.domain)
                            IconButton(
                                onClick = { onToggleFlag(activeQuestion.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                val isFlagged = flaggedIds.contains(activeQuestion.id)
                                Icon(
                                    imageVector = if (isFlagged) Icons.Default.Flag else Icons.Outlined.Flag,
                                    contentDescription = "Flag for review",
                                    tint = if (isFlagged) AmberWarning else TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = activeQuestion.title,
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = activeQuestion.stem,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )

                        if (!activeQuestion.labData.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            LabDataTable(labData = activeQuestion.labData)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Options
                val userPick = userAnswers[activeQuestion.id]
                val optionLetters = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    activeQuestion.options.forEachIndexed { optIdx, optText ->
                        val letter = optionLetters.getOrElse(optIdx) { "${optIdx + 1}" }
                        val isSelected = userPick == optIdx

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) CyanPrimary.copy(alpha = 0.15f) else SlateNavySurface)
                                .border(1.dp, if (isSelected) CyanPrimary else CardBorderStroke, RoundedCornerShape(10.dp))
                                .clickable { onSelectAnswer(activeQuestion.id, optIdx) }
                                .padding(12.dp)
                                .testTag("mock_option_$optIdx")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) CyanPrimary else CardElevatedBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = letter,
                                        color = if (isSelected) DeepNavyBg else TextSecondary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = optText,
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Bottom Navigation Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { if (currentIndex > 0) onSelectIndex(currentIndex - 1) },
                        enabled = currentIndex > 0,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Previous", color = TextPrimary)
                    }

                    Button(
                        onClick = { showFinishConfirm = true },
                        colors = ButtonDefaults.buttonColors(containerColor = AmberWarning),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("finish_mock_btn")
                    ) {
                        Text("Finish Exam", color = DeepNavyBg, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { if (currentIndex < questions.size - 1) onSelectIndex(currentIndex + 1) },
                        enabled = currentIndex < questions.size - 1,
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Next", color = DeepNavyBg, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    if (showFinishConfirm) {
        AlertDialog(
            onDismissRequest = { showFinishConfirm = false },
            containerColor = SlateNavySurface,
            title = {
                Text(text = "Submit Mock Examination?", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                val answeredCount = userAnswers.size
                Text(
                    text = "You have answered $answeredCount of ${questions.size} questions. Are you ready to submit and calculate your FRCPath Part 1 score?",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showFinishConfirm = false
                        onFinishExam()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                ) {
                    Text("Submit & Score", color = DeepNavyBg, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showFinishConfirm = false }) {
                    Text("Continue Exam", color = TextSecondary)
                }
            }
        )
    }
}

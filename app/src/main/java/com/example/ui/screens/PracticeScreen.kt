package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ApprovedResource
import com.example.data.model.Question
import com.example.ui.components.DomainChip
import com.example.ui.components.GroundingSandboxSeal
import com.example.ui.components.LabDataTable
import com.example.ui.components.QuestionTypeBadge
import com.example.ui.theme.CardBorderStroke
import com.example.ui.theme.CardElevatedBg
import com.example.ui.theme.ChipBg
import com.example.ui.theme.CrimsonError
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.DeepNavyBg
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.LabTableBg
import com.example.ui.theme.SlateNavySurface
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PracticeScreen(
    questions: List<Question>,
    resources: List<ApprovedResource>,
    currentIndex: Int,
    selectedOption: Int?,
    isSubmitted: Boolean,
    candidateNotes: String,
    isModelRevealed: Boolean,
    bookmarkedIds: List<String>,
    selectedDomain: String,
    selectedType: String,
    onDomainSelected: (String) -> Unit,
    onTypeSelected: (String) -> Unit,
    onQuestionIndexChanged: (Int) -> Unit,
    onOptionSelected: (Int) -> Unit,
    onSubmitAnswer: (Question) -> Unit,
    onUpdateNotes: (String) -> Unit,
    onToggleModelRevealed: () -> Unit,
    onToggleBookmark: (String, Boolean) -> Unit,
    onOpenResourceModal: (ApprovedResource) -> Unit,
    onBackToDashboard: () -> Unit
) {
    val filteredQuestions = questions.filter { q ->
        (selectedDomain == "All" || q.domain == selectedDomain) &&
        (selectedType == "All" || q.questionType == selectedType)
    }

    val activeQuestion = filteredQuestions.getOrNull(currentIndex)
    val activeResource = resources.find { it.id == activeQuestion?.sourceResourceId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyBg)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        // Top Navigation & Filter Bar
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
                    text = "QUESTION PRACTICE BANK",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanPrimary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = if (filteredQuestions.isNotEmpty()) "Question ${currentIndex + 1} of ${filteredQuestions.size}" else "0 Questions",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            if (activeQuestion != null) {
                val isBookmarked = bookmarkedIds.contains(activeQuestion.id)
                IconButton(
                    onClick = { onToggleBookmark(activeQuestion.id, isBookmarked) },
                    modifier = Modifier.testTag("bookmark_question_btn")
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) CyanPrimary else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Domain Filters Scrollable Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val domains = listOf("All", "Haemato-Oncology", "Haemostasis & Thrombosis", "Transfusion Medicine", "General Haematology")
            domains.forEach { domain ->
                val isSelected = selectedDomain == domain
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) CyanPrimary else CardElevatedBg)
                        .clickable { onDomainSelected(domain) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = domain,
                        color = if (isSelected) DeepNavyBg else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Question Type Filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val types = listOf("All" to "All Types", "MCQ" to "MCQs (SBA)", "EMQ" to "EMQ Match", "ESSAY" to "Case Essays")
            types.forEach { (typeKey, typeLabel) ->
                val isSelected = selectedType == typeKey
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) TealAccent.copy(alpha = 0.2f) else SlateNavySurface)
                        .border(1.dp, if (isSelected) TealAccent else CardBorderStroke, RoundedCornerShape(6.dp))
                        .clickable { onTypeSelected(typeKey) }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = typeLabel,
                        color = if (isSelected) TealAccent else TextMuted,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (activeQuestion == null) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No questions found matching selected filter.",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        } else {
            // Question Card
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateNavySurface),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorderStroke, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header Tags
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            DomainChip(domain = activeQuestion.domain)
                            Spacer(modifier = Modifier.width(6.dp))
                            QuestionTypeBadge(type = activeQuestion.questionType)
                        }
                        if (activeQuestion.yearPaper != null) {
                            Text(
                                text = activeQuestion.yearPaper,
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Title
                    Text(
                        text = activeQuestion.title,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Stem
                    Text(
                        text = activeQuestion.stem,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    // Lab Data Table if available
                    if (!activeQuestion.labData.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        LabDataTable(labData = activeQuestion.labData)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Options or Essay Mode
            if (activeQuestion.questionType == "ESSAY") {
                // Structured Essay Mode
                Card(
                    colors = CardDefaults.cardColors(containerColor = SlateNavySurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorderStroke, RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = null,
                                tint = CyanPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "CANDIDATE REVISION NOTES / PRACTICE RESPONSE",
                                color = CyanPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = candidateNotes,
                            onValueChange = onUpdateNotes,
                            placeholder = { Text("Formulate your points here, outline investigations, management steps and rationale...", color = TextMuted, fontSize = 13.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .testTag("candidate_notes_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CardElevatedBg,
                                unfocusedContainerColor = CardElevatedBg,
                                focusedBorderColor = CyanPrimary,
                                unfocusedBorderColor = CardBorderStroke,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Reveal Model Answer Button
                        Button(
                            onClick = onToggleModelRevealed,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isModelRevealed) CardElevatedBg else CyanPrimary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("toggle_model_answer_btn")
                        ) {
                            Icon(
                                imageVector = if (isModelRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = if (isModelRevealed) CyanPrimary else DeepNavyBg,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isModelRevealed) "Hide Model Answer & Rubric" else "Reveal Royal College Model Answer & Rubric",
                                color = if (isModelRevealed) CyanPrimary else DeepNavyBg,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        AnimatedVisibility(visible = isModelRevealed, enter = fadeIn(), exit = fadeOut()) {
                            Column(modifier = Modifier.padding(top = 14.dp)) {
                                if (activeQuestion.markingRubric != null) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(LabTableBg)
                                            .border(1.dp, TealAccent.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                            .padding(10.dp)
                                    ) {
                                        Column {
                                            Text(
                                                text = "ROYAL COLLEGE MARKING RUBRIC",
                                                color = TealAccent,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = activeQuestion.markingRubric,
                                                color = TextPrimary,
                                                fontSize = 12.sp,
                                                lineHeight = 16.sp
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                }

                                if (activeQuestion.essayModelAnswer != null) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(CardElevatedBg)
                                            .padding(12.dp)
                                    ) {
                                        Column {
                                            Text(
                                                text = "COMPREHENSIVE MODEL ANSWER",
                                                color = EmeraldSuccess,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = activeQuestion.essayModelAnswer,
                                                color = TextPrimary,
                                                fontSize = 13.sp,
                                                lineHeight = 18.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // MCQ / EMQ Mode
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val optionLetters = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")
                    activeQuestion.options.forEachIndexed { index, optionText ->
                        val letter = optionLetters.getOrElse(index) { "${index + 1}" }
                        val isPicked = selectedOption == index
                        val isCorrectOption = index == activeQuestion.correctIndex

                        val bgColor = when {
                            isSubmitted && isCorrectOption -> EmeraldSuccess.copy(alpha = 0.2f)
                            isSubmitted && isPicked && !isCorrectOption -> CrimsonError.copy(alpha = 0.2f)
                            isPicked -> CyanPrimary.copy(alpha = 0.15f)
                            else -> SlateNavySurface
                        }

                        val borderColor = when {
                            isSubmitted && isCorrectOption -> EmeraldSuccess
                            isSubmitted && isPicked && !isCorrectOption -> CrimsonError
                            isPicked -> CyanPrimary
                            else -> CardBorderStroke
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(bgColor)
                                .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                                .clickable(enabled = !isSubmitted) { onOptionSelected(index) }
                                .padding(12.dp)
                                .testTag("option_$index")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(if (isPicked) CyanPrimary else CardElevatedBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = letter,
                                        color = if (isPicked) DeepNavyBg else TextSecondary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = optionText,
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                if (isSubmitted && isCorrectOption) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Correct",
                                        tint = EmeraldSuccess,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else if (isSubmitted && isPicked && !isCorrectOption) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Incorrect",
                                        tint = CrimsonError,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Check Answer Button
                    if (!isSubmitted) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Button(
                            onClick = { onSubmitAnswer(activeQuestion) },
                            enabled = selectedOption != null,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyanPrimary,
                                disabledContainerColor = CardBorderStroke
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("submit_answer_btn")
                        ) {
                            Text(
                                text = "Submit & Verify Evidence",
                                color = if (selectedOption != null) DeepNavyBg else TextMuted,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Answer Rationale & Grounding Box
                if (isSubmitted) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SlateNavySurface),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, CardBorderStroke, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = TealAccent,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "EVIDENCE-BASED CLINICAL RATIONALE",
                                    color = TealAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = activeQuestion.rationale,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // Evidence Sandbox Seal (always visible on submission or in essay mode)
            if (isSubmitted || activeQuestion.questionType == "ESSAY") {
                Spacer(modifier = Modifier.height(12.dp))
                GroundingSandboxSeal(
                    issuingBody = activeResource?.issuingBody ?: "Accredited Body",
                    guidelineSection = activeQuestion.guidelineSection,
                    evidenceGrade = activeQuestion.evidenceGrade,
                    onViewGuideline = {
                        if (activeResource != null) {
                            onOpenResourceModal(activeResource)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Navigation Pager Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        if (currentIndex > 0) onQuestionIndexChanged(currentIndex - 1)
                    },
                    enabled = currentIndex > 0,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Previous")
                }

                Text(
                    text = "${currentIndex + 1} / ${filteredQuestions.size}",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Button(
                    onClick = {
                        if (currentIndex < filteredQuestions.size - 1) onQuestionIndexChanged(currentIndex + 1)
                    },
                    enabled = currentIndex < filteredQuestions.size - 1,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Next", color = DeepNavyBg, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = DeepNavyBg,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

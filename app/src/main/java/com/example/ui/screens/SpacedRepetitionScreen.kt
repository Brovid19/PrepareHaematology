package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ApprovedResource
import com.example.data.model.Flashcard
import com.example.ui.components.DomainChip
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CardBorderStroke
import com.example.ui.theme.CardElevatedBg
import com.example.ui.theme.CrimsonError
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.DeepNavyBg
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.LabBorder
import com.example.ui.theme.LabTableBg
import com.example.ui.theme.SlateNavySurface
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.FlashcardSessionMode
import com.example.ui.viewmodel.ProficiencyLevel
import com.example.ui.viewmodel.Sm2ReviewOutcome
import com.example.ui.viewmodel.SpacedReviewSchedule
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SpacedRepetitionScreen(
    flashcards: List<Flashcard>,
    resources: List<ApprovedResource>,
    currentIndex: Int,
    isFlipped: Boolean,
    selectedFilter: String,
    sessionMode: FlashcardSessionMode,
    scheduleForecast: SpacedReviewSchedule,
    lastSm2Outcome: Sm2ReviewOutcome?,
    isSessionCompleted: Boolean,
    reviewedInSessionCount: Int,
    isCreateDialogOpen: Boolean = false,
    statusMessage: String? = null,
    onFilterChanged: (String) -> Unit,
    onSessionModeChanged: (FlashcardSessionMode) -> Unit,
    onFlipCard: () -> Unit,
    onRateCard: (Flashcard, Int, List<Flashcard>) -> Unit,
    onNextCard: (Int) -> Unit,
    onPrevCard: (Int) -> Unit,
    onOpenCreateDialog: () -> Unit = {},
    onCloseCreateDialog: () -> Unit = {},
    onDismissMessage: () -> Unit = {},
    onCreateCustomCard: (String, String, String, String, String, String) -> Unit = { _, _, _, _, _, _ -> },
    onDismissSessionCompleted: () -> Unit,
    onOpenResourceModal: (ApprovedResource) -> Unit,
    onSpeakFlashcard: (Flashcard, ApprovedResource?) -> Unit = { _, _ -> },
    onAddFlashcardToPlaylist: (Flashcard, ApprovedResource?) -> Unit = { _, _ -> },
    onAddAllDueFlashcardsToPlaylist: (List<Flashcard>, List<ApprovedResource>) -> Unit = { _, _ -> },
    onAddAllFlashcardsToPlaylist: (List<Flashcard>, List<ApprovedResource>) -> Unit = { _, _ -> },
    onOpenPlaylist: () -> Unit = {},
    isTtsPlaying: Boolean = false,
    activeSpeakingCardId: String? = null,
    onBackToDashboard: () -> Unit
) {
    val now = System.currentTimeMillis()

    // Filter by guideline resource
    val resourceFiltered = flashcards.filter { card ->
        when (selectedFilter) {
            "All" -> true
            "BSH 2026 Amyloidosis" -> card.sourceResourceId == "bsh_amyloidosis_2026"
            "IMWG Myeloma" -> card.sourceResourceId == "imwg_myeloma_2021"
            "WHO Classification" -> card.sourceResourceId == "who_haem_5th"
            "Transfusion & Safety" -> card.sourceResourceId == "bsh_transfusion_components"
            "Haemostasis & HIT" -> card.sourceResourceId == "ukhcdo_haemostasis_2022"
            else -> true
        }
    }

    // Filter by session mode
    val filteredCards = resourceFiltered.filter { card ->
        when (sessionMode) {
            FlashcardSessionMode.ALL_CARDS -> true
            FlashcardSessionMode.DUE_FOR_REVIEW -> card.dueDateTimestamp <= now
            FlashcardSessionMode.DIFFICULT_DRILL -> card.easeFactor < 2.2f || card.intervalDays <= 1
        }
    }

    val safeIndex = if (filteredCards.isNotEmpty()) currentIndex.coerceIn(0, filteredCards.size - 1) else 0
    val activeCard = filteredCards.getOrNull(safeIndex)
    val activeResource = resources.find { it.id == activeCard?.sourceResourceId }

    // 3D Flip animation
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 350),
        label = "card_flip"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyBg)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        // Top Navigation Header
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
                    text = "SPACED REPETITION ENGINE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealAccent,
                    letterSpacing = 1.sp
                )
                Text(
                    text = if (filteredCards.isNotEmpty()) "Card ${safeIndex + 1} of ${filteredCards.size}" else "0 Flashcards",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            // "+ New Card" Action Button
            Button(
                onClick = onOpenCreateDialog,
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("open_create_card_dialog_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = DeepNavyBg,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "New Card",
                    color = DeepNavyBg,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        // Status / Success notification banner
        if (statusMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(EmeraldSuccess.copy(alpha = 0.15f))
                    .border(1.dp, EmeraldSuccess.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = statusMessage,
                        color = EmeraldSuccess,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onDismissMessage,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss",
                            tint = EmeraldSuccess,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Upcoming Review Schedule & Retention Forecast Card
        Card(
            colors = CardDefaults.cardColors(containerColor = SlateNavySurface),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CardBorderStroke, RoundedCornerShape(14.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = CyanPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "UPCOMING REVIEW SESSIONS FORECAST",
                            color = CyanPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    }
                    Text(
                        text = "Retention: ${scheduleForecast.estimatedRetentionRate}%",
                        color = EmeraldSuccess,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Schedule timeline pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SchedulePill(
                        label = "Due Today",
                        count = scheduleForecast.dueNowCount,
                        color = if (scheduleForecast.dueNowCount > 0) AmberWarning else EmeraldSuccess,
                        modifier = Modifier.weight(1f)
                    )
                    SchedulePill(
                        label = "Tomorrow",
                        count = scheduleForecast.dueTomorrowCount,
                        color = TealAccent,
                        modifier = Modifier.weight(1f)
                    )
                    SchedulePill(
                        label = "2-3 Days",
                        count = scheduleForecast.dueIn2To3DaysCount,
                        color = CyanPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    SchedulePill(
                        label = "This Week",
                        count = scheduleForecast.dueThisWeekCount,
                        color = TextSecondary,
                        modifier = Modifier.weight(1f)
                    )
                    SchedulePill(
                        label = "Later (8d+)",
                        count = scheduleForecast.dueLaterCount,
                        color = EmeraldSuccess,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Proficiency distribution row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ProficiencyCounterBadge(label = "Mastered", count = scheduleForecast.totalMastered, color = EmeraldSuccess)
                        ProficiencyCounterBadge(label = "Proficient", count = scheduleForecast.totalProficient, color = TealAccent)
                        ProficiencyCounterBadge(label = "Learning", count = scheduleForecast.totalLearning, color = AmberWarning)
                    }
                    Text(
                        text = String.format("Avg Ease: %.2f", scheduleForecast.averageEaseFactor),
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Audio Playlist Quick Action Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { onAddAllDueFlashcardsToPlaylist(resourceFiltered, resources) },
                modifier = Modifier.weight(1f).testTag("queue_due_cards_btn"),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD66371)),
                border = BorderStroke(1.dp, Color(0xFFD66371).copy(alpha = 0.5f)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp, vertical = 5.dp)
            ) {
                Icon(imageVector = Icons.Default.PlaylistAdd, contentDescription = null, modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Queue Due (${scheduleForecast.dueNowCount})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            OutlinedButton(
                onClick = onOpenPlaylist,
                modifier = Modifier.weight(1f).testTag("open_tts_playlist_btn"),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TealAccent),
                border = BorderStroke(1.dp, TealAccent.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp, vertical = 5.dp)
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.QueueMusic, contentDescription = null, modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Audio Playlist",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Session Mode Switcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val modes = listOf(
                FlashcardSessionMode.ALL_CARDS to "All (${resourceFiltered.size})",
                FlashcardSessionMode.DUE_FOR_REVIEW to "Due Today (${scheduleForecast.dueNowCount})",
                FlashcardSessionMode.DIFFICULT_DRILL to "Difficult Drill"
            )
            modes.forEach { (mode, label) ->
                val isSelected = sessionMode == mode
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) TealAccent.copy(alpha = 0.2f) else SlateNavySurface)
                        .border(1.dp, if (isSelected) TealAccent else CardBorderStroke, RoundedCornerShape(8.dp))
                        .clickable { onSessionModeChanged(mode) }
                        .padding(vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) TealAccent else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Guideline Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filters = listOf(
                "All",
                "BSH 2026 Amyloidosis",
                "IMWG Myeloma",
                "WHO Classification",
                "Transfusion & Safety",
                "Haemostasis & HIT"
            )
            filters.forEach { filter ->
                val isSelected = selectedFilter == filter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) CyanPrimary else CardElevatedBg)
                        .clickable { onFilterChanged(filter) }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) DeepNavyBg else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // SM-2 Review Outcome Feedback Toast
        AnimatedVisibility(
            visible = lastSm2Outcome != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if (lastSm2Outcome != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(LabTableBg)
                        .border(1.dp, CyanPrimary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = CyanPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        val dateFormat = SimpleDateFormat("MMM dd", Locale.UK)
                        val dueDateStr = dateFormat.format(Date(lastSm2Outcome.nextDueDate))

                        Text(
                            text = "SM-2 Interval: ${lastSm2Outcome.oldInterval}d → ${lastSm2Outcome.newInterval}d (Rep ${lastSm2Outcome.newRepetitions}) • Next Due: $dueDateStr • Status: ${lastSm2Outcome.proficiency.name}",
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        if (activeCard == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SlateNavySurface)
                    .border(1.dp, CardBorderStroke, RoundedCornerShape(16.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = EmeraldSuccess,
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (sessionMode == FlashcardSessionMode.DUE_FOR_REVIEW) "All Due Reviews Completed!" else "No Cards in this View",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (sessionMode == FlashcardSessionMode.DUE_FOR_REVIEW) "Outstanding cards for today have been reviewed. Check the forecast schedule above for upcoming sessions." else "Switch session mode or clear filter to view other cards.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Flashcard with 3D Flip
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateNavySurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 12f * density
                    }
                    .border(1.dp, CardBorderStroke, RoundedCornerShape(16.dp))
                    .clickable(onClick = onFlipCard)
                    .testTag("flashcard_container")
            ) {
                if (rotation <= 90f) {
                    // FRONT OF CARD
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            DomainChip(domain = activeCard.domain)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Card proficiency chip
                                val profColor = when {
                                    activeCard.intervalDays >= 21 && activeCard.repetitionCount >= 4 -> EmeraldSuccess
                                    activeCard.intervalDays >= 6 && activeCard.repetitionCount >= 2 -> TealAccent
                                    else -> AmberWarning
                                }
                                val profText = when {
                                    activeCard.intervalDays >= 21 && activeCard.repetitionCount >= 4 -> "MASTERED"
                                    activeCard.intervalDays >= 6 && activeCard.repetitionCount >= 2 -> "PROFICIENT"
                                    else -> "LEARNING"
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(profColor.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = profText,
                                        color = profColor,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Rep: ${activeCard.repetitionCount}",
                                    color = TextSecondary,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = activeCard.front,
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Start
                        )

                        Spacer(modifier = Modifier.height(26.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = null,
                                tint = TealAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Tap card to flip to high-yield guideline criteria",
                                color = TealAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    // BACK OF CARD
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer { rotationY = 180f }
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "HIGH-YIELD CRITERIA & EVIDENCE",
                                color = EmeraldSuccess,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Text(
                                text = "Interval: ${activeCard.intervalDays}d • EF: ${String.format("%.2f", activeCard.easeFactor)}",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = activeCard.back,
                            color = TextPrimary,
                            fontSize = 15.sp,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Guideline Reference Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(CardElevatedBg)
                                .border(1.dp, CardBorderStroke, RoundedCornerShape(8.dp))
                                .clickable {
                                    if (activeResource != null) {
                                        onOpenResourceModal(activeResource)
                                    }
                                }
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = CyanPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "SOURCE GUIDELINE REFERENCE",
                                        color = CyanPrimary,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = activeCard.guidelineRef,
                                        color = TextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Text(
                                    text = "View",
                                    color = TealAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // SM-2 Recall Rating Buttons
            Text(
                text = "HOW WELL DID YOU RECALL THIS? (SM-2 ADAPTIVE RATING)",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Again (0)
                RatingButton(
                    label = "Again",
                    sublabel = "Reset 1d",
                    color = CrimsonError,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("rate_again_btn"),
                    onClick = { onRateCard(activeCard, 0, filteredCards) }
                )
                // Hard (2)
                RatingButton(
                    label = "Hard",
                    sublabel = "${(activeCard.intervalDays * 1.2).toInt().coerceAtLeast(1)}d",
                    color = AmberWarning,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("rate_hard_btn"),
                    onClick = { onRateCard(activeCard, 2, filteredCards) }
                )
                // Good (3)
                RatingButton(
                    label = "Good",
                    sublabel = "${(activeCard.intervalDays * 2.5).toInt().coerceAtLeast(3)}d",
                    color = TealAccent,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("rate_good_btn"),
                    onClick = { onRateCard(activeCard, 3, filteredCards) }
                )
                // Easy (5)
                RatingButton(
                    label = "Easy",
                    sublabel = "${(activeCard.intervalDays * 3.5).toInt().coerceAtLeast(7)}d",
                    color = EmeraldSuccess,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("rate_easy_btn"),
                    onClick = { onRateCard(activeCard, 5, filteredCards) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Audio Revision Controls for Current Flashcard
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val isSpeakingThisCard = isTtsPlaying && activeSpeakingCardId == "card_${activeCard.id}"
                Button(
                    onClick = { onSpeakFlashcard(activeCard, activeResource) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSpeakingThisCard) Color(0xFFD66371) else TealAccent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("flashcard_tts_listen_btn")
                ) {
                    Icon(
                        imageVector = if (isSpeakingThisCard) Icons.Default.GraphicEq else Icons.Default.VolumeUp,
                        contentDescription = "Read flashcard aloud",
                        tint = DeepNavyBg,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isSpeakingThisCard) "Speaking..." else "Listen (TTS)",
                        color = DeepNavyBg,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = { onAddFlashcardToPlaylist(activeCard, activeResource) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanPrimary),
                    border = BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("flashcard_add_playlist_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlaylistAdd,
                        contentDescription = "Add to Audio Playlist",
                        tint = CyanPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "+ Audio Queue",
                        color = CyanPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Navigation Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onPrevCard(filteredCards.size) },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous Card",
                        tint = TextSecondary
                    )
                }

                Button(
                    onClick = onFlipCard,
                    colors = ButtonDefaults.buttonColors(containerColor = CardElevatedBg),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("flip_card_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Autorenew,
                        contentDescription = null,
                        tint = CyanPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isFlipped) "Show Question" else "Flip to Answer",
                        color = CyanPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = { onNextCard(filteredCards.size) },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next Card",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Session Completed Dialog
    if (isSessionCompleted) {
        AlertDialog(
            onDismissRequest = onDismissSessionCompleted,
            containerColor = SlateNavySurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Spaced Session Complete!", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Great job! You reviewed $reviewedInSessionCount cards scheduled for today.",
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your next review session has been scheduled in the SM-2 algorithm for tomorrow. Check back then to lock these criteria into long-term memory.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = onDismissSessionCompleted,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                ) {
                    Text("Done", color = DeepNavyBg, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Create Custom Flashcard Dialog ("Type your own cards")
    if (isCreateDialogOpen) {
        CreateCustomFlashcardDialog(
            resources = resources,
            onDismiss = onCloseCreateDialog,
            onCreate = onCreateCustomCard
        )
    }
}

@Composable
fun SchedulePill(
    label: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
            .padding(vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$count",
                color = color,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = TextSecondary,
                fontSize = 9.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ProficiencyCounterBadge(label: String, count: Int, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$label: $count",
            color = TextSecondary,
            fontSize = 10.sp
        )
    }
}

@Composable
fun RatingButton(
    label: String,
    sublabel: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                color = color,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = sublabel,
                color = TextSecondary,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun CreateCustomFlashcardDialog(
    resources: List<ApprovedResource>,
    onDismiss: () -> Unit,
    onCreate: (front: String, back: String, domain: String, sourceResourceId: String, guidelineSection: String, evidenceGrade: String) -> Unit
) {
    var front by remember { mutableStateOf("") }
    var back by remember { mutableStateOf("") }
    var selectedDomain by remember { mutableStateOf("Malignant Haematology") }
    var selectedResourceId by remember { mutableStateOf(resources.firstOrNull()?.id ?: "bsh_amyloidosis_2026") }
    var section by remember { mutableStateOf("Diagnostic Thresholds") }
    var evidenceGrade by remember { mutableStateOf("Grade 1A") }
    var localError by remember { mutableStateOf<String?>(null) }

    val domains = listOf(
        "Malignant Haematology",
        "Haemostasis & Thrombosis",
        "Transfusion Medicine",
        "General Haematology"
    )

    val grades = listOf("Grade 1A", "Grade 1B", "Grade 2A", "Grade 2B")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SlateNavySurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = CyanPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Type Your Own Flashcard",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "SM-2 Spaced Repetition Engine",
                        color = TealAccent,
                        fontSize = 11.sp
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Author custom clinical recall cards. Your card will enter the SM-2 adaptive queue starting with interval = 1 day and ease factor = 2.5.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                if (localError != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = localError!!,
                        color = CrimsonError,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Card Front (Prompt)
                Text(text = "CARD FRONT (CLINICAL PROMPT / QUESTION)", color = CyanPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = front,
                    onValueChange = { front = it; localError = null },
                    placeholder = { Text("e.g. What is the target serum ferritin during venesection induction in hemochromatosis?", color = TextMuted, fontSize = 12.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_card_front_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardElevatedBg,
                        unfocusedContainerColor = CardElevatedBg,
                        focusedBorderColor = CyanPrimary,
                        unfocusedBorderColor = CardBorderStroke,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    minLines = 2,
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Card Back (Model Answer)
                Text(text = "CARD BACK (MODEL ANSWER & CRITERIA)", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = back,
                    onValueChange = { back = it; localError = null },
                    placeholder = { Text("e.g. Target serum ferritin <50 mcg/L. Verify Hb >=110 g/L prior to each venesection session.", color = TextMuted, fontSize = 12.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_card_back_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardElevatedBg,
                        unfocusedContainerColor = CardElevatedBg,
                        focusedBorderColor = EmeraldSuccess,
                        unfocusedBorderColor = CardBorderStroke,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    minLines = 2,
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Domain Selection
                Text(text = "CLINICAL DOMAIN", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    domains.forEach { dom ->
                        val isSel = selectedDomain == dom
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSel) CyanPrimary else DeepNavyBg)
                                .clickable { selectedDomain = dom }
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = dom,
                                color = if (isSel) DeepNavyBg else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Source Guideline (Evidence Sandbox Grounding)
                Text(text = "GROUNDING GUIDELINE (DATABASE SANDBOX)", color = TealAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    resources.forEach { res ->
                        val isSel = selectedResourceId == res.id
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSel) TealAccent else DeepNavyBg)
                                .border(1.dp, if (isSel) TealAccent else CardBorderStroke, RoundedCornerShape(6.dp))
                                .clickable { selectedResourceId = res.id }
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "${res.issuingBody.take(4)} • ${res.title.take(22)}...",
                                color = if (isSel) DeepNavyBg else TextPrimary,
                                fontSize = 10.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Section & Evidence Grade
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "GUIDELINE SECTION", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = section,
                            onValueChange = { section = it },
                            placeholder = { Text("Section / Algorithm", color = TextMuted, fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CardElevatedBg,
                                unfocusedContainerColor = CardElevatedBg,
                                focusedBorderColor = CyanPrimary,
                                unfocusedBorderColor = CardBorderStroke,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(6.dp),
                            singleLine = true
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "EVIDENCE GRADE", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            grades.take(2).forEach { gr ->
                                val isSel = evidenceGrade == gr
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSel) EmeraldSuccess else DeepNavyBg)
                                        .clickable { evidenceGrade = gr }
                                        .padding(horizontal = 6.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = gr,
                                        color = if (isSel) DeepNavyBg else TextSecondary,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (front.isBlank() || back.isBlank()) {
                        localError = "Please enter both front clinical prompt and back model answer."
                    } else {
                        onCreate(front, back, selectedDomain, selectedResourceId, section, evidenceGrade)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                modifier = Modifier.testTag("submit_create_card_btn")
            ) {
                Text(text = "Add to SM-2 Deck", color = DeepNavyBg, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(text = "Cancel", color = TextSecondary)
            }
        }
    )
}


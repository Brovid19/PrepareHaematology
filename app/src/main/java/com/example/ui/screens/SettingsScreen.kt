package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.AppSettings
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

@Composable
fun SettingsScreen(
    currentSettings: AppSettings,
    savedMessage: String?,
    onUpdateSettings: (AppSettings) -> Unit,
    onResetToDefaults: () -> Unit,
    onResetSpacedProgress: () -> Unit,
    onDismissMessage: () -> Unit,
    onBackToDashboard: () -> Unit
) {
    var showResetDefaultsDialog by remember { mutableStateOf(false) }
    var showResetProgressDialog by remember { mutableStateOf(false) }

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
                modifier = Modifier.size(36.dp).testTag("settings_back_btn")
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
                    text = "APPLICATION CONFIGURATION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanPrimary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Preferences & Defaults",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            // Quick Reset Button
            OutlinedButton(
                onClick = { showResetDefaultsDialog = true },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, CardBorderStroke),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.testTag("settings_reset_btn")
            ) {
                Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Defaults", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        // Status / Confirmation Banner
        if (savedMessage != null) {
            Spacer(modifier = Modifier.height(10.dp))
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
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = savedMessage,
                            color = EmeraldSuccess,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(onClick = onDismissMessage, modifier = Modifier.size(24.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Dismiss", tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // SECTION 1: Audio & Text-to-Speech Defaults
        SettingsSectionCard(
            title = "AUDIO & TEXT-TO-SPEECH (TTS)",
            icon = Icons.Default.Headphones,
            accentColor = TealAccent
        ) {
            // Speech Rate
            Text(
                text = "Default Narration Speed",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = "Controls base playback rate for guideline summaries and flashcard decks.",
                fontSize = 11.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(0.85f to "0.85x", 1.0f to "1.0x (Normal)", 1.25f to "1.25x (Fast)", 1.5f to "1.5x (Speed)").forEach { (rate, label) ->
                    val isSelected = (currentSettings.defaultSpeechRate - rate) in -0.05f..0.05f
                    SettingsChoiceChip(
                        label = label,
                        isSelected = isSelected,
                        accentColor = TealAccent,
                        modifier = Modifier.weight(1f),
                        onClick = { onUpdateSettings(currentSettings.copy(defaultSpeechRate = rate)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Speech Voice Accent
            Text(
                text = "Pronunciation Dialect",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = "British English is calibrated for UK haematology guidelines and FRCPath nomenclature.",
                fontSize = 11.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("en-GB" to "British English (UK FRCPath)", "en-US" to "Standard English (US)").forEach { (locale, label) ->
                    val isSelected = currentSettings.preferredVoiceLocale == locale
                    SettingsChoiceChip(
                        label = label,
                        isSelected = isSelected,
                        accentColor = TealAccent,
                        modifier = Modifier.weight(1f),
                        onClick = { onUpdateSettings(currentSettings.copy(preferredVoiceLocale = locale)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Auto-advance toggle
            SettingsToggleRow(
                title = "Auto-Advance Playlist",
                subtitle = "Seamlessly transition to next queued guideline or flashcard.",
                checked = currentSettings.autoAdvancePlaylist,
                onCheckedChange = { onUpdateSettings(currentSettings.copy(autoAdvancePlaylist = it)) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Read Evidence Grades
            SettingsToggleRow(
                title = "Recite Evidence Level & Grades",
                subtitle = "Speaks BSH/WHO guideline evidence rating (e.g. Grade 1A, Grade 2C) on flashcards.",
                checked = currentSettings.readEvidenceGradesAloud,
                onCheckedChange = { onUpdateSettings(currentSettings.copy(readEvidenceGradesAloud = it)) }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // SECTION 2: Spaced Repetition (SM-2) Defaults
        SettingsSectionCard(
            title = "SPACED REPETITION (SM-2 ALGORITHM)",
            icon = Icons.Default.Psychology,
            accentColor = Color(0xFFD66371)
        ) {
            // Daily Flashcard Goal
            Text(
                text = "Daily Revision Target",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = "Target number of cards to complete each study session.",
                fontSize = 11.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(10 to "10 Cards", 20 to "20 (Recommended)", 30 to "30 Cards", 50 to "50 Cards").forEach { (goal, label) ->
                    val isSelected = currentSettings.dailyFlashcardGoal == goal
                    SettingsChoiceChip(
                        label = label,
                        isSelected = isSelected,
                        accentColor = Color(0xFFD66371),
                        modifier = Modifier.weight(1f),
                        onClick = { onUpdateSettings(currentSettings.copy(dailyFlashcardGoal = goal)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Default Session Filter Mode
            Text(
                text = "Default Deck Session Mode",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = "Starting review mode when opening the Spaced Recall screen.",
                fontSize = 11.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(
                    "DUE_FOR_REVIEW" to "Due Today",
                    "ALL_CARDS" to "All Cards",
                    "DIFFICULT_DRILL" to "Difficult Drill"
                ).forEach { (mode, label) ->
                    val isSelected = currentSettings.defaultSessionMode == mode
                    SettingsChoiceChip(
                        label = label,
                        isSelected = isSelected,
                        accentColor = Color(0xFFD66371),
                        modifier = Modifier.weight(1f),
                        onClick = { onUpdateSettings(currentSettings.copy(defaultSessionMode = mode)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Show Guideline Ref on Front
            SettingsToggleRow(
                title = "Display Guideline Section on Question Front",
                subtitle = "Shows source guideline name before flipping (useful for targeted topic study).",
                checked = currentSettings.showReferenceOnFront,
                onCheckedChange = { onUpdateSettings(currentSettings.copy(showReferenceOnFront = it)) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Initial Ease Factor Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Initial SM-2 Ease Factor",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Standard SuperMemo baseline ease factor for newly ingested cards.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
                Text(
                    text = String.format("%.2f", currentSettings.defaultEaseFactor),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD66371)
                )
            }
            Slider(
                value = currentSettings.defaultEaseFactor,
                onValueChange = { onUpdateSettings(currentSettings.copy(defaultEaseFactor = (it * 10).toInt() / 10f)) },
                valueRange = 1.8f..2.8f,
                steps = 9,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFD66371),
                    activeTrackColor = Color(0xFFD66371),
                    inactiveTrackColor = LabTableBg
                )
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // SECTION 3: Practice & Timed Mock Exam Defaults
        SettingsSectionCard(
            title = "PRACTICE & TIMED MOCK EXAM",
            icon = Icons.Default.Quiz,
            accentColor = CyanPrimary
        ) {
            // Default Mock Duration
            Text(
                text = "Default Mock Exam Duration",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = "Countdown timer duration for standard FRCPath Part 1 practice runs.",
                fontSize = 11.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(15 to "15 Mins", 20 to "20 Mins (Standard)", 30 to "30 Mins", 45 to "45 Mins").forEach { (mins, label) ->
                    val isSelected = currentSettings.defaultMockDurationMinutes == mins
                    SettingsChoiceChip(
                        label = label,
                        isSelected = isSelected,
                        accentColor = CyanPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = { onUpdateSettings(currentSettings.copy(defaultMockDurationMinutes = mins)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Default Practice Domain
            Text(
                text = "Default Practice Domain",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = "Initial syllabus filter when opening the Practice Bank.",
                fontSize = 11.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("All", "Malignant", "Haemostasis", "Transfusion", "General").forEach { domain ->
                    val isSelected = currentSettings.defaultPracticeDomain == domain
                    SettingsChoiceChip(
                        label = domain,
                        isSelected = isSelected,
                        accentColor = CyanPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = { onUpdateSettings(currentSettings.copy(defaultPracticeDomain = domain)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Instant feedback in practice
            SettingsToggleRow(
                title = "Instant Answer & Distractor Feedback",
                subtitle = "Immediately show guideline citations and rationale upon selecting an option in Practice Mode.",
                checked = currentSettings.instantFeedbackInPractice,
                onCheckedChange = { onUpdateSettings(currentSettings.copy(instantFeedbackInPractice = it)) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Randomize questions
            SettingsToggleRow(
                title = "Shuffle Practice Questions",
                subtitle = "Randomize question presentation order across sub-specialty domains.",
                checked = currentSettings.shuffleQuestions,
                onCheckedChange = { onUpdateSettings(currentSettings.copy(shuffleQuestions = it)) }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // SECTION 4: Theme & Display
        SettingsSectionCard(
            title = "THEME & DISPLAY",
            icon = Icons.Default.Palette,
            accentColor = AmberWarning
        ) {
            // Theme Scheme
            Text(
                text = "Interface Colour Scheme",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = "Prepare Haematology dark theme optimized with soft reds and high legibility.",
                fontSize = 11.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(
                    "DARK_SOFT_RED" to "Soft Red (Default)",
                    "OLED_BLACK" to "Pure OLED",
                    "SYSTEM" to "System Auto"
                ).forEach { (themeKey, label) ->
                    val isSelected = currentSettings.displayThemeMode == themeKey
                    SettingsChoiceChip(
                        label = label,
                        isSelected = isSelected,
                        accentColor = AmberWarning,
                        modifier = Modifier.weight(1f),
                        onClick = { onUpdateSettings(currentSettings.copy(displayThemeMode = themeKey)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Summaries Default Expansion
            Text(
                text = "Summaries Accordion Default View",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = "Determines whether guideline cards start collapsed or expanded.",
                fontSize = 11.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(
                    "EXPAND_FIRST" to "Expand First Only",
                    "COLLAPSE_ALL" to "Collapse All",
                    "EXPAND_ALL" to "Expand All"
                ).forEach { (expKey, label) ->
                    val isSelected = currentSettings.summariesDefaultExpansion == expKey
                    SettingsChoiceChip(
                        label = label,
                        isSelected = isSelected,
                        accentColor = AmberWarning,
                        modifier = Modifier.weight(1f),
                        onClick = { onUpdateSettings(currentSettings.copy(summariesDefaultExpansion = expKey)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Text Scaling
            Text(
                text = "Reading Text Size",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(100 to "Standard (100%)", 115 to "Large (115%)", 130 to "Extra Large (130%)").forEach { (scale, label) ->
                    val isSelected = currentSettings.textScalePercent == scale
                    SettingsChoiceChip(
                        label = label,
                        isSelected = isSelected,
                        accentColor = AmberWarning,
                        modifier = Modifier.weight(1f),
                        onClick = { onUpdateSettings(currentSettings.copy(textScalePercent = scale)) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // SECTION 5: Maintenance & Data Reset
        SettingsSectionCard(
            title = "DATA MAINTENANCE & FACTORY RESET",
            icon = Icons.Default.Warning,
            accentColor = CrimsonError
        ) {
            Text(
                text = "Progress & Scheduling Operations",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = "Reset repetition history if restarting revision from scratch before the exam.",
                fontSize = 11.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { showResetProgressDialog = true },
                    modifier = Modifier.weight(1f).testTag("reset_spaced_progress_btn"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD66371)),
                    border = BorderStroke(1.dp, Color(0xFFD66371).copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Reset SM-2 Deck", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { showResetDefaultsDialog = true },
                    modifier = Modifier.weight(1f).testTag("reset_all_defaults_btn"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                    border = BorderStroke(1.dp, LabBorder),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Reset Defaults", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // App Information Footer Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardElevatedBg),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, CardBorderStroke)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.School, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Prepare Haematology • FRCPath Part 1",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Includes latest British Journal of Haematology (BJH 2026) guidelines: Biss et al. (Haem-Gynae MDT), Tam et al. (POCT Quality), MacDonald et al. (TTP ADAMTS13), and Osborne et al. (PMBCL). Grounded in BSH, WHO 5th Ed, and UKHCDO accredited evidence.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    // Confirmation Dialog: Reset Defaults
    if (showResetDefaultsDialog) {
        AlertDialog(
            onDismissRequest = { showResetDefaultsDialog = false },
            containerColor = SlateNavySurface,
            title = {
                Text(text = "Reset All Settings to Defaults?", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "This will restore all audio, spaced repetition, mock exam, and display preferences to default values.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResetToDefaults()
                        showResetDefaultsDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TealAccent),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Reset", color = DeepNavyBg, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showResetDefaultsDialog = false },
                    border = BorderStroke(1.dp, LabBorder),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Cancel", color = TextSecondary)
                }
            }
        )
    }

    // Confirmation Dialog: Reset Spaced Progress
    if (showResetProgressDialog) {
        AlertDialog(
            onDismissRequest = { showResetProgressDialog = false },
            containerColor = SlateNavySurface,
            title = {
                Text(text = "Reset Spaced Repetition Progress?", color = CrimsonError, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "This will reset interval days to 1 and repetition counts to 0 for all flashcards, returning the deck to day-one review status. Custom flashcards will be preserved.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResetSpacedProgress()
                        showResetProgressDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonError),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Confirm Reset", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showResetProgressDialog = false },
                    border = BorderStroke(1.dp, LabBorder),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
fun SettingsSectionCard(
    title: String,
    icon: ImageVector,
    accentColor: Color,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SlateNavySurface),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, CardBorderStroke)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    letterSpacing = 0.8.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun SettingsChoiceChip(
    label: String,
    isSelected: Boolean,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) accentColor.copy(alpha = 0.22f) else LabTableBg)
            .border(
                1.dp,
                if (isSelected) accentColor else LabBorder,
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 7.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) accentColor else TextMuted,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun SettingsToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = DeepNavyBg,
                checkedTrackColor = TealAccent,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = LabTableBg
            )
        )
    }
}

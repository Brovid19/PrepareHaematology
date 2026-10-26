package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ApprovedResource
import com.example.data.model.Flashcard
import com.example.data.model.Question
import com.example.ui.components.DomainChip
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CardBorderStroke
import com.example.ui.theme.CardElevatedBg
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
fun SummarisedGuidelinesScreen(
    resources: List<ApprovedResource>,
    questions: List<Question>,
    flashcards: List<Flashcard>,
    onPracticeGuideline: (String) -> Unit,
    onRecallGuideline: (String) -> Unit,
    onBackToDashboard: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    val expandedGuidelineIds = remember { mutableStateMapOf<String, Boolean>() }

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    // Pre-expand first item (e.g. 2026 BSH AL Amyloidosis) initially
    if (expandedGuidelineIds.isEmpty() && resources.isNotEmpty()) {
        expandedGuidelineIds[resources.first().id] = true
    }

    val filtered = resources.filter { res ->
        val matchesCat = selectedCategory == "All" || res.category == selectedCategory
        val matchesQuery = searchQuery.isBlank() ||
                res.title.contains(searchQuery, ignoreCase = true) ||
                res.issuingBody.contains(searchQuery, ignoreCase = true) ||
                res.keyCutoffs.contains(searchQuery, ignoreCase = true) ||
                res.summary.contains(searchQuery, ignoreCase = true)
        matchesCat && matchesQuery
    }

    val expandedCount = filtered.count { expandedGuidelineIds[it.id] == true }

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
                    text = "FRCPath EXAM PREPARATION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanPrimary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Summarised Guidelines",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(EmeraldSuccess.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${resources.size} Summaries",
                    color = EmeraldSuccess,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Description Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(CardElevatedBg)
                .border(1.dp, CardBorderStroke, RoundedCornerShape(10.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = TealAccent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "High-Yield Clinical Guideline Digests",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Collapsible accordion view of diagnostic cutoffs, staging systems, and Grade 1A/1B recommendations for the FRCPath syllabus.",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Filter summaries (e.g. NT-proBNP, Fludarabine, 4Ts, APL)...", color = TextMuted, fontSize = 12.sp) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("summaries_search_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SlateNavySurface,
                unfocusedContainerColor = SlateNavySurface,
                focusedBorderColor = CyanPrimary,
                unfocusedBorderColor = CardBorderStroke,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val categories = listOf("All", "Malignant Haematology", "Haemostasis & Thrombosis", "Transfusion Medicine", "General Haematology")
            categories.forEach { cat ->
                val isSelected = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) CyanPrimary else CardElevatedBg)
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) DeepNavyBg else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Collapsible List Controls: "Expand All" & "Collapse All" Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(CardElevatedBg)
                .border(1.dp, CardBorderStroke, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "COLLAPSIBLE LIST",
                    color = TealAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CyanPrimary.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "$expandedCount of ${filtered.size} Expanded",
                        color = CyanPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Expand All Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SlateNavySurface)
                        .border(1.dp, CyanPrimary.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                        .clickable {
                            filtered.forEach { res -> expandedGuidelineIds[res.id] = true }
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("expand_all_guidelines_btn")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.UnfoldMore,
                            contentDescription = null,
                            tint = CyanPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Expand All",
                            color = CyanPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Collapse All Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SlateNavySurface)
                        .border(1.dp, CardBorderStroke, RoundedCornerShape(6.dp))
                        .clickable {
                            filtered.forEach { res -> expandedGuidelineIds[res.id] = false }
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("collapse_all_guidelines_btn")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.UnfoldLess,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Collapse All",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // List of Collapsible Guideline Accordion Cards
        filtered.forEach { res ->
            val isExpanded = expandedGuidelineIds[res.id] == true
            val resQuestions = questions.filter { it.sourceResourceId == res.id }
            val resCards = flashcards.filter { it.sourceResourceId == res.id }

            // Chevron rotation animation
            val chevronRotation by animateFloatAsState(
                targetValue = if (isExpanded) 180f else 0f,
                animationSpec = tween(durationMillis = 250),
                label = "chevron_rotate"
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = SlateNavySurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .border(
                        1.dp,
                        if (isExpanded) CyanPrimary.copy(alpha = 0.6f) else CardBorderStroke,
                        RoundedCornerShape(12.dp)
                    )
                    .testTag("summary_card_${res.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Accordion Header Row (Entire row clickable to toggle)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedGuidelineIds[res.id] = !isExpanded }
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CyanPrimary.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${res.issuingBody} • ${res.year}",
                                        color = CyanPrimary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                DomainChip(domain = res.category)
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (isExpanded) EmeraldSuccess.copy(alpha = 0.15f) else DeepNavyBg)
                                        .padding(horizontal = 5.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (isExpanded) "Expanded" else "Collapsed",
                                        color = if (isExpanded) EmeraldSuccess else TextMuted,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = res.title,
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 19.sp
                            )
                        }

                        // Animated Accordion Chevron Toggle
                        IconButton(
                            onClick = { expandedGuidelineIds[res.id] = !isExpanded },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = if (isExpanded) "Collapse summary" else "Expand summary",
                                tint = if (isExpanded) CyanPrimary else TextSecondary,
                                modifier = Modifier
                                    .size(24.dp)
                                    .rotate(chevronRotation)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Citation and Online Chip Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = res.citation,
                            color = TealAccent,
                            fontSize = 11.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CyanPrimary.copy(alpha = 0.15f))
                                .clickable {
                                    try {
                                        uriHandler.openUri(res.getOnlineUrl())
                                    } catch (e: Exception) {
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(res.getOnlineUrl()))
                                            context.startActivity(intent)
                                        } catch (_: Exception) {}
                                    }
                                }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                .testTag("online_chip_${res.id}")
                        ) {
                            Text(
                                text = "Online ↗",
                                color = CyanPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Collapsed state: Brief summary preview + tap to expand cue
                    if (!isExpanded) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = res.summary,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { expandedGuidelineIds[res.id] = true }
                                .padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = "Tap to expand diagnostic cutoffs, staging & recommendations →",
                                color = CyanPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Expandable Accordion High-Yield Details with smooth transition
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            // Section 0: Full Summary
                            SummaryBlock(
                                title = "EXECUTIVE CLINICAL DIGEST",
                                content = res.summary,
                                accentColor = CyanPrimary,
                                icon = Icons.Default.MenuBook
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Section 1: Key Cutoffs & Diagnostic Thresholds
                            SummaryBlock(
                                title = "KEY DIAGNOSTIC CUTOFFS & BIOMARKERS",
                                content = res.keyCutoffs,
                                accentColor = TealAccent,
                                icon = Icons.Default.Verified
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Section 2: Staging Criteria & Classification
                            SummaryBlock(
                                title = "STAGING SYSTEMS & RISK STRATIFICATION",
                                content = res.stagingCriteria,
                                accentColor = CyanPrimary,
                                icon = Icons.Default.LocalHospital
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Section 3: Core Recommendations
                            SummaryBlock(
                                title = "CORE CLINICAL RECOMMENDATIONS (GRADE 1A/1B)",
                                content = res.recommendations,
                                accentColor = EmeraldSuccess,
                                icon = Icons.Default.CheckCircle
                            )

                            // Section 4: Exam Pitfalls & Pearls
                            val pitfalls = getGuidelineExamPitfall(res.id)
                            if (pitfalls != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                SummaryBlock(
                                    title = "EXAM TRAPS & HIGH-YIELD CLINICAL PEARLS",
                                    content = pitfalls,
                                    accentColor = AmberWarning,
                                    icon = Icons.Default.Warning
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Full Guideline Online Link
                            OutlinedButton(
                                onClick = {
                                    try {
                                        uriHandler.openUri(res.getOnlineUrl())
                                    } catch (e: Exception) {
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(res.getOnlineUrl()))
                                            context.startActivity(intent)
                                        } catch (_: Exception) {}
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.6f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("open_online_guideline_${res.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                    contentDescription = null,
                                    tint = CyanPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Read Full Guideline Online (${res.issuingBody})",
                                    color = CyanPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Action buttons: Practice questions & Spaced review on this guideline
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onPracticeGuideline(res.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(imageVector = Icons.Default.Quiz, contentDescription = null, tint = DeepNavyBg, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (resQuestions.isNotEmpty()) "Practice (${resQuestions.size})" else "Practice",
                                        color = DeepNavyBg,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }

                                OutlinedButton(
                                    onClick = { onRecallGuideline(res.id) },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(imageVector = Icons.Default.Psychology, contentDescription = null, tint = TealAccent, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (resCards.isNotEmpty()) "Recall (${resCards.size})" else "Recall Deck",
                                        color = TealAccent,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun SummaryBlock(
    title: String,
    content: String,
    accentColor: androidx.compose.ui.graphics.Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(LabTableBg)
            .border(1.dp, LabBorder, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    color = accentColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = content,
                color = TextPrimary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }
    }
}

fun getGuidelineExamPitfall(resourceId: String): String? {
    return when (resourceId) {
        "bsh_amyloidosis_2026" -> """
            • EXAM PITFALL 1: Never proceed directly to an invasive kidney biopsy in a suspected AL amyloidosis patient if a fat pad or bone marrow biopsy can demonstrate Congo red positivity (Grade 2B).
            • EXAM PITFALL 2: Patients can progress to AL amyloidosis with minimal or zero increase in clonal paraprotein/light chains. Watch out for unexplained falling albumin or rising ALP!
            • EXAM PITFALL 3: In the AL-ISS 2026 staging, Stage IIIC defines ultra-poor survival (~4 months) requiring NT-proBNP >8500 ng/L, Troponin T >=0.035 ng/L, AND abnormal GLS >-9%.
        """.trimIndent()
        "imwg_myeloma_2021" -> """
            • EXAM PITFALL 1: Plain skeletal survey misses ~30% of lytic lesions and is now superseded by Whole-Body Low-Dose CT (WBLDCT).
            • EXAM PITFALL 2: Bortezomib requires NO dose adjustment in acute renal failure, making it ideal for cast nephropathy induction.
            • EXAM PITFALL 3: The SLiM biomarkers define myeloma requiring treatment even in the absence of traditional CRAB symptoms.
        """.trimIndent()
        "bsh_transfusion_components" -> """
            • EXAM PITFALL 1: Purine analogue treated patients (Fludarabine, Cladribine, Pentostatin) require LIFELONG irradiated cellular components.
            • EXAM PITFALL 2: In acute life-threatening Warfarin bleeding, use 4-factor Prothrombin Complex Concentrate (PCC 25-50 IU/kg) + IV Vitamin K; FFP takes too long and causes TACO.
            • EXAM PITFALL 3: Prophylactic platelet transfusions are contraindicated in TTP and HIT unless life-threatening hemorrhage is present.
        """.trimIndent()
        "ukhcdo_haemostasis_2022" -> """
            • EXAM PITFALL 1: In suspected HIT (4Ts >=4), stop all heparin immediately and switch to non-heparin anticoagulation (Argatroban, Fondaparinux, Danaparoid). Never give Warfarin acutely until platelets >150x10^9/L!
            • EXAM PITFALL 2: Acquired Haemophilia A has an isolated prolonged APTT that shows time- and temperature-dependent prolongation after 2-hour incubation at 37°C.
        """.trimIndent()
        "bsh_iron_haemochromatosis" -> """
            • EXAM PITFALL 1: Optimal induction venesection is assessed by serum ferritin (<50 mcg/L), NOT transferrin saturation (which fluctuates with meals).
            • EXAM PITFALL 2: Check Hb before every venesection and defer if Hb <110 g/L.
        """.trimIndent()
        "bsh_sickle_transfusion" -> """
            • EXAM PITFALL 1: In sickle cell pre-op, never transfuse Hb above 100-110 g/L (hyperviscosity crisis risk). Target Hb S is <30%.
            • EXAM PITFALL 2: Extended Rh (D, C, c, E, e) and Kell (K) phenotyping is mandatory to prevent dangerous alloimmunization and delayed hemolytic reactions.
        """.trimIndent()
        "bsh_apl_guidelines" -> """
            • EXAM PITFALL 1: Start ATRA immediately upon morphological suspicion of APL; do NOT wait for molecular confirmation of PML-RARA.
            • EXAM PITFALL 2: Differentiation syndrome (weight gain, dyspnea, pleural effusions) requires immediate high-dose IV Dexamethasone 10 mg BD.
        """.trimIndent()
        "bsh_antiphospholipid" -> """
            • EXAM PITFALL 1: DOACs are contraindicated in triple-positive Antiphospholipid Syndrome due to increased arterial thrombotic risk; Warfarin remains first-line.
        """.trimIndent()
        "bsh_ttp_guidelines" -> """
            • EXAM PITFALL 1: Never transfuse platelets in TTP unless life-threatening intracranial hemorrhage. Commence Therapeutic Plasma Exchange with SD-FFP urgently.
        """.trimIndent()
        else -> null
    }
}

package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.GeneratedGuidelinePackage
import com.example.data.model.ApprovedResource
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
fun ResourceRegistryScreen(
    resources: List<ApprovedResource>,
    isIngestDialogOpen: Boolean,
    errorMessage: String?,
    successMessage: String?,
    isAiAnalyzingPdf: Boolean = false,
    isAiReviewDialogOpen: Boolean = false,
    aiGeneratedPackage: GeneratedGuidelinePackage? = null,
    onOpenIngestDialog: () -> Unit,
    onCloseIngestDialog: () -> Unit,
    onIngestGuideline: (String, String, Int, String, String, String, String, String, String, String) -> Unit,
    onUploadPdf: (Uri) -> Unit = {},
    onLoadSamplePdf: () -> Unit = {},
    onLoadPresetGuideline: (String) -> Unit = {},
    onUpdateAiPackage: (GeneratedGuidelinePackage) -> Unit = {},
    onCommitAiPackage: () -> Unit = {},
    onDismissAiReview: () -> Unit = {},
    onOpenResourceModal: (ApprovedResource) -> Unit,
    onDismissMessage: () -> Unit,
    onBackToDashboard: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    // PDF file picker launcher
    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onUploadPdf(it) }
    }

    val filtered = resources.filter { res ->
        val matchesCategory = selectedCategory == "All" || res.category == selectedCategory
        val matchesSearch = searchQuery.isBlank() ||
                res.title.contains(searchQuery, ignoreCase = true) ||
                res.issuingBody.contains(searchQuery, ignoreCase = true) ||
                res.summary.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DeepNavyBg)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            // Navigation Header
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
                        text = "APPROVED EVIDENCE REGISTRY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanPrimary,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Clinical Guidelines Database",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                // Ingest Options Menu
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Upload PDF button
                    Button(
                        onClick = { pdfPickerLauncher.launch("application/pdf") },
                        colors = ButtonDefaults.buttonColors(containerColor = TealAccent),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("upload_guideline_pdf_header_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.UploadFile,
                            contentDescription = null,
                            tint = DeepNavyBg,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "IMPORT (BETA)",
                            color = DeepNavyBg,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    // Manual Ingest button
                    Button(
                        onClick = onOpenIngestDialog,
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("ingest_new_guideline_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = DeepNavyBg,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Manual",
                            color = DeepNavyBg,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // AI PDF Ingestion Spotlight Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(CardElevatedBg)
                    .border(1.dp, TealAccent.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = TealAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "GUIDELINE IMPORT (EXPERIMENTAL)",
                            color = TealAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Recognised bundled guidelines (Garg, Biss, Tam, MacDonald, Osborne 2026) load their editor-verified summaries, questions and flashcards. Any other file is added as an UNVERIFIED placeholder with no generated content: enter cutoffs and recommendations yourself from the source document. No AI model is used and PDF text is not extracted on-device.",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { pdfPickerLauncher.launch("application/pdf") },
                            colors = ButtonDefaults.buttonColors(containerColor = TealAccent),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .weight(1.3f)
                                .testTag("select_pdf_btn")
                        ) {
                            Icon(imageVector = Icons.Default.UploadFile, contentDescription = null, tint = DeepNavyBg, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Select PDF File", color = DeepNavyBg, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = onLoadSamplePdf,
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .weight(1.2f)
                                .testTag("test_sample_pdf_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Test BSH PDF", color = CyanPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "LOAD A BUNDLED 2026 GUIDELINE:",
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onLoadPresetGuideline("biss") },
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.weight(1f).testTag("test_biss_btn")
                        ) {
                            Text("Biss 2026\n(Gynae)", fontSize = 10.sp, color = TealAccent, textAlign = androidx.compose.ui.text.style.TextAlign.Center, lineHeight = 12.sp)
                        }
                        OutlinedButton(
                            onClick = { onLoadPresetGuideline("tam") },
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.weight(1f).testTag("test_tam_btn")
                        ) {
                            Text("Tam 2026\n(POCT)", fontSize = 10.sp, color = CyanPrimary, textAlign = androidx.compose.ui.text.style.TextAlign.Center, lineHeight = 12.sp)
                        }
                        OutlinedButton(
                            onClick = { onLoadPresetGuideline("macdonald") },
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.weight(1f).testTag("test_macdonald_btn")
                        ) {
                            Text("MacDonald\n(TTP)", fontSize = 10.sp, color = EmeraldSuccess, textAlign = androidx.compose.ui.text.style.TextAlign.Center, lineHeight = 12.sp)
                        }
                        OutlinedButton(
                            onClick = { onLoadPresetGuideline("osborne") },
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.weight(1f).testTag("test_osborne_btn")
                        ) {
                            Text("Osborne\n(PMBCL)", fontSize = 10.sp, color = AmberWarning, textAlign = androidx.compose.ui.text.style.TextAlign.Center, lineHeight = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sandbox info banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DeepNavyBg)
                    .border(1.dp, CyanPrimary.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = CyanPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "STRICT EVIDENCE GROUNDING SANDBOX",
                            color = CyanPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "All mock questions and flashcards are strictly validated against guidelines registered in this database.",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
            }

            // Success or error alert banners
            if (successMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(EmeraldSuccess.copy(alpha = 0.15f))
                        .border(1.dp, EmeraldSuccess.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = successMessage,
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

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CrimsonError.copy(alpha = 0.15f))
                        .border(1.dp, CrimsonError.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = errorMessage,
                            color = CrimsonError,
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
                                tint = CrimsonError,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search approved guidelines (BSH, WHO, Amyloidosis)...", color = TextMuted, fontSize = 12.sp) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("registry_search_input"),
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

            Spacer(modifier = Modifier.height(14.dp))

            // Resource List Count
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${filtered.size} REGISTERED GUIDELINES",
                    color = TealAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "Tap to inspect cutoffs & staging",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Resource Cards
            filtered.forEach { res ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = SlateNavySurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .border(1.dp, CardBorderStroke, RoundedCornerShape(12.dp))
                        .clickable { onOpenResourceModal(res) }
                        .testTag("registry_item_${res.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CyanPrimary.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${res.issuingBody} • ${res.year}",
                                    color = CyanPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(TealAccent.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = res.category,
                                    color = TealAccent,
                                    fontSize = 10.sp,
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
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

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
                                    .testTag("registry_online_chip_${res.id}")
                            ) {
                                Text(
                                    text = "Online ↗",
                                    color = CyanPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = res.summary,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (res.isVerified) Icons.Default.Verified else Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = if (res.isVerified) EmeraldSuccess else AmberWarning,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (res.isVerified) "Editor verified" else "Unverified (user-supplied)",
                                    color = if (res.isVerified) EmeraldSuccess else AmberWarning,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                text = "Inspect Cutoffs & Staging →",
                                color = TealAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // Ingestion Dialog (Manual Pathway)
    if (isIngestDialogOpen) {
        IngestGuidelineDialog(
            errorMessage = errorMessage,
            onDismiss = onCloseIngestDialog,
            onSubmit = onIngestGuideline,
            onTriggerPdfPicker = {
                pdfPickerLauncher.launch("application/pdf")
            }
        )
    }

    // AI Analyzing Progress Dialog
    if (isAiAnalyzingPdf) {
        AlertDialog(
            onDismissRequest = {},
            containerColor = SlateNavySurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = CyanPrimary,
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Reading document…",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Extracting diagnostic algorithms, staging systems, and Grade 1A/1B recommendations...",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Synthesizing FRCPath mock questions and SM-2 adaptive flashcards grounded in this document.",
                        color = TealAccent,
                        fontSize = 11.sp
                    )
                }
            },
            confirmButton = {}
        )
    }

    // AI Guideline Review & Confirmation Dialog
    if (isAiReviewDialogOpen && aiGeneratedPackage != null) {
        AiGuidelineReviewDialog(
            pkg = aiGeneratedPackage,
            onDismiss = onDismissAiReview,
            onCommit = onCommitAiPackage
        )
    }
}

@Composable
fun AiGuidelineReviewDialog(
    pkg: GeneratedGuidelinePackage,
    onDismiss: () -> Unit,
    onCommit: () -> Unit
) {
    val res = pkg.resource

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SlateNavySurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = TealAccent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = if (pkg.matchedVerifiedGuideline) "Verified guideline matched" else "Unverified placeholder: review before saving",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Read ${pkg.extractedWordCount} words • ${pkg.flashcards.size} verified cards & ${pkg.questions.size} verified questions",
                        color = EmeraldSuccess,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
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
                    text = "Review the synthesized clinical guideline, mock questions, and flashcards before committing into the Database Sandbox.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Guideline Meta Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CardElevatedBg)
                        .border(1.dp, CyanPrimary.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Column {
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
                            Text(text = res.category, color = TealAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = res.title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = res.citation, color = TextSecondary, fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Diagnostic Cutoffs Summary Preview
                Text(text = "KEY CUTOFFS & BIOMARKERS", color = TealAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(LabTableBg)
                        .border(1.dp, LabBorder, RoundedCornerShape(6.dp))
                        .padding(8.dp)
                ) {
                    Text(text = res.keyCutoffs, color = TextPrimary, fontSize = 11.sp, lineHeight = 15.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Staging Criteria Preview
                Text(text = "STAGING CRITERIA", color = CyanPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(LabTableBg)
                        .border(1.dp, LabBorder, RoundedCornerShape(6.dp))
                        .padding(8.dp)
                ) {
                    Text(text = res.stagingCriteria, color = TextPrimary, fontSize = 11.sp, lineHeight = 15.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Generated Spaced Repetition Flashcards count badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(DeepNavyBg)
                        .border(1.dp, EmeraldSuccess.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Psychology, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "${pkg.flashcards.size} SM-2 Flashcards Created", color = EmeraldSuccess, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Initialized with interval 1d, ease factor 2.5 for immediate spaced recall.", color = TextSecondary, fontSize = 10.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Generated Mock Questions count badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(DeepNavyBg)
                        .border(1.dp, CyanPrimary.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Quiz, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "${pkg.questions.size} FRCPath Mock Questions Created", color = CyanPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Strictly grounded in this guideline's diagnostic algorithms and recommendations.", color = TextSecondary, fontSize = 10.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onCommit,
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                modifier = Modifier.testTag("commit_ai_package_btn")
            ) {
                Text(text = "Approve & Ingest into Sandbox", color = DeepNavyBg, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(text = "Discard", color = TextSecondary)
            }
        }
    )
}

@Composable
fun IngestGuidelineDialog(
    errorMessage: String?,
    onDismiss: () -> Unit,
    onSubmit: (String, String, Int, String, String, String, String, String, String, String) -> Unit,
    onTriggerPdfPicker: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var issuingBody by remember { mutableStateOf("British Society for Haematology") }
    var yearStr by remember { mutableStateOf("2026") }
    var citation by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Malignant Haematology") }
    var summary by remember { mutableStateOf("") }
    var keyCutoffs by remember { mutableStateOf("") }
    var stagingCriteria by remember { mutableStateOf("") }
    var recommendations by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SlateNavySurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = CyanPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Ingest Approved Guideline",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // PDF Upload shortcut inside dialog
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CardElevatedBg)
                        .border(1.dp, TealAccent.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .clickable { onTriggerPdfPicker() }
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.UploadFile, contentDescription = null, tint = TealAccent, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "Prefer uploading a PDF file?", color = TealAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Bundled guidelines are recognised by title/author; other files become unverified placeholders.", color = TextSecondary, fontSize = 10.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Or enter accredited clinical guideline parameters manually below:",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage, color = CrimsonError, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                DialogInputField(label = "Guideline Title", value = title, onValueChange = { title = it }, placeholder = "e.g. BSH Early Diagnosis of AL Amyloidosis")
                Spacer(modifier = Modifier.height(8.dp))
                DialogInputField(label = "Issuing Body", value = issuingBody, onValueChange = { issuingBody = it }, placeholder = "e.g. British Society for Haematology")
                Spacer(modifier = Modifier.height(8.dp))
                DialogInputField(label = "Publication Year", value = yearStr, onValueChange = { yearStr = it }, placeholder = "e.g. 2026")
                Spacer(modifier = Modifier.height(8.dp))
                DialogInputField(label = "Full Citation", value = citation, onValueChange = { citation = it }, placeholder = "Authors, Journal, Volume, Pages, Year")
                Spacer(modifier = Modifier.height(8.dp))
                DialogInputField(label = "Online Guideline Link / DOI", value = url, onValueChange = { url = it }, placeholder = "e.g. https://doi.org/10.1111/bjh.70472")
                Spacer(modifier = Modifier.height(8.dp))
                DialogInputField(label = "Executive Summary", value = summary, onValueChange = { summary = it }, placeholder = "Brief clinical summary of the guideline scope", maxLines = 3)
                Spacer(modifier = Modifier.height(8.dp))
                DialogInputField(label = "Key Diagnostic Cutoffs", value = keyCutoffs, onValueChange = { keyCutoffs = it }, placeholder = "e.g. NT-proBNP >332 ng/L, urine PCR >50 mg/mmol", maxLines = 3)
                Spacer(modifier = Modifier.height(8.dp))
                DialogInputField(label = "Staging Criteria", value = stagingCriteria, onValueChange = { stagingCriteria = it }, placeholder = "e.g. AL-ISS Stage I to IIIC", maxLines = 3)
                Spacer(modifier = Modifier.height(8.dp))
                DialogInputField(label = "Core Recommendations (Grade 1A/1B)", value = recommendations, onValueChange = { recommendations = it }, placeholder = "Grade 1A/1B clinical recommendations", maxLines = 3)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val year = yearStr.toIntOrNull() ?: 2026
                    onSubmit(title, issuingBody, year, citation, category, summary, keyCutoffs, stagingCriteria, recommendations, url)
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                modifier = Modifier.testTag("confirm_ingest_btn")
            ) {
                Text(text = "Ingest into Sandbox", color = DeepNavyBg, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(text = "Cancel", color = TextSecondary)
            }
        }
    )
}

@Composable
fun DialogInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    maxLines: Int = 1
) {
    Column {
        Text(text = label, color = TealAccent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(2.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = TextMuted, fontSize = 12.sp) },
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
            maxLines = maxLines
        )
    }
}

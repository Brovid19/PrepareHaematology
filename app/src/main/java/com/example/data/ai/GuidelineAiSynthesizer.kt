package com.example.data.ai

import android.content.Context
import android.net.Uri
import com.example.data.db.SeedData
import com.example.data.model.ApprovedResource
import com.example.data.model.Flashcard
import com.example.data.model.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Calendar

data class GeneratedGuidelinePackage(
    val resource: ApprovedResource,
    val questions: List<Question>,
    val flashcards: List<Flashcard>,
    val extractedWordCount: Int,
    val summarySnippet: String,
    /** True when the document matched an editor-verified guideline already in [SeedData]. */
    val matchedVerifiedGuideline: Boolean = false
)

/**
 * Guideline import (EXPERIMENTAL).
 *
 * This is NOT a language model. It does two honest things only:
 *  1. If the document clearly matches one of the editor-verified guidelines bundled in
 *     [SeedData], it returns that verified resource with its verified questions and flashcards
 *     (same IDs, so importing again never creates duplicates).
 *  2. Otherwise it creates an UNVERIFIED placeholder resource with no generated study items,
 *     so the user can fill in content from the source document themselves.
 *
 * It never invents cutoffs, recommendations, evidence grades, citations or questions.
 */
object GuidelineAiSynthesizer {

    const val NOT_EXTRACTED =
        "Not extracted. Open the source document and enter verified content before studying from this entry."

    /**
     * Reads plain-text documents. PDFs are binary and cannot be parsed on-device without a PDF
     * library, so for a PDF an empty string is returned and matching relies on the file name.
     */
    suspend fun extractTextFromUri(context: Context, uri: Uri): String = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val buffered = inputStream.buffered()
                buffered.mark(8)
                val header = ByteArray(5)
                val read = buffered.read(header)
                buffered.reset()
                if (read == 5 && String(header, Charsets.US_ASCII) == "%PDF-") {
                    return@withContext ""
                }
                val reader = BufferedReader(InputStreamReader(buffered, Charsets.UTF_8))
                reader.lineSequence()
                    .map { it.replace(Regex("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F]"), " ").trim() }
                    .filter { it.length > 2 }
                    .take(2000)
                    .joinToString("\n")
            } ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    /** Word-boundary match, so e.g. "tam" does not match "vitamin" and "hit" does not match "white". */
    private fun hasWord(haystack: String, word: String): Boolean =
        Regex("\\b" + Regex.escape(word) + "\\b").containsMatchIn(haystack)

    /**
     * Identifies one of the verified bundled guidelines from distinctive title phrases or
     * first-author surnames. Returns null when there is no confident match.
     */
    internal fun matchVerifiedGuidelineId(rawText: String, sourceName: String): String? {
        val haystack = (rawText + " " + sourceName).replace('_', ' ').replace('-', ' ').lowercase()
        val compact = haystack.replace(Regex("\\s+"), " ")
        return when {
            compact.contains("haematology and gynaecology") ||
                (hasWord(compact, "biss") && hasWord(compact, "gynaecology")) -> "bsh_haem_gynae_2026"
            compact.contains("point of care testing in general haematology") ||
                (hasWord(compact, "tam") && (compact.contains("point of care") || hasWord(compact, "poct"))) -> "bsh_poct_haematology_2026"
            (compact.contains("thrombotic thrombocytopenic purpura") && compact.contains("laborator")) ||
                (hasWord(compact, "macdonald") && (hasWord(compact, "ttp") || hasWord(compact, "adamts13"))) -> "bsh_ttp_laboratory_2026"
            compact.contains("primary mediastinal b cell lymphoma") ||
                (hasWord(compact, "osborne") && hasWord(compact, "pmbcl")) -> "bsh_pmbcl_lymphoma_2026"
            compact.contains("early diagnosis of al amyloidosis") ||
                (hasWord(compact, "garg") && compact.contains("amyloid")) -> "bsh_amyloidosis_2026"
            else -> null
        }
    }

    private fun detectIssuingBody(text: String): String = when {
        text.contains("haemophilia centre doctors") || hasWord(text, "ukhcdo") -> "UKHCDO"
        text.contains("british society for haematology") || hasWord(text, "bsh") -> "British Society for Haematology"
        text.contains("international myeloma working group") || hasWord(text, "imwg") -> "International Myeloma Working Group"
        text.contains("world health organization") -> "World Health Organization"
        text.contains("national institute for health and care excellence") -> "NICE"
        hasWord(text, "ebmt") -> "EBMT"
        else -> "Unknown issuing body"
    }

    private fun detectCategory(text: String): String = when {
        listOf("lymphoma", "myeloma", "amyloid", "leukaemia", "leukemia", "myelodysplastic").any { text.contains(it) } ->
            "Malignant Haematology"
        listOf("transfusion", "blood component", "irradiated").any { text.contains(it) } || hasWord(text, "taco") || hasWord(text, "trali") ->
            "Transfusion Medicine"
        listOf("haemostasis", "thrombosis", "anticoagula", "coagulation", "adamts13", "heparin").any { text.contains(it) } ->
            "Haemostasis & Thrombosis"
        else -> "General Haematology"
    }

    suspend fun synthesizeFromGuideline(
        rawText: String,
        sourceName: String = "Uploaded Guideline"
    ): GeneratedGuidelinePackage = withContext(Dispatchers.Default) {
        val words = rawText.split(Regex("\\s+")).count { it.isNotBlank() }
        val snippet = if (rawText.isBlank()) {
            "No text could be extracted from this file (PDFs are not parsed on-device)."
        } else {
            rawText.take(180).trim().replace("\n", " ") + "..."
        }

        val matchedId = matchVerifiedGuidelineId(rawText, sourceName)
        val verified = matchedId?.let { id -> SeedData.getApprovedResources().firstOrNull { it.id == id } }

        if (verified != null) {
            return@withContext GeneratedGuidelinePackage(
                resource = verified,
                questions = SeedData.getQuestions().filter { it.sourceResourceId == verified.id },
                flashcards = SeedData.getFlashcards().filter { it.sourceResourceId == verified.id },
                extractedWordCount = words,
                summarySnippet = snippet,
                matchedVerifiedGuideline = true
            )
        }

        // Unknown document: create an honest, unverified placeholder only.
        val lower = rawText.lowercase()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val year = Regex("\\b(19[89]\\d|20[0-4]\\d)\\b").find(rawText)?.value?.toIntOrNull()
            ?.takeIf { it <= currentYear } ?: currentYear
        val cleanName = sourceName
            .substringAfterLast('/')
            .replace(Regex("\\.pdf$", RegexOption.IGNORE_CASE), "")
            .replace('_', ' ')
            .trim()
        val title = cleanName.ifBlank { "Uploaded guideline ($year)" }
        val slug = title.lowercase().replace(Regex("[^a-z0-9]+"), "_").trim('_').take(24)

        val resource = ApprovedResource(
            id = "ingested_${slug}_$year",
            title = title,
            issuingBody = detectIssuingBody(lower),
            year = year,
            citation = "Unverified upload: $title",
            category = detectCategory(lower),
            summary = "User-uploaded document. Its content has NOT been extracted or checked by an editor.",
            keyCutoffs = NOT_EXTRACTED,
            stagingCriteria = NOT_EXTRACTED,
            recommendations = NOT_EXTRACTED,
            url = "",
            isVerified = false
        )

        GeneratedGuidelinePackage(
            resource = resource,
            questions = emptyList(),
            flashcards = emptyList(),
            extractedWordCount = words,
            summarySnippet = snippet,
            matchedVerifiedGuideline = false
        )
    }
}

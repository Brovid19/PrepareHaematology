package com.example.audio

import com.example.data.model.ApprovedResource
import com.example.data.model.Flashcard

object TtsTextFormatter {

    fun formatSummaryForSpeech(res: ApprovedResource): String {
        val cleanSummary = cleanMarkdownAndSymbols(res.summary)
        val cleanCutoffs = cleanMarkdownAndSymbols(res.keyCutoffs)
        val cleanStaging = cleanMarkdownAndSymbols(res.stagingCriteria)
        val cleanRecs = cleanMarkdownAndSymbols(res.recommendations)

        return buildString {
            append("Summary for ")
            append(res.title)
            append(". Issued by ")
            append(res.issuingBody)
            append(", ")
            append(res.year)
            append(". Category: ")
            append(res.category)
            append(". Executive Summary: ")
            append(cleanSummary)
            if (cleanCutoffs.isNotBlank()) {
                append(". Key Diagnostic Cutoffs and Thresholds: ")
                append(cleanCutoffs)
            }
            if (cleanStaging.isNotBlank()) {
                append(". Staging and Classification Criteria: ")
                append(cleanStaging)
            }
            if (cleanRecs.isNotBlank()) {
                append(". Core Evidence-Based Recommendations: ")
                append(cleanRecs)
            }
        }
    }

    fun formatFlashcardForSpeech(card: Flashcard, res: ApprovedResource?): String {
        val cleanFront = cleanMarkdownAndSymbols(card.front)
        val cleanBack = cleanMarkdownAndSymbols(card.back)
        return buildString {
            append("Haematology flashcard. ")
            if (card.guidelineRef.isNotBlank()) {
                append("Reference: ")
                append(card.guidelineRef)
                append(". ")
            } else if (res != null) {
                append("Guideline: ")
                append(res.title)
                append(". ")
            }
            append("Question: ")
            append(cleanFront)
            append(". ... Answer: ")
            append(cleanBack)
        }
    }

    private fun cleanMarkdownAndSymbols(text: String): String {
        return text
            .replace("#", "")
            .replace("*", "")
            .replace("•", ". ")
            .replace("->", " leads to ")
            .replace("≥", " greater than or equal to ")
            .replace("≤", " less than or equal to ")
            .replace(">", " greater than ")
            .replace("<", " less than ")
            .replace("\n\n", ". ")
            .replace("\n", " ")
            .replace("\\s+".toRegex(), " ")
            .trim()
    }
}

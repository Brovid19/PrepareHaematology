package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = ApprovedResource::class,
            parentColumns = ["id"],
            childColumns = ["sourceResourceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["sourceResourceId"]), Index(value = ["domain"])]
)
data class Question(
    @PrimaryKey
    val id: String,
    val domain: String, // "Haemato-Oncology", "Haemostasis & Thrombosis", "Transfusion Medicine", "General Haematology"
    val questionType: String, // "MCQ", "EMQ", "ESSAY"
    val title: String,
    val stem: String,
    val labData: String? = null,
    val options: List<String> = emptyList(),
    val correctIndex: Int, // 0..4 for MCQ, 0..9 for EMQ, -1 for Essay
    val rationale: String,
    val sourceResourceId: String, // FK to ApprovedResource (Strict Evidence Sandbox)
    val guidelineSection: String, // Section or table reference in the guideline
    val evidenceGrade: String, // "Grade 1A", "Grade 1B", "Consensus", etc.
    val yearPaper: String? = null, // e.g., "RCPath Paper 1 2019", "RCPath Sample EMQ"
    val essayModelAnswer: String? = null, // Model answer for multi-part essay
    val markingRubric: String? = null // Specific marks allocation
)

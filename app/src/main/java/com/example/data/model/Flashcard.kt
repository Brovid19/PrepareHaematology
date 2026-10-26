package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "flashcards",
    foreignKeys = [
        ForeignKey(
            entity = ApprovedResource::class,
            parentColumns = ["id"],
            childColumns = ["sourceResourceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["sourceResourceId"]), Index(value = ["dueDateTimestamp"])]
)
data class Flashcard(
    @PrimaryKey
    val id: String,
    val domain: String,
    val topic: String,
    val sourceResourceId: String, // FK to ApprovedResource (Evidence Sandbox)
    val front: String, // Question / clinical recall trigger
    val back: String, // High-yield answer, key guideline cutoffs
    val guidelineRef: String,
    val intervalDays: Int = 1,
    val repetitionCount: Int = 0,
    val easeFactor: Float = 2.5f,
    val dueDateTimestamp: Long = System.currentTimeMillis(),
    val lastReviewedTimestamp: Long = 0L
)

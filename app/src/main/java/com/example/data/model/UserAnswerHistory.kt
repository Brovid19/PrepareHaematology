package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_answers")
data class UserAnswerHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val questionId: String,
    val selectedOptionIndex: Int,
    val isCorrect: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = "",
    val isBookmarked: Boolean = false
)

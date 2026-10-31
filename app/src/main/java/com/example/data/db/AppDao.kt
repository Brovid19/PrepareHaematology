package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.data.model.ApprovedResource
import com.example.data.model.Flashcard
import com.example.data.model.Question
import com.example.data.model.UserAnswerHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface ApprovedResourceDao {
    @Query("SELECT * FROM approved_resources ORDER BY year DESC, title ASC")
    fun getAllResources(): Flow<List<ApprovedResource>>

    @Query("SELECT * FROM approved_resources WHERE id = :id LIMIT 1")
    suspend fun getResourceById(id: String): ApprovedResource?

    @Query("SELECT * FROM approved_resources WHERE category = :category")
    fun getResourcesByCategory(category: String): Flow<List<ApprovedResource>>

    // Upsert updates rows in place. REPLACE would DELETE then INSERT, which fires the
    // ON DELETE CASCADE foreign keys and silently wipes linked questions and flashcards
    // (including the learner's spaced-repetition progress).
    @Upsert
    suspend fun insertResource(resource: ApprovedResource)

    @Upsert
    suspend fun insertResources(resources: List<ApprovedResource>)
}

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions ORDER BY domain, id")
    fun getAllQuestions(): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE domain = :domain")
    fun getQuestionsByDomain(domain: String): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE questionType = :type")
    fun getQuestionsByType(type: String): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE sourceResourceId = :resourceId")
    fun getQuestionsByResource(resourceId: String): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    suspend fun getQuestionById(id: String): Question?

    @Upsert
    suspend fun insertQuestions(questions: List<Question>)

    @Upsert
    suspend fun insertQuestion(question: Question)
}

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcards ORDER BY dueDateTimestamp ASC")
    fun getAllFlashcards(): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards WHERE dueDateTimestamp <= :currentTimestamp ORDER BY dueDateTimestamp ASC")
    fun getDueFlashcards(currentTimestamp: Long): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards WHERE domain = :domain ORDER BY dueDateTimestamp ASC")
    fun getFlashcardsByDomain(domain: String): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards WHERE sourceResourceId = :resourceId")
    fun getFlashcardsByResource(resourceId: String): Flow<List<Flashcard>>

    @Update
    suspend fun updateFlashcard(flashcard: Flashcard)

    @Upsert
    suspend fun insertFlashcard(flashcard: Flashcard)

    @Upsert
    suspend fun insertFlashcards(flashcards: List<Flashcard>)

    @Query("SELECT * FROM flashcards WHERE id IN (:ids)")
    suspend fun getFlashcardsByIds(ids: List<String>): List<Flashcard>
}

@Dao
interface UserAnswerDao {
    @Query("SELECT * FROM user_answers ORDER BY timestamp DESC")
    fun getAllAnswers(): Flow<List<UserAnswerHistory>>

    @Query("SELECT * FROM user_answers WHERE questionId = :questionId ORDER BY timestamp DESC")
    fun getAnswersForQuestion(questionId: String): Flow<List<UserAnswerHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answer: UserAnswerHistory)

    @Query("SELECT DISTINCT questionId FROM user_answers WHERE isBookmarked = 1")
    fun getBookmarkedQuestionIds(): Flow<List<String>>

    @Query("UPDATE user_answers SET isBookmarked = :isBookmarked WHERE questionId = :questionId")
    suspend fun setBookmark(questionId: String, isBookmarked: Boolean)

    // A question is a weak spot only while its MOST RECENT attempt was incorrect,
    // so answering it correctly later clears it.
    @Query(
        """
        SELECT u.questionId FROM user_answers u
        WHERE u.timestamp = (SELECT MAX(t.timestamp) FROM user_answers t WHERE t.questionId = u.questionId)
          AND u.isCorrect = 0
        GROUP BY u.questionId
        ORDER BY MAX(u.timestamp) DESC
        """
    )
    fun getIncorrectQuestionIds(): Flow<List<String>>
}

package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.ApprovedResource
import com.example.data.model.Flashcard
import com.example.data.model.Question
import com.example.data.model.UserAnswerHistory

@Database(
    entities = [
        ApprovedResource::class,
        Question::class,
        Flashcard::class,
        UserAnswerHistory::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun approvedResourceDao(): ApprovedResourceDao
    abstract fun questionDao(): QuestionDao
    abstract fun flashcardDao(): FlashcardDao
    abstract fun userAnswerDao(): UserAnswerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "frcpath_haematology.db"
                )
                    // WARNING: destructive migration wipes learner progress. Any future schema
                    // change (bumping `version`) MUST ship with an explicit Migration instead.
                    .fallbackToDestructiveMigration()
                    // Seeding is done in exactly one place: ExamRepository.ensureDataSeeded().
                    // The previous onCreate callback raced with it.
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun seedDatabase(db: AppDatabase) {
            db.approvedResourceDao().insertResources(SeedData.getApprovedResources())
            db.questionDao().insertQuestions(SeedData.getQuestions())
            db.flashcardDao().insertFlashcards(SeedData.getFlashcards())
        }
    }
}

package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.ApprovedResource
import com.example.data.model.Flashcard
import com.example.data.model.Question
import com.example.data.model.UserAnswerHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate database on creation
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    seedDatabase(database)
                                }
                            }
                        }
                    })
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

package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppSettings(
    // Audio & TTS
    val defaultSpeechRate: Float = 1.0f,
    val preferredVoiceLocale: String = "en-GB", // "en-GB" (British English) or "en-US"
    val autoAdvancePlaylist: Boolean = true,
    val pauseBetweenTracksSeconds: Float = 1.0f,
    val readEvidenceGradesAloud: Boolean = true,

    // Spaced Repetition (SM-2)
    val dailyFlashcardGoal: Int = 20,
    val defaultSessionMode: String = "DUE_FOR_REVIEW", // ALL_CARDS, DUE_FOR_REVIEW, DIFFICULT_DRILL
    val defaultEaseFactor: Float = 2.5f,
    val showReferenceOnFront: Boolean = false,

    // Practice & Mock Exam
    val defaultMockDurationMinutes: Int = 20,
    val defaultPracticeDomain: String = "All",
    val instantFeedbackInPractice: Boolean = true,
    val shuffleQuestions: Boolean = true,

    // Theme & Display
    val displayThemeMode: String = "DARK_SOFT_RED", // "DARK_SOFT_RED", "OLED_BLACK", "SYSTEM"
    val summariesDefaultExpansion: String = "EXPAND_FIRST", // "EXPAND_FIRST", "COLLAPSE_ALL", "EXPAND_ALL"
    val textScalePercent: Int = 100 // 100, 115, 130
)

class SettingsManager(context: Context?) {

    private val prefs: SharedPreferences? = context?.getSharedPreferences(
        "prepare_haematology_settings",
        Context.MODE_PRIVATE
    )

    private val _settingsState = MutableStateFlow(loadSettings())
    val settingsState: StateFlow<AppSettings> = _settingsState.asStateFlow()

    private fun loadSettings(): AppSettings {
        if (prefs == null) return AppSettings()
        return AppSettings(
            defaultSpeechRate = prefs.getFloat(KEY_SPEECH_RATE, 1.0f),
            preferredVoiceLocale = prefs.getString(KEY_VOICE_LOCALE, "en-GB") ?: "en-GB",
            autoAdvancePlaylist = prefs.getBoolean(KEY_AUTO_ADVANCE, true),
            pauseBetweenTracksSeconds = prefs.getFloat(KEY_PAUSE_SECONDS, 1.0f),
            readEvidenceGradesAloud = prefs.getBoolean(KEY_READ_EVIDENCE, true),

            dailyFlashcardGoal = prefs.getInt(KEY_DAILY_GOAL, 20),
            defaultSessionMode = prefs.getString(KEY_SESSION_MODE, "DUE_FOR_REVIEW") ?: "DUE_FOR_REVIEW",
            defaultEaseFactor = prefs.getFloat(KEY_DEFAULT_EASE, 2.5f),
            showReferenceOnFront = prefs.getBoolean(KEY_SHOW_REF_FRONT, false),

            defaultMockDurationMinutes = prefs.getInt(KEY_MOCK_DURATION, 20),
            defaultPracticeDomain = prefs.getString(KEY_PRACTICE_DOMAIN, "All") ?: "All",
            instantFeedbackInPractice = prefs.getBoolean(KEY_INSTANT_FEEDBACK, true),
            shuffleQuestions = prefs.getBoolean(KEY_SHUFFLE_QUESTIONS, true),

            displayThemeMode = prefs.getString(KEY_THEME_MODE, "DARK_SOFT_RED") ?: "DARK_SOFT_RED",
            summariesDefaultExpansion = prefs.getString(KEY_SUMMARIES_EXPANSION, "EXPAND_FIRST") ?: "EXPAND_FIRST",
            textScalePercent = prefs.getInt(KEY_TEXT_SCALE, 100)
        )
    }

    fun saveSettings(settings: AppSettings) {
        _settingsState.value = settings
        prefs?.edit()?.apply {
            putFloat(KEY_SPEECH_RATE, settings.defaultSpeechRate)
            putString(KEY_VOICE_LOCALE, settings.preferredVoiceLocale)
            putBoolean(KEY_AUTO_ADVANCE, settings.autoAdvancePlaylist)
            putFloat(KEY_PAUSE_SECONDS, settings.pauseBetweenTracksSeconds)
            putBoolean(KEY_READ_EVIDENCE, settings.readEvidenceGradesAloud)

            putInt(KEY_DAILY_GOAL, settings.dailyFlashcardGoal)
            putString(KEY_SESSION_MODE, settings.defaultSessionMode)
            putFloat(KEY_DEFAULT_EASE, settings.defaultEaseFactor)
            putBoolean(KEY_SHOW_REF_FRONT, settings.showReferenceOnFront)

            putInt(KEY_MOCK_DURATION, settings.defaultMockDurationMinutes)
            putString(KEY_PRACTICE_DOMAIN, settings.defaultPracticeDomain)
            putBoolean(KEY_INSTANT_FEEDBACK, settings.instantFeedbackInPractice)
            putBoolean(KEY_SHUFFLE_QUESTIONS, settings.shuffleQuestions)

            putString(KEY_THEME_MODE, settings.displayThemeMode)
            putString(KEY_SUMMARIES_EXPANSION, settings.summariesDefaultExpansion)
            putInt(KEY_TEXT_SCALE, settings.textScalePercent)
            apply()
        }
    }

    fun resetToDefaults(): AppSettings {
        val defaults = AppSettings()
        saveSettings(defaults)
        return defaults
    }

    companion object {
        private const val KEY_SPEECH_RATE = "tts_speech_rate"
        private const val KEY_VOICE_LOCALE = "tts_voice_locale"
        private const val KEY_AUTO_ADVANCE = "tts_auto_advance"
        private const val KEY_PAUSE_SECONDS = "tts_pause_seconds"
        private const val KEY_READ_EVIDENCE = "tts_read_evidence"

        private const val KEY_DAILY_GOAL = "sm2_daily_goal"
        private const val KEY_SESSION_MODE = "sm2_default_session_mode"
        private const val KEY_DEFAULT_EASE = "sm2_default_ease"
        private const val KEY_SHOW_REF_FRONT = "sm2_show_ref_front"

        private const val KEY_MOCK_DURATION = "exam_mock_duration"
        private const val KEY_PRACTICE_DOMAIN = "exam_practice_domain"
        private const val KEY_INSTANT_FEEDBACK = "exam_instant_feedback"
        private const val KEY_SHUFFLE_QUESTIONS = "exam_shuffle_questions"

        private const val KEY_THEME_MODE = "display_theme_mode"
        private const val KEY_SUMMARIES_EXPANSION = "display_summaries_expansion"
        private const val KEY_TEXT_SCALE = "display_text_scale"
    }
}

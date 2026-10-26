package com.example.audio

enum class TtsItemType {
    SUMMARY,
    FLASHCARD
}

data class TtsPlaylistItem(
    val id: String,
    val type: TtsItemType,
    val title: String,
    val subtitle: String,
    val spokenText: String,
    val referenceId: String
) {
    val estimatedDurationSeconds: Int
        get() {
            val words = spokenText.split("\\s+".toRegex()).size
            // Average conversational speech rate: ~140 words per minute (~2.3 words/sec)
            return (words / 2.3).toInt().coerceAtLeast(3)
        }
}

data class TtsPlaybackState(
    val isInitialized: Boolean = false,
    val isPlaying: Boolean = false,
    val isPaused: Boolean = false,
    val currentItemIndex: Int = 0,
    val speechRate: Float = 1.0f,
    val playlist: List<TtsPlaylistItem> = emptyList(),
    val isPlaylistSheetOpen: Boolean = false,
    val currentSpeakingItem: TtsPlaylistItem? = null,
    val statusMessage: String? = null
)

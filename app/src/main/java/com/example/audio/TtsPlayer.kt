package com.example.audio

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.Locale

class TtsPlayer(
    context: Context?,
    private val onInitResult: (Boolean) -> Unit = {},
    private val onUtteranceFinished: (String) -> Unit = {},
    private val onUtteranceError: (String) -> Unit = {}
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    var isReady: Boolean = false
        private set

    private var pendingRate: Float = 1.0f

    init {
        if (context != null) {
            try {
                tts = TextToSpeech(context.applicationContext, this)
            } catch (e: Exception) {
                Log.e("TtsPlayer", "Failed to instantiate TextToSpeech: ${e.message}")
                isReady = false
                onInitResult(false)
            }
        } else {
            isReady = false
            onInitResult(false)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val engine = tts
            if (engine != null) {
                // Prefer British English for UK FRCPath examinations, fallback to general English
                val langResult = engine.setLanguage(Locale.UK)
                if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    engine.setLanguage(Locale.ENGLISH)
                }
                engine.setSpeechRate(pendingRate)
                engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        // playback began
                    }

                    override fun onDone(utteranceId: String?) {
                        if (utteranceId != null) {
                            onUtteranceFinished(utteranceId)
                        }
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        if (utteranceId != null) {
                            onUtteranceError(utteranceId)
                        }
                    }

                    override fun onError(utteranceId: String?, errorCode: Int) {
                        if (utteranceId != null) {
                            onUtteranceError(utteranceId)
                        }
                    }
                })
                isReady = true
                onInitResult(true)
            } else {
                isReady = false
                onInitResult(false)
            }
        } else {
            Log.w("TtsPlayer", "TTS Init failed with status: $status")
            isReady = false
            onInitResult(false)
        }
    }

    fun setSpeechRate(rate: Float) {
        pendingRate = rate
        if (isReady) {
            tts?.setSpeechRate(rate)
        }
    }

    fun speak(text: String, utteranceId: String): Boolean {
        if (!isReady || tts == null) {
            Log.w("TtsPlayer", "TTS not ready to speak utterance: $utteranceId")
            return false
        }
        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        }
        val result = tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
        return result == TextToSpeech.SUCCESS
    }

    fun stop() {
        try {
            tts?.stop()
        } catch (e: Exception) {
            Log.e("TtsPlayer", "Error stopping TTS: ${e.message}")
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
            isReady = false
        } catch (e: Exception) {
            Log.e("TtsPlayer", "Error shutting down TTS: ${e.message}")
        }
    }
}

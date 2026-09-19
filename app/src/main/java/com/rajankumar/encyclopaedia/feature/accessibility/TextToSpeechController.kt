package com.rajankumar.encyclopaedia.feature.accessibility

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TextToSpeechController(context: Context) : TextToSpeech.OnInitListener {
  private val textToSpeech = TextToSpeech(context.applicationContext, this)
  private var ready = false
  private var pendingText: String? = null
  private var speechRate = SpeechRate.NORMAL

  override fun onInit(status: Int) {
    ready = status == TextToSpeech.SUCCESS
    if (!ready) {
      pendingText = null
      return
    }

    textToSpeech.language = Locale.getDefault()
    textToSpeech.setSpeechRate(speechRate.value)
    pendingText?.let {
      pendingText = null
      speak(it)
    }
  }

  fun setSpeechRate(rate: SpeechRate) {
    speechRate = rate
    if (ready) textToSpeech.setSpeechRate(rate.value)
  }

  fun speak(content: SpeakableContent) = speak(content.asSpeechText())

  fun speak(text: String) {
    val normalized = text.trim()
    if (normalized.isEmpty()) return
    if (!ready) {
      pendingText = normalized
      return
    }
    textToSpeech.speak(normalized, TextToSpeech.QUEUE_FLUSH, null, "encyclopaedia-content")
  }

  fun stop() {
    pendingText = null
    if (ready) textToSpeech.stop()
  }

  fun shutdown() {
    pendingText = null
    textToSpeech.stop()
    textToSpeech.shutdown()
    ready = false
  }
}

package com.rajankumar.encyclopaedia.feature.accessibility

enum class SpeechError { NOT_READY, LANGUAGE_UNAVAILABLE, EMPTY_CONTENT, ENGINE_FAILURE }

fun SpeechError.message(): String = when (this) {
  SpeechError.NOT_READY -> "Text-to-speech is still starting. Try again in a moment."
  SpeechError.LANGUAGE_UNAVAILABLE -> "The selected language is not available in the device speech engine."
  SpeechError.EMPTY_CONTENT -> "There is no readable text on this item."
  SpeechError.ENGINE_FAILURE -> "The device speech engine could not read this content."
}
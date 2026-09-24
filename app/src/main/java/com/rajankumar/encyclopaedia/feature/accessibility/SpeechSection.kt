package com.rajankumar.encyclopaedia.feature.accessibility

data class SpeechSection(val heading: String, val text: String) {
  fun speechText(): String = listOf(heading.trim(), sanitizeSpeechText(text)).filter { it.isNotBlank() }.joinToString(". ")
}

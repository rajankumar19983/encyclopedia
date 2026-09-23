package com.rajankumar.encyclopaedia.feature.accessibility

data class SpeechRequest(val text: String, val kind: SpeechContentKind = SpeechContentKind.GENERAL) {
  val normalizedText: String get() = sanitizeSpeechText(text)
  val speakable: Boolean get() = normalizedText.isNotBlank()
}
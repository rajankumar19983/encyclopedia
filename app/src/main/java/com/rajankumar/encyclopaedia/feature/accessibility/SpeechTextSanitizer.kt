package com.rajankumar.encyclopaedia.feature.accessibility

fun sanitizeSpeechText(text: String): String = text
  .replace(Regex("\\s+"), " ")
  .trim()

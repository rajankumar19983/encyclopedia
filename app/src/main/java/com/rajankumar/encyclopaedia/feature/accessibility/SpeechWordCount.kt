package com.rajankumar.encyclopaedia.feature.accessibility

fun speechWordCount(text: String): Int {
  val clean = sanitizeSpeechText(text)
  return if (clean.isBlank()) 0 else clean.split(Regex("\\s+")).size
}

fun speechWordCountLabel(text: String): String = when (val count = speechWordCount(text)) { 0 -> "No readable words"; 1 -> "1 word"; else -> "$count words" }

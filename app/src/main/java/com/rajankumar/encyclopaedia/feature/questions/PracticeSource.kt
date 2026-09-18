package com.rajankumar.encyclopaedia.feature.questions

fun displayQuestionSource(source: String): String = when (source.trim().uppercase()) {
  "SCAN" -> "Scanned"
  "PDF" -> "PDF import"
  "PYQ" -> "Previous year"
  "AI" -> "AI generated"
  "USER" -> "Created by you"
  else -> source.ifBlank { "Unknown" }
}

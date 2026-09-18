package com.rajankumar.encyclopaedia.feature.questions

fun normalizeDifficulty(value: String): String = when (value.trim().uppercase()) {
  "EASY" -> "EASY"
  "HARD" -> "HARD"
  "UNRATED" -> "UNRATED"
  else -> "MEDIUM"
}

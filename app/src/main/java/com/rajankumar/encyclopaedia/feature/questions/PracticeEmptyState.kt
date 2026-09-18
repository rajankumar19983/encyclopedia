package com.rajankumar.encyclopaedia.feature.questions

fun PracticeMode.emptyMessage(): String = when (this) {
  PracticeMode.RANDOM -> "No practice-ready questions are available yet."
  PracticeMode.NEW -> "You have attempted every practice-ready question. Try Random or Mistakes."
  PracticeMode.MISTAKES -> "No previous mistakes are available to review."
}

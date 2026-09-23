package com.rajankumar.encyclopaedia.feature.accessibility

enum class SpeechContentKind { LESSON, QUESTION, EXPLANATION, GENERAL }

fun SpeechContentKind.label(): String = when (this) {
  SpeechContentKind.LESSON -> "Lesson"
  SpeechContentKind.QUESTION -> "Question"
  SpeechContentKind.EXPLANATION -> "Explanation"
  SpeechContentKind.GENERAL -> "Content"
}
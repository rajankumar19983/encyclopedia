package com.rajankumar.encyclopaedia.feature.performance

fun PerformanceCoverage.message(): String = when {
  total <= 0 -> "Add questions to measure syllabus practice coverage."
  practised <= 0 -> "No questions practised yet."
  complete -> "Every stored question has been practised at least once."
  else -> "$percent% coverage • $remaining questions not yet practised"
}

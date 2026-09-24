package com.rajankumar.encyclopaedia.feature.performance

fun PerformanceFocus.message(): String = when (this) {
  PerformanceFocus.START_PRACTICE -> "Attempt a few questions to establish your baseline."
  PerformanceFocus.COVERAGE -> "Practice more unattempted questions to broaden coverage."
  PerformanceFocus.ACCURACY -> "Review mistakes and prioritise weak topics."
  PerformanceFocus.SPEED -> "Practise timed questions while protecting accuracy."
  PerformanceFocus.MAINTAIN -> "Keep practising consistently and revisit weak areas."
}

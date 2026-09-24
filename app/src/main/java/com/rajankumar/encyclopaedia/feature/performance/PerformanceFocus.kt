package com.rajankumar.encyclopaedia.feature.performance

enum class PerformanceFocus { START_PRACTICE, COVERAGE, ACCURACY, SPEED, MAINTAIN }

fun PerformanceSummary.focus(): PerformanceFocus = when {
  attempts <= 0 -> PerformanceFocus.START_PRACTICE
  coverage < 50 -> PerformanceFocus.COVERAGE
  accuracy < 60 -> PerformanceFocus.ACCURACY
  averageTimeMs > 45_000 -> PerformanceFocus.SPEED
  else -> PerformanceFocus.MAINTAIN
}

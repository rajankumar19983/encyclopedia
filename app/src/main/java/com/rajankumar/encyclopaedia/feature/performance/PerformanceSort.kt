package com.rajankumar.encyclopaedia.feature.performance

enum class PerformanceSort { MOST_MISTAKES, LOWEST_ACCURACY, MOST_ATTEMPTED }

fun List<WeakQuestion>.sortedByPerformance(sort: PerformanceSort): List<WeakQuestion> = when (sort) {
  PerformanceSort.MOST_MISTAKES -> sortedByDescending { it.mistakes }
  PerformanceSort.LOWEST_ACCURACY -> sortedBy { it.accuracy }
  PerformanceSort.MOST_ATTEMPTED -> sortedByDescending { it.attempts }
}

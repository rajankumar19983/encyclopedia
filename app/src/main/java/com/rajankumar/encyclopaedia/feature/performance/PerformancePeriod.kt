package com.rajankumar.encyclopaedia.feature.performance

enum class PerformancePeriod(val days: Int, val label: String) {
  WEEK(7, "7 days"), MONTH(30, "30 days"), ALL(0, "All time")
}

package com.rajankumar.encyclopaedia.feature.performance

data class PerformanceSnapshot(val accuracy: Int, val coverage: Int, val level: PerformanceLevel)

fun PerformanceSummary.snapshot() = PerformanceSnapshot(accuracy, coverage, performanceLevel(attempts, accuracy))

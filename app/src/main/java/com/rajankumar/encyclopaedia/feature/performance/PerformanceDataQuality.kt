package com.rajankumar.encyclopaedia.feature.performance

data class PerformanceDataQuality(val confidence: PerformanceConfidence, val enoughForTrend: Boolean)

fun PerformanceSummary.dataQuality() = PerformanceDataQuality(performanceConfidence(attempts), attempts >= 20)

package com.rajankumar.encyclopaedia.feature.performance

fun PerformanceSummary.summaryText(): String = if (attempts <= 0) "No question attempts recorded yet" else "$accuracy% accuracy • $coverage% coverage • ${attempts.coerceAtLeast(0)} attempts"

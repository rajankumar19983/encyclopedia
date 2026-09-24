package com.rajankumar.encyclopaedia.feature.performance

fun PerformanceSummary.mistakes(): Int = (attempts.coerceAtLeast(0) - correct.coerceAtLeast(0)).coerceAtLeast(0)

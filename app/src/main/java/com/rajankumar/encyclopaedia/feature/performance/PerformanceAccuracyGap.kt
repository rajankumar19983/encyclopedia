package com.rajankumar.encyclopaedia.feature.performance

fun PerformanceSummary.accuracyGap(target: Int = 80): Int = (target.coerceIn(0, 100) - accuracy).coerceAtLeast(0)

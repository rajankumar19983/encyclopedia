package com.rajankumar.encyclopaedia.feature.performance

fun PerformanceSummary.hasAttemptedAllQuestions(): Boolean = totalQuestions > 0 && uniqueQuestions.coerceAtLeast(0) >= totalQuestions

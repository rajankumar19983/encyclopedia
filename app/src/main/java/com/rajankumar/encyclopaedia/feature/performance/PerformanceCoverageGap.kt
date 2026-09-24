package com.rajankumar.encyclopaedia.feature.performance

fun PerformanceSummary.unattemptedQuestions(): Int = (totalQuestions.coerceAtLeast(0) - uniqueQuestions.coerceAtLeast(0)).coerceAtLeast(0)

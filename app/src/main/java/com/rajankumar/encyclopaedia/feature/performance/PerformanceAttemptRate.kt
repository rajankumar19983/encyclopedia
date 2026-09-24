package com.rajankumar.encyclopaedia.feature.performance

fun PerformanceSummary.attemptsPerQuestion(): Double = if (uniqueQuestions <= 0) 0.0 else attempts.coerceAtLeast(0).toDouble() / uniqueQuestions

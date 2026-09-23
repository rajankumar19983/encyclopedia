package com.rajankumar.encyclopaedia.feature.performance

fun PerformanceEvidence.message(): String = "${accuracy.percent}% accuracy • ${coverage.percent}% coverage • ${confidence.name.lowercase()} confidence"

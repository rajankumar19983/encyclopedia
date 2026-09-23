package com.rajankumar.encyclopaedia.feature.performance

fun PerformanceEvidence.accessibilityDescription(): String = "Practice analytics: ${accuracy.percent} percent accuracy, ${coverage.percent} percent question coverage, ${confidence.name.lowercase()} evidence confidence. Recommended next step: ${nextStep()}."

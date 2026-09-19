package com.rajankumar.encyclopaedia.feature.performance

fun PerformanceData.recommendation(): String = when {
  summary.attempts == 0 -> "Start with a short practice session."
  weakQuestions.any { it.mistakes >= 3 } -> "Prioritize repeated mistakes in Revision."
  summary.coverage < 50 -> "Practise more unseen questions to broaden coverage."
  summary.accuracy < 75 -> "Mix mistake review with regular practice."
  else -> "Maintain recall with mixed practice and periodic revision."
}

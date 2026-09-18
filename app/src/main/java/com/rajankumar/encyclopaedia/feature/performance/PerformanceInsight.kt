package com.rajankumar.encyclopaedia.feature.performance

data class PerformanceInsight(val title: String, val detail: String)

fun PerformanceSummary.insights(): List<PerformanceInsight> = buildList {
  if (attempts == 0) add(PerformanceInsight("Start practising", "Your performance insights will appear after you answer questions."))
  else {
    add(PerformanceInsight("Accuracy", "$accuracy% across $attempts attempts."))
    add(PerformanceInsight("Coverage", "$coverage% of your question bank has been practised."))
    if (accuracy < 60) add(PerformanceInsight("Revision focus", "Review mistakes before adding more new questions."))
    if (coverage < 50) add(PerformanceInsight("Question coverage", "Include more new questions in upcoming sessions."))
  }
}

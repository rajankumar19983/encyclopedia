package com.rajankumar.encyclopaedia.feature.performance

enum class PerformanceLevel(val label: String) {
  NO_DATA("No data"), BUILDING("Building"), DEVELOPING("Developing"), STRONG("Strong")
}

fun performanceLevel(attempts: Int, accuracy: Int): PerformanceLevel = when {
  attempts == 0 -> PerformanceLevel.NO_DATA
  attempts < 20 -> PerformanceLevel.BUILDING
  accuracy < 75 -> PerformanceLevel.DEVELOPING
  else -> PerformanceLevel.STRONG
}

data class PerformanceMilestone(val title: String, val reached: Boolean)

fun PerformanceSummary.milestones(): List<PerformanceMilestone> = listOf(
  PerformanceMilestone("First 10 attempts", attempts >= 10),
  PerformanceMilestone("50 attempts", attempts >= 50),
  PerformanceMilestone("80% accuracy", attempts >= 10 && accuracy >= 80),
  PerformanceMilestone("Full question coverage", totalQuestions > 0 && coverage >= 100)
)

data class PerformanceInsight(val title: String, val detail: String)

fun PerformanceSummary.insights(): List<PerformanceInsight> = buildList {
  if (attempts == 0) {
    add(PerformanceInsight("Start practising", "Your performance insights will appear after you answer questions."))
  } else {
    add(PerformanceInsight("Accuracy", "$accuracy% across $attempts attempts."))
    add(PerformanceInsight("Coverage", "$coverage% of your question bank has been practised."))
    if (accuracy < 60) add(PerformanceInsight("Revision focus", "Review mistakes before adding more new questions."))
    if (coverage < 50) add(PerformanceInsight("Question coverage", "Include more new questions in upcoming sessions."))
  }
}

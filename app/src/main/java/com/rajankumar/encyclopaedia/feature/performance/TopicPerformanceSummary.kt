package com.rajankumar.encyclopaedia.feature.performance

data class TopicPerformanceSummary(
  val attemptedTopics: Int,
  val strongTopics: Int,
  val developingTopics: Int,
  val averageAccuracy: Int,
)

fun List<TopicPerformance>.summary(): TopicPerformanceSummary {
  if (isEmpty()) return TopicPerformanceSummary(0, 0, 0, 0)
  return TopicPerformanceSummary(
    attemptedTopics = size,
    strongTopics = count { it.attempts >= 3 && it.accuracyPercent >= 75 },
    developingTopics = count { it.accuracyPercent < 75 || it.attempts < 3 },
    averageAccuracy = sumOf { it.accuracyPercent } / size,
  )
}

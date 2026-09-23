package com.rajankumar.encyclopaedia.feature.performance

data class TopicPerformanceCoverage(
  val topicCount: Int,
  val practisedTopicCount: Int,
  val strongTopicCount: Int,
  val weakTopicCount: Int,
) {
  val unpractisedTopicCount: Int get() = (topicCount - practisedTopicCount).coerceAtLeast(0)
  val coveragePercent: Int get() = if (topicCount == 0) 0 else practisedTopicCount * 100 / topicCount
}

fun performanceCoverage(allTopicCount: Int, performance: List<TopicPerformance>): TopicPerformanceCoverage =
  TopicPerformanceCoverage(
    topicCount = allTopicCount.coerceAtLeast(0),
    practisedTopicCount = performance.size,
    strongTopicCount = performance.count { it.accuracyPercent >= 80 },
    weakTopicCount = performance.count { it.accuracyPercent < 60 },
  )

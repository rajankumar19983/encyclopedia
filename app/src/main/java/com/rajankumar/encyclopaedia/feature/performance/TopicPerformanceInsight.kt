package com.rajankumar.encyclopaedia.feature.performance

data class TopicPerformanceInsight(val headline: String, val detail: String)

fun topicPerformanceInsight(coverage: TopicPerformanceCoverage, priorities: List<TopicPerformancePriority>): TopicPerformanceInsight = when {
  coverage.topicCount == 0 -> TopicPerformanceInsight("Build your syllabus", "Add topics to start tracking module performance.")
  coverage.practisedTopicCount == 0 -> TopicPerformanceInsight("Start practising", "No topic has enough attempt data yet.")
  priorities.firstOrNull()?.performance?.accuracyPercent?.let { it < 60 } == true -> {
    val first = priorities.first()
    TopicPerformanceInsight("Focus on ${first.performance.topic.name}", "${first.performance.accuracyPercent}% accuracy • ${first.reason.lowercase()}.")
  }
  coverage.unpractisedTopicCount > 0 -> TopicPerformanceInsight("Expand coverage", "${coverage.unpractisedTopicCount} topics have no practice data yet.")
  else -> TopicPerformanceInsight("Maintain your progress", "All tracked topics have practice data. Keep revisiting weaker areas.")
}

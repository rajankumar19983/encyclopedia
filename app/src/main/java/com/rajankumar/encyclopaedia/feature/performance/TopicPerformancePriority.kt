package com.rajankumar.encyclopaedia.feature.performance

data class TopicPerformancePriority(
  val performance: TopicPerformance,
  val score: Int,
  val reason: String,
)

fun List<TopicPerformance>.studyPriorities(limit: Int = 5): List<TopicPerformancePriority> =
  map { item ->
    val accuracyGap = (100 - item.accuracyPercent).coerceAtLeast(0)
    val mistakeWeight = item.mistakes.coerceAtMost(20) * 3
    val score = accuracyGap + mistakeWeight
    val reason = when {
      item.accuracyPercent < 50 -> "Low accuracy"
      item.mistakes >= 3 -> "Repeated mistakes"
      item.accuracyPercent < 70 -> "Needs reinforcement"
      else -> "Keep practising"
    }
    TopicPerformancePriority(item, score, reason)
  }.sortedWith(compareByDescending<TopicPerformancePriority> { it.score }.thenBy { it.performance.topic.name.lowercase() })
    .take(limit.coerceAtLeast(0))

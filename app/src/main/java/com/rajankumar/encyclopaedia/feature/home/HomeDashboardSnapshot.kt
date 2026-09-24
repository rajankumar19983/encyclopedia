package com.rajankumar.encyclopaedia.feature.home

data class HomeDashboardSnapshot(
  val topicCount: Int,
  val questionCount: Int,
  val attemptCount: Int,
  val correctCount: Int,
  val practisedCount: Int
) {
  val hasStudyData: Boolean get() = topicCount > 0 || questionCount > 0 || attemptCount > 0

  fun normalized(): HomeDashboardSnapshot = copy(
    topicCount = topicCount.coerceAtLeast(0),
    questionCount = questionCount.coerceAtLeast(0),
    attemptCount = attemptCount.coerceAtLeast(0),
    correctCount = correctCount.coerceIn(0, attemptCount.coerceAtLeast(0)),
    practisedCount = practisedCount.coerceIn(0, questionCount.coerceAtLeast(0))
  )

  fun dashboard(): HomeDashboardState {
    val value = normalized()
    return buildHomeDashboardState(value.topicCount, value.questionCount, value.attemptCount, value.correctCount, value.practisedCount)
  }
}

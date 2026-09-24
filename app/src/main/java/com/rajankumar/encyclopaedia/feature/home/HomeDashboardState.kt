package com.rajankumar.encyclopaedia.feature.home

data class HomeDashboardState(
  val stats: List<HomeStatModel>,
  val status: HomeStudyStatus,
  val recommendation: HomePlanRecommendation,
  val motivation: String,
  val emptyState: HomeEmptyState
)

fun buildHomeDashboardState(topicCount: Int, questionCount: Int, attemptCount: Int, correctCount: Int, practisedCount: Int): HomeDashboardState {
  val metrics = calculateHomeMetrics(questionCount, attemptCount, correctCount, practisedCount)
  val status = homeStudyStatus(questionCount, attemptCount, metrics.accuracyPercent, metrics.coveragePercent)
  return HomeDashboardState(
    stats = buildHomeStatModels(topicCount, questionCount, attemptCount, correctCount, practisedCount),
    status = status,
    recommendation = recommendHomePlan(questionCount, attemptCount, metrics.coveragePercent, metrics.accuracyPercent),
    motivation = homeMotivation(status),
    emptyState = homeEmptyState(topicCount, questionCount)
  )
}

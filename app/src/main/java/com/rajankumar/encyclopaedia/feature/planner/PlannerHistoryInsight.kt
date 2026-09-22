package com.rajankumar.encyclopaedia.feature.planner

data class PlannerHistoryInsight(
  val recentCompletion: Int,
  val consistency: Int,
  val trend: PlannerTrend,
  val message: String,
)

fun List<PlannerDayHistory>.plannerHistoryInsight(): PlannerHistoryInsight {
  val recent = recentCompletionPercent()
  val consistency = consistencyPercent()
  val trend = completionTrend()
  val message = when {
    isEmpty() -> "Complete a few planned days to build useful study-planning insights."
    recent >= 80 && consistency >= 70 -> "Your recent plans are being completed reliably."
    trend == PlannerTrend.IMPROVING -> "Your plan completion is improving. Keep the daily workload realistic."
    recent < 50 -> "Recent completion is low. Reduce plan size and finish carried work first."
    else -> "Your planning rhythm is developing. Keep prioritising unfinished work."
  }
  return PlannerHistoryInsight(recent, consistency, trend, message)
}

package com.rajankumar.encyclopaedia.feature.planner

enum class PlannerHistoryHealth { NO_DATA, STRONG, STABLE, STRUGGLING }

fun List<PlannerDayHistory>.plannerHistoryHealth(): PlannerHistoryHealth {
  if (isEmpty()) return PlannerHistoryHealth.NO_DATA
  val recent = recentCompletionPercent()
  val consistency = consistencyPercent()
  return when {
    recent >= 80 && consistency >= 60 -> PlannerHistoryHealth.STRONG
    recent < 45 -> PlannerHistoryHealth.STRUGGLING
    else -> PlannerHistoryHealth.STABLE
  }
}

fun PlannerHistoryHealth.label(): String = when (this) {
  PlannerHistoryHealth.NO_DATA -> "Building history"
  PlannerHistoryHealth.STRONG -> "Strong planning rhythm"
  PlannerHistoryHealth.STABLE -> "Stable planning rhythm"
  PlannerHistoryHealth.STRUGGLING -> "Planning needs adjustment"
}

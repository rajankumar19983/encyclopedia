package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertTrue
import org.junit.Test

class PlannerHistoryInsightTest {
  @Test
  fun emptyHistoryExplainsHowToBuildInsights() {
    assertTrue(emptyList<PlannerDayHistory>().plannerHistoryInsight().message.contains("few planned days"))
  }

  @Test
  fun lowRecentCompletionRecommendsSmallerPlans() {
    val history = listOf(
      PlannerDayHistory("2026-09-22", 1, 4),
      PlannerDayHistory("2026-09-21", 1, 4),
      PlannerDayHistory("2026-09-20", 1, 4),
    )
    assertTrue(history.plannerHistoryInsight().message.contains("Reduce plan size"))
  }
}

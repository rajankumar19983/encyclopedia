package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlannerDailyRecommendationTest {
  @Test
  fun carriedPendingWorkIsExplicitlyPrioritised() {
    val tasks = listOf(PlannerTaskEntity("a", "Revision", "2026-09-22", carriedFromDate = "2026-09-21"))
    val recommendation = tasks.dailyPlannerRecommendation()
    assertEquals("Clear carried work first", recommendation.title)
    assertTrue(recommendation.detail.contains("1 carried"))
  }

  @Test
  fun completedPlanIsRecognised() {
    val tasks = listOf(PlannerTaskEntity("a", "Revision", "2026-09-22", isCompleted = true))
    assertEquals("Plan completed", tasks.dailyPlannerRecommendation().title)
  }
}

package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertTrue
import org.junit.Test

class PlannerRecoveryInsightTest {
  @Test
  fun weakCarryRecoveryRecommendsSmallerPlans() {
    val tasks = (1..4).map { PlannerTaskEntity("$it", "Task $it", "2026-09-22", carriedFromDate = "2026-09-20", isCompleted = it == 1) }
    assertTrue(tasks.plannerRecoveryInsight().message.contains("smaller"))
  }

  @Test
  fun strongRecoveryIsRecognised() {
    val tasks = (1..4).map { PlannerTaskEntity("$it", "Task $it", "2026-09-22", carriedFromDate = "2026-09-20", isCompleted = it <= 3) }
    assertTrue(tasks.plannerRecoveryInsight().message.contains("successfully"))
  }
}

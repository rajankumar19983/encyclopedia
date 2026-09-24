package com.rajankumar.encyclopaedia.feature.home

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.*
import org.junit.Test

class HomeTodayPlanTest {
  @Test fun summarizesPlannerTasks() {
    val plan = listOf(PlannerTaskEntity("1", "Done", "2026-09-24", isCompleted = true), PlannerTaskEntity("2", "Next", "2026-09-24")).toHomeTodayPlan()
    assertEquals(1, plan.completed); assertEquals(2, plan.total); assertEquals("Next", plan.nextTask); assertEquals(.5f, plan.progress)
  }
  @Test fun emptyPlanHasFriendlySummary() = assertEquals("No tasks planned for today yet.", emptyList<PlannerTaskEntity>().toHomeTodayPlan().summary)
}

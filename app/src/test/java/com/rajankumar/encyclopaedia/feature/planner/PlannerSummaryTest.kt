package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerSummaryTest {
  @Test
  fun emptyPlanPromptsPlanning() {
    assertEquals("Plan your first study task for today.", PlannerProgress(0, 0).summaryText())
  }

  @Test
  fun completePlanCelebratesCompletion() {
    assertEquals("Today's plan is complete.", PlannerProgress(3, 3).summaryText())
  }

  @Test
  fun untouchedPlanShowsReadyCount() {
    assertEquals("4 tasks ready to start.", PlannerProgress(0, 4).summaryText())
  }

  @Test
  fun activePlanShowsRemainingCount() {
    assertEquals("2 tasks left for today.", PlannerProgress(2, 4).summaryText())
  }
}

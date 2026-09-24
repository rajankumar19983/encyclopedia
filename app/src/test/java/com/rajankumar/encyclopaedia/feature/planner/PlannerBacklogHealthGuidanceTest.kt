package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertTrue
import org.junit.Test

class PlannerBacklogHealthGuidanceTest {
  @Test fun overloadedGuidanceDiscouragesNewTasks() = assertTrue(PlannerBacklogHealth.OVERLOADED.guidance().contains("before planning new tasks"))
  @Test fun clearGuidanceEncouragesFocusedPlanning() = assertTrue(PlannerBacklogHealth.CLEAR.guidance().contains("focused"))
}

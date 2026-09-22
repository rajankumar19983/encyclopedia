package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerDayStatusTest {
  @Test fun emptyIsUnplanned() = assertEquals(PlannerDayStatus.UNPLANNED, emptyList<PlannerTaskEntity>().plannerDayStatus())

  @Test fun untouchedPlanIsNotStarted() = assertEquals(
    PlannerDayStatus.NOT_STARTED,
    listOf(PlannerTaskEntity("a", "A", "2026-09-22")).plannerDayStatus(),
  )

  @Test fun mixedPlanIsInProgress() = assertEquals(
    PlannerDayStatus.IN_PROGRESS,
    listOf(
      PlannerTaskEntity("a", "A", "2026-09-22", isCompleted = true),
      PlannerTaskEntity("b", "B", "2026-09-22"),
    ).plannerDayStatus(),
  )
}

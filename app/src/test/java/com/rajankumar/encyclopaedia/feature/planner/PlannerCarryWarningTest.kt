package com.rajankumar.encyclopaedia.feature.planner

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PlannerCarryWarningTest {
  private fun carried(id: Int) = PlannerTaskEntity("$id", "Task $id", "2026-09-22", carriedFromDate = "2026-09-20")

  @Test fun noWarningForSmallCarryLoad() = assertNull(listOf(carried(1), carried(2)).plannerCarryWarning())

  @Test
  fun warnsWhenCarryLoadReachesThree() {
    val warning = (1..3).map(::carried).plannerCarryWarning()
    assertEquals(3, warning?.count)
    assertTrue(warning?.message?.contains("older work") == true)
  }

  @Test
  fun strongerWarningForLargeBacklog() {
    assertTrue((1..6).map(::carried).plannerCarryWarning()?.message?.contains("Reduce new work") == true)
  }
}

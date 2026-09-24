package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PlannerDateSafetyTest {
  @Test fun validDateAdvancesOneDay() = assertEquals("2026-09-25", nextPlannerDate("2026-09-24"))
  @Test fun invalidDateReturnsNull() = assertNull(nextPlannerDate("not-a-date"))
  @Test fun displayDatePreservesInvalidValue() = assertEquals("unknown", plannerDisplayDate("unknown"))
}

package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeDailyGoalTest {
  @Test fun calculatesGoalPercent() = assertEquals(50, HomeDailyGoal(5, 10).percent)
  @Test fun clampsGoalCounts() { assertEquals(0, HomeDailyGoal(-2, 10).percent); assertEquals(100, HomeDailyGoal(20, 10).percent) }
}

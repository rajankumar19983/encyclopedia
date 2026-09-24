package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeDailyGoalLabelTest {
  @Test fun labelsGoalStates() { assertEquals("Daily goal not started", HomeDailyGoal(0, 10).label()); assertEquals("5 of 10 questions • 50%", HomeDailyGoal(5, 10).label()); assertEquals("Daily goal complete", HomeDailyGoal(10, 10).label()) }
}

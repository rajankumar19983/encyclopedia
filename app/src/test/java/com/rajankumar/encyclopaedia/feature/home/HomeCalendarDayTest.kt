package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.*
import org.junit.Test

class HomeCalendarDayTest {
  @Test fun normalDayIsValid() = assertTrue(HomeCalendarDay(24).valid)
  @Test fun impossibleDayIsInvalid() = assertFalse(HomeCalendarDay(32).valid)
  @Test fun accessibilityDescribesSelectedPlannedDay() = assertEquals("Day 24, selected, study plan available", HomeCalendarDay(24, selected = true, hasPlan = true).accessibilityLabel)
}

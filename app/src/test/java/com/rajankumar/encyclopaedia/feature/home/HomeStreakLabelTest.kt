package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeStreakLabelTest {
  @Test fun labelsStreaks() { assertEquals("Start your streak today", homeStreakLabel(0)); assertEquals("1 day streak", homeStreakLabel(1)); assertEquals("7 day streak", homeStreakLabel(7)) }
}

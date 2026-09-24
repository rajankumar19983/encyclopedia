package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeStudyMinutesTest {
  @Test fun formatsStudyTime() { assertEquals("No study time today", homeStudyTimeLabel(0)); assertEquals("25 min studied", homeStudyTimeLabel(25)); assertEquals("1h 15m studied", homeStudyTimeLabel(75)) }
}

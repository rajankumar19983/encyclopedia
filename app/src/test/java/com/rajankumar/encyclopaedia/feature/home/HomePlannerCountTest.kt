package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomePlannerCountTest {
  @Test fun labelsPlannerProgress() { assertEquals("No tasks planned", homePlannerCountLabel(0, 0)); assertEquals("2 of 5 tasks complete", homePlannerCountLabel(2, 5)); assertEquals("5 of 5 tasks complete", homePlannerCountLabel(9, 5)) }
}

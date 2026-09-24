package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeUpcomingItemTest {
  @Test fun blankTitleHasSafeFallback() = assertEquals("Untitled study task", HomeUpcomingItem(" ", "Tomorrow").displayTitle)
}

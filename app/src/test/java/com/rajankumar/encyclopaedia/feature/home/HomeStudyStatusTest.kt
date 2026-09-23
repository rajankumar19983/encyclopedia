package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeStudyStatusTest {
  @Test fun classifiesStudyLifecycle() {
    assertEquals(HomeStudyStatus.EMPTY, homeStudyStatus(0, 0, 0, 0))
    assertEquals(HomeStudyStatus.STARTING, homeStudyStatus(10, 0, 0, 0))
    assertEquals(HomeStudyStatus.NEEDS_REVIEW, homeStudyStatus(10, 5, 40, 50))
    assertEquals(HomeStudyStatus.BUILDING_COVERAGE, homeStudyStatus(10, 5, 80, 50))
    assertEquals(HomeStudyStatus.ESTABLISHED, homeStudyStatus(10, 10, 80, 100))
  }
}

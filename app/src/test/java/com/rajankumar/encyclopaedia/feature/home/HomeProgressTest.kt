package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeProgressTest {
  @Test fun percentProgressIsBounded() {
    assertEquals(0f, percentProgress(-5))
    assertEquals(0.42f, percentProgress(42))
    assertEquals(1f, percentProgress(140))
  }

  @Test fun practiceProgressUsesTarget() {
    assertEquals(0.5f, practiceProgress(10, 20))
    assertEquals(1f, practiceProgress(30, 20))
    assertEquals(0f, practiceProgress(10, 0))
  }
}

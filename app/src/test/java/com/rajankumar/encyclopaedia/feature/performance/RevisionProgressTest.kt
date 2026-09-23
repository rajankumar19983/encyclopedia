package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionProgressTest {
  @Test fun progressIsBounded() {
    assertEquals(0f, revisionProgress(0, 0))
    assertEquals(0.5f, revisionProgress(2, 4))
    assertEquals(1f, revisionProgress(8, 4))
  }
}

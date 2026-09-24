package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionSessionProgressTest {
  @Test fun calculatesPercent() = assertEquals(50, RevisionSessionProgress(5, 10).percent)
  @Test fun clampsInvalidCounts() { assertEquals(0, RevisionSessionProgress(-1, 10).percent); assertEquals(100, RevisionSessionProgress(20, 10).percent) }
}

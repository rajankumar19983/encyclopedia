package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionFilterTest {
  @Test fun queryWhitespaceIsNormalized() = assertEquals("operating systems", normalizeRevisionQuery("  operating   systems "))
  @Test fun queryLengthIsBounded() = assertEquals(120, normalizeRevisionQuery("x".repeat(200)).length)
}

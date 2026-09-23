package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrOptionDuplicateTest {
  @Test fun detectsEquivalentOptionsIgnoringCaseAndSpacing() {
    val result = detectDuplicateOcrOptions(listOf("Random Access Memory", "ROM", " random   access memory "))
    assertTrue(result.hasDuplicates)
    assertEquals(listOf(0, 2), result.groups.single())
  }

  @Test fun distinctOptionsAreClean() = assertFalse(detectDuplicateOcrOptions(listOf("RAM", "ROM")).hasDuplicates)
}

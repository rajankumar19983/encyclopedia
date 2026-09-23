package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrOptionDuplicateWarningTest {
  @Test fun cleanOptionsHaveNoWarning() = assertNull(OcrDuplicateOptions(emptyList()).warning())

  @Test fun warningUsesOptionLabels() {
    val warning = OcrDuplicateOptions(listOf(listOf(1, 4))).warning().orEmpty()
    assertTrue(warning.contains("B, E"))
  }
}

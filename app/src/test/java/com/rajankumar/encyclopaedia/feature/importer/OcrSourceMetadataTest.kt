package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrSourceMetadataTest {
  @Test
  fun extractsKnownExamAndYear() {
    val metadata = OcrSourceMetadataExtractor.extract("DSSSB TGT Computer Science 2024 Shift 1")
    assertEquals("DSSSB", metadata.examName)
    assertEquals(2024, metadata.year)
  }
}

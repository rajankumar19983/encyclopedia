package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrSourceMetadataHardeningTest {
  @Test fun recognizesFullDsssbNameDateAndShift() {
    val metadata = OcrSourceMetadataExtractor.extract("Delhi Subordinate Services Selection Board TGT Computer Science 14/09/2025 Shift-II")
    assertEquals("DSSSB", metadata.examName)
    assertEquals(2025, metadata.year)
    assertEquals("2025-09-14", metadata.examDate)
    assertEquals("SHIFT 2", metadata.shift)
    assertEquals(OcrMetadataConfidence.HIGH, metadata.confidence)
  }

  @Test fun recognizesBpscLongNameAndSessionWord() {
    val metadata = OcrSourceMetadataExtractor.extract("Bihar Public Service Commission TRE 4.0 2026 Session: Morning")
    assertEquals("BPSC", metadata.examName)
    assertEquals(2026, metadata.year)
    assertEquals("MORNING", metadata.shift)
    assertEquals(OcrMetadataConfidence.HIGH, metadata.confidence)
  }

  @Test fun dateSuppliesYearWithoutSeparateYearMatch() {
    val metadata = OcrSourceMetadataExtractor.extract("CTET paper held 07-07-2024")
    assertEquals(2024, metadata.year)
    assertEquals("2024-07-07", metadata.examDate)
    assertEquals(OcrMetadataConfidence.HIGH, metadata.confidence)
  }

  @Test fun unknownExamDoesNotInventAttribution() {
    val metadata = OcrSourceMetadataExtractor.extract("Computer Science practice questions")
    assertEquals(null, metadata.examName)
    assertEquals(OcrMetadataConfidence.NONE, metadata.confidence)
    assertTrue(metadata.needsMetadataReview())
  }
}

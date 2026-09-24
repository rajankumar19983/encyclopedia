package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrMetadataPresentationTest {
  @Test fun labelDoesNotRepeatYearWhenFullDateExists() {
    val metadata = OcrSourceMetadata("DSSSB", 2025, "2025-09-14", "SHIFT 2", OcrMetadataConfidence.HIGH)
    assertEquals("DSSSB • 2025-09-14 • SHIFT 2", metadata.reviewLabel())
  }

  @Test fun lowConfidenceMetadataProducesReviewNotice() {
    val metadata = OcrSourceMetadata(year = 2024, confidence = OcrMetadataConfidence.LOW)
    assertTrue(metadata.metadataNotices().isNotEmpty())
    assertTrue(metadata.needsMetadataReview())
  }
}

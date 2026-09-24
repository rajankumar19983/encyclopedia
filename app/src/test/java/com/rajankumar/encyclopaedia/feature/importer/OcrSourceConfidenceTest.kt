package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrSourceConfidenceTest {
  @Test fun classifiesMetadataConfidence() {
    assertEquals(OcrMetadataConfidence.NONE, OcrSourceMetadataExtractor.extract("No source metadata").confidence)
    assertEquals(OcrMetadataConfidence.LOW, OcrSourceMetadataExtractor.extract("DSSSB").confidence)
    assertEquals(OcrMetadataConfidence.MEDIUM, OcrSourceMetadataExtractor.extract("DSSSB 2024").confidence)
    assertEquals(OcrMetadataConfidence.HIGH, OcrSourceMetadataExtractor.extract("DSSSB 2024 SHIFT 1").confidence)
  }
}

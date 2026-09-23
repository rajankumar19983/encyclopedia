package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrSourceConfidenceTest {
  @Test fun classifiesMetadataCompleteness() {
    assertEquals(OcrMetadataConfidence.NONE, OcrSourceMetadata().confidence())
    assertEquals(OcrMetadataConfidence.PARTIAL, OcrSourceMetadata(examName = "DSSSB").confidence())
    assertEquals(OcrMetadataConfidence.PARTIAL, OcrSourceMetadata(year = 2024).confidence())
    assertEquals(OcrMetadataConfidence.COMPLETE, OcrSourceMetadata("DSSSB", 2024).confidence())
  }
}

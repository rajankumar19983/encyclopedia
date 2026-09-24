package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertTrue
import org.junit.Test

class OcrMetadataReviewTest {
  @Test fun missingMetadataPromptsManualEntry() {
    assertTrue(OcrSourceMetadata().reviewGuidance().message.contains("Add it manually"))
  }

  @Test fun completeMetadataStillRequiresVerification() {
    assertTrue(OcrSourceMetadata("DSSSB", 2025).reviewGuidance().message.contains("verify", ignoreCase = true))
  }
}

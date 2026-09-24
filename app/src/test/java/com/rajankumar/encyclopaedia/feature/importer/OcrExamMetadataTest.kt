package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OcrExamMetadataTest {
  @Test fun detectsKnownExamNameFromMixedPage() {
    assertEquals("DSSSB TGT Computer Science", detectOcrExamMetadata("DSSSB TGT Computer Science 2024\nनिर्देश\n1. Question").examName)
  }

  @Test fun absentExamMetadataRemainsUnknown() {
    assertNull(detectOcrExamMetadata("1. Which protocol is used for web pages?").examName)
  }
}

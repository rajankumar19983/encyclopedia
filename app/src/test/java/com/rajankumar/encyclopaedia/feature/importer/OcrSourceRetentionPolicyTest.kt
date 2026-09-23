package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrSourceRetentionPolicyTest {
  @Test fun originalMediaIsNotRetainedByDefault() {
    assertFalse(defaultOcrSourceRetentionPolicy.retainOriginalMedia)
    assertTrue(defaultOcrSourceRetentionPolicy.retainExtractedTextDuringReview)
    assertFalse(defaultOcrSourceRetentionPolicy.persistExtractedTextAfterImport)
  }
}

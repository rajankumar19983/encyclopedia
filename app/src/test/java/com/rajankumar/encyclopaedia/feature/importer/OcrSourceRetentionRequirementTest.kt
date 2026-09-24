package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrSourceRetentionRequirementTest {
  @Test fun defaultsDoNotPersistSourceImagesOrRawOcrText() {
    val policy = defaultOcrSourceRetentionPolicy
    assertFalse(policy.retainOriginalMedia)
    assertTrue(policy.retainExtractedTextDuringReview)
    assertFalse(policy.persistExtractedTextAfterImport)
  }
}

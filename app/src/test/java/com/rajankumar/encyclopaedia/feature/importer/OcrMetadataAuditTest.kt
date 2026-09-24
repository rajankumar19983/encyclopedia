package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrMetadataAuditTest {
  @Test fun strongConsistentMetadataDoesNotNeedMetadataReview() {
    val audit = auditOcrMetadata(listOf(OcrPageText(1, "DSSSB 14/09/2025 Shift II")))
    assertFalse(audit.requiresReview)
  }

  @Test fun conflictingSourcesRequireReview() {
    val audit = auditOcrMetadata(listOf(OcrPageText(1, "DSSSB 2025"), OcrPageText(2, "BPSC 2025")))
    assertTrue(audit.requiresReview)
  }
}

package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrReviewRiskTest {
  @Test fun cleanNormalDraftIsLowRisk() {
    val audit = OcrDraftAudit(OcrOptionCountQuality.NORMAL, OcrAnswerConflict(listOf("A")), emptyList())
    assertEquals(OcrReviewRisk.LOW, audit.reviewRisk())
  }

  @Test fun warningRequiresReview() {
    val audit = OcrDraftAudit(OcrOptionCountQuality.NORMAL, OcrAnswerConflict(listOf("A")), listOf("Verify OCR"))
    assertEquals(OcrReviewRisk.REVIEW_REQUIRED, audit.reviewRisk())
  }
}

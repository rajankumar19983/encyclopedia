package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertTrue
import org.junit.Test

class OcrReviewSemanticsTest {
  @Test fun labelsExplainPersistenceOutcome() {
    assertTrue(OcrReviewDecision.APPROVED.accessibilityLabel(2).contains("saved"))
    assertTrue(OcrReviewDecision.REJECTED.accessibilityLabel(3).contains("not saved"))
  }
}

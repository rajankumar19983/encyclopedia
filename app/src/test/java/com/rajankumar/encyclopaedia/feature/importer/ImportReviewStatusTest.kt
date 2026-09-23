package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertTrue
import org.junit.Test

class ImportReviewStatusTest {
  @Test fun reportsAttentionCount() {
    assertTrue(importReviewStatus(OcrReviewSummary(5, 3, 2)).contains("2 need attention"))
  }

  @Test fun confirmsNothingIsAutoSaved() {
    assertTrue(importReviewStatus(OcrReviewSummary(3, 3, 0)).contains("Nothing is saved"))
  }
}

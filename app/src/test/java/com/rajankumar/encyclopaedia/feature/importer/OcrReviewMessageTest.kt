package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrReviewMessageTest {
  @Test fun reportsRemainingReviewWork() {
    assertEquals("2 of 3 reviewed • 1 remaining.", OcrBatchReviewProgress(3, 1, 1).message())
  }

  @Test fun reportsCompletedBatch() {
    assertEquals("Review complete • 2 approved • 1 rejected.", OcrBatchReviewProgress(3, 2, 1).message())
  }
}

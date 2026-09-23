package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OcrBatchCompletionTest {
  @Test fun emptyBatchNeedsNoGuidance() = assertNull(OcrBatchReviewProgress(0, 0, 0).completionGuidance())

  @Test fun unresolvedBatchExplainsRemainingDecision() {
    assertEquals("1 OCR draft still needs an explicit Approve or Reject decision.", OcrBatchReviewProgress(3, 1, 1).completionGuidance())
  }

  @Test fun completedBatchIsSafeToLeave() {
    assertEquals("Every OCR draft has an explicit decision. You can leave this import safely.", OcrBatchReviewProgress(2, 1, 1).completionGuidance())
  }
}

package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertTrue
import org.junit.Test

class RevisionHealthTest {
  @Test fun heavyQueueGuidancePrioritizesUrgentWork() = assertTrue(RevisionHealth.HEAVY.guidance().contains("urgent"))
  @Test fun clearQueueStillEncouragesPractice() = assertTrue(RevisionHealth.CLEAR.guidance().contains("practising"))
}

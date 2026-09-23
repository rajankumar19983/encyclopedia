package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class ImportReviewProgressTest {
  @Test fun tracksExplicitReviewOutcomes() {
    assertEquals(ImportReviewProgress(10, 4, 2, 4), importReviewProgress(10, 4, 2))
  }

  @Test fun clampsImpossibleCounts() {
    assertEquals(ImportReviewProgress(3, 3, 0, 0), importReviewProgress(3, 5, 2))
  }
}

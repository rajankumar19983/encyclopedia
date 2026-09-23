package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertTrue
import org.junit.Test

class RevisionScoreTest {
  @Test fun weakLowEvidenceItemsScoreHigher() {
    assertTrue(revisionScore(30, 1) > revisionScore(90, 5))
  }
}

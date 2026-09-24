package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionAccuracyTest {
  @Test fun emptyAttemptsHaveZeroAccuracy() = assertEquals(0, revisionAccuracyPercent(0, 0))
  @Test fun accuracyIsCalculated() = assertEquals(75, revisionAccuracyPercent(3, 4))
  @Test fun correctAnswersAreClamped() = assertEquals(100, revisionAccuracyPercent(9, 4))
}

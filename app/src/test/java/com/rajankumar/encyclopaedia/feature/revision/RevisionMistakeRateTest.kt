package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionMistakeRateTest {
  @Test fun mistakeRateIsCalculated() = assertEquals(50, revisionMistakeRatePercent(2, 4))
  @Test fun mistakesCannotExceedAttempts() = assertEquals(100, revisionMistakeRatePercent(8, 2))
  @Test fun zeroAttemptsAreSafe() = assertEquals(0, revisionMistakeRatePercent(1, 0))
}

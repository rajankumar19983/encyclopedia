package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionMasteryTest {
  @Test fun weakAccuracyNeedsWork() = assertEquals(RevisionMastery.NEEDS_WORK, revisionMastery(40))
  @Test fun improvingAccuracyIsDeveloping() = assertEquals(RevisionMastery.DEVELOPING, revisionMastery(65))
  @Test fun highAccuracyIsStrong() = assertEquals(RevisionMastery.STRONG, revisionMastery(90))
}

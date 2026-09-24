package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionPriorityBandTest {
  @Test fun highMistakeRateIsUrgent() = assertEquals(RevisionPriority.URGENT, revisionPriorityFromMistakeRate(80))
  @Test fun mediumMistakeRateIsHigh() = assertEquals(RevisionPriority.HIGH, revisionPriorityFromMistakeRate(60))
  @Test fun lowMistakeRateIsLow() = assertEquals(RevisionPriority.LOW, revisionPriorityFromMistakeRate(10))
}

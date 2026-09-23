package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class AccuracySampleTest {
  @Test fun calculatesNormalAccuracy() = assertEquals(75, AccuracySample(3, 4).percent)
  @Test fun emptySampleIsZero() = assertEquals(0, AccuracySample(0, 0).percent)
  @Test fun impossibleCorrectCountIsBounded() = assertEquals(100, AccuracySample(9, 4).percent)
}

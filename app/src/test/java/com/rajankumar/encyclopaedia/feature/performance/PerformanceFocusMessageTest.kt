package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertTrue
import org.junit.Test

class PerformanceFocusMessageTest {
  @Test fun accuracyFocusPointsToMistakes() = assertTrue(PerformanceFocus.ACCURACY.message().contains("mistakes"))
  @Test fun speedFocusProtectsAccuracy() = assertTrue(PerformanceFocus.SPEED.message().contains("accuracy"))
}

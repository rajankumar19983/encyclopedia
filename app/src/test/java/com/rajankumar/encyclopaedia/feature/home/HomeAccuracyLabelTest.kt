package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeAccuracyLabelTest {
  @Test fun noAttemptsHasNoAccuracy() = assertEquals("No accuracy data yet", homeAccuracyLabel(80, 0))
  @Test fun clampsAndLabelsAccuracy() = assertEquals("100% accuracy", homeAccuracyLabel(120, 5))
}

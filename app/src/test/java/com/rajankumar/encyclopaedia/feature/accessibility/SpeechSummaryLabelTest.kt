package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechSummaryLabelTest {
  @Test fun labelsEmptySummary() = assertEquals("No readable content", SpeechSummary(0, 0, 0).label())
  @Test fun labelsReadableSummary() = assertEquals("10 words • 1 section • about 1 min", SpeechSummary(10, 1, 1).label())
}

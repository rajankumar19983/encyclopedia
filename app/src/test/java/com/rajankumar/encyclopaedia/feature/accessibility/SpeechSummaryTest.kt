package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechSummaryTest {
  @Test fun summarizesShortContent() { val summary = speechSummary("CPU scheduling basics"); assertEquals(3, summary.words); assertEquals(1, summary.sections); assertEquals(1, summary.estimatedMinutes) }
}

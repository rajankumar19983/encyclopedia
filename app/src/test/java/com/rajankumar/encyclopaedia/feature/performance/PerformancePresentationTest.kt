package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformancePresentationTest {
  @Test
  fun durationFormattingHandlesSecondsAndMinutes() {
    assertEquals("0s", formatDurationMs(-1L))
    assertEquals("59s", formatDurationMs(59_999L))
    assertEquals("1m 0s", formatDurationMs(60_000L))
    assertEquals("2m 5s", formatDurationMs(125_000L))
  }

  @Test
  fun signedPercentAddsPlusOnlyForPositiveValues() {
    assertEquals("+5%", signedPercent(5))
    assertEquals("0%", signedPercent(0))
    assertEquals("-5%", signedPercent(-5))
  }
}

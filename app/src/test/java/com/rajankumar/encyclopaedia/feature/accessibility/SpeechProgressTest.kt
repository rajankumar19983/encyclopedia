package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechProgressTest {
  @Test fun reportsFractionAndLabel() {
    val progress = SpeechProgress(2, 4)
    assertEquals(0.5f, progress.fraction)
    assertEquals("Part 2 of 4", progress.label)
  }
  @Test fun emptyProgressIsSafe() = assertEquals(0f, SpeechProgress(0, 0).fraction)
}
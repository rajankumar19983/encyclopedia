package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertTrue
import org.junit.Test

class RevisionAccessibilityTest {
  @Test fun descriptionIncludesUsefulStudyContext() {
    val text = RevisionQueueItem("1", "Networks", 40, 3).accessibilityDescription()
    assertTrue(text.contains("Networks"))
    assertTrue(text.contains("40 percent accuracy"))
  }
}

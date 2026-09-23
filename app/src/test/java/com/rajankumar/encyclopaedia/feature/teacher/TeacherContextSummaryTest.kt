package com.rajankumar.encyclopaedia.feature.teacher

import org.junit.Assert.assertTrue
import org.junit.Test

class TeacherContextSummaryTest {
  @Test fun structuredContextExplainsNoScreenshotRequirement() {
    val summary = TeacherContext("lesson", "Networks", "Content").summary()
    assertTrue(summary.detail.contains("structured context"))
    assertTrue(summary.detail.contains("not required"))
  }
}

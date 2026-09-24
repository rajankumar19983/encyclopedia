package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertTrue
import org.junit.Test

class HomeDashboardSemanticsTest {
  @Test fun summaryReadsCoreMetrics() {
    val text = homeDashboardAccessibilitySummary(24, 1250, 12, 68)
    assertTrue(text.contains("24 topics"))
    assertTrue(text.contains("1250 questions"))
    assertTrue(text.contains("68 percent"))
  }
  @Test fun emptySummaryExplainsHowToBegin() = assertTrue(homeDashboardAccessibilitySummary(0, 0, 0, 0).contains("import PYQ"))
}

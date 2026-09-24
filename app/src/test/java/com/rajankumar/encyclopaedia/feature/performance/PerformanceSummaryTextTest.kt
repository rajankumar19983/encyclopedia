package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceSummaryTextTest {
  @Test fun emptySummaryExplainsMissingAttempts() = assertEquals("No question attempts recorded yet", PerformanceSummary(0, 0, 0, 10, 0).summaryText())
  @Test fun populatedSummaryIncludesCoreMetrics() = assertEquals("60% accuracy • 50% coverage • 10 attempts", PerformanceSummary(10, 6, 5, 10, 0).summaryText())
}

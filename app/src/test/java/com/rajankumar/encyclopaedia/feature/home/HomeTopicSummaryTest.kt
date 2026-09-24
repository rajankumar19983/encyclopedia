package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeTopicSummaryTest {
  @Test fun formatsTopicCount() = assertEquals("12 topics", HomeTopicSummary(12, 4).label)
  @Test fun exposesProgress() = assertEquals(50, HomeTopicSummary(10, 5).progress.percent)
}

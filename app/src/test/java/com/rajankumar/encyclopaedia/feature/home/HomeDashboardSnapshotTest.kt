package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeDashboardSnapshotTest {
  @Test fun impossibleCountsAreNormalizedBeforeDashboardCalculation() {
    val snapshot = HomeDashboardSnapshot(-2, 5, 3, 8, 9).normalized()
    assertEquals(HomeDashboardSnapshot(0, 5, 3, 3, 5), snapshot)
    assertEquals("100%", snapshot.dashboard().stats.last().value)
  }
}

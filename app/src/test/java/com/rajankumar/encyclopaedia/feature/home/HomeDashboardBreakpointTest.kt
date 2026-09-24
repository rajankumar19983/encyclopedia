package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeDashboardBreakpointTest {
  @Test fun compactBelowTabletWidth() = assertEquals(HomeDashboardSize.COMPACT, homeDashboardSize(699))
  @Test fun mediumOnTablet() = assertEquals(HomeDashboardSize.MEDIUM, homeDashboardSize(900))
  @Test fun expandedOnWideTablet() = assertEquals(HomeDashboardSize.EXPANDED, homeDashboardSize(1200))
  @Test fun statColumnsFollowAvailableWidth() {
    assertEquals(1, HomeDashboardSize.COMPACT.statColumns())
    assertEquals(2, HomeDashboardSize.MEDIUM.statColumns())
    assertEquals(4, HomeDashboardSize.EXPANDED.statColumns())
  }
}

package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeDashboardBreakpointTest {
  @Test fun compactBelowTabletWidth() = assertEquals(HomeDashboardSize.COMPACT, homeDashboardSize(699))
  @Test fun mediumOnTablet() = assertEquals(HomeDashboardSize.MEDIUM, homeDashboardSize(900))
  @Test fun expandedOnWideTablet() = assertEquals(HomeDashboardSize.EXPANDED, homeDashboardSize(1200))
}

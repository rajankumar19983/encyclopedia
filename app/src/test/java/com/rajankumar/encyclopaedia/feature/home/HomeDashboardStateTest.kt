package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.*
import org.junit.Test

class HomeDashboardStateTest {
  @Test fun dashboardUsesSingleConsistentSnapshot() {
    val state = buildHomeDashboardState(4, 20, 10, 8, 12)
    assertEquals(HomeStudyStatus.BUILDING_COVERAGE, state.status)
    assertEquals("Expand your coverage", state.recommendation.title)
    assertEquals("80%", state.stats.last().value)
    assertTrue(state.motivation.isNotBlank())
    assertFalse(state.emptyState.visible)
  }

  @Test fun emptyDashboardExposesOnboarding() = assertTrue(buildHomeDashboardState(0, 0, 0, 0, 0).emptyState.visible)
}

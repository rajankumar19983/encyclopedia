package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeQuickActionTest {
  @Test fun dashboardExposesCoreStudyDestinations() {
    val actions = homeQuickActions()
    assertEquals(HomeQuickAction.entries.toSet(), actions.map { it.action }.toSet())
    assertTrue(actions.all { it.title.isNotBlank() && it.detail.isNotBlank() })
  }
}

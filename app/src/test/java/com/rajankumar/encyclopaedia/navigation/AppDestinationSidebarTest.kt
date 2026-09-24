package com.rajankumar.encyclopaedia.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class AppDestinationSidebarTest {
  @Test fun tabletSidebarMatchesDashboardDesign() {
    assertEquals(
      listOf("Home", "Topics & Knowledge", "Questions", "Revision", "Daily Routine", "PYQ Papers", "Performance", "Question Editor", "Import / OCR", "Backup & Restore", "Settings"),
      AppDestination.entries.filter(AppDestination::showInTabletSidebar).map { it.label }
    )
  }
}

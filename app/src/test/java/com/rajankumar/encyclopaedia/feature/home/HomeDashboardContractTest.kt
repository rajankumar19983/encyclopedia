package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.*
import org.junit.Test

class HomeDashboardContractTest {
  @Test fun referenceDashboardKeepsFourSummaryCards() = assertEquals(4, HomeDashboardContract.summaryTitles.size)
  @Test fun referenceDashboardKeepsCoreSections() = assertEquals(listOf("Today's Plan", "Calendar", "Upcoming", "Quick Actions"), HomeDashboardContract.sectionTitles)
  @Test fun referenceDashboardKeepsQuickActions() = assertEquals(listOf("Practice MCQs", "Import PYQs", "Add Notes", "Start Revision"), HomeDashboardContract.quickActionTitles)
  @Test fun searchHintCoversStudyContent() = assertTrue(HomeDashboardContract.searchHint.contains("topics"))
}

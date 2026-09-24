package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeQuickActionTargetTest {
  @Test fun labelsMatchDashboardReference() = assertEquals(HomeDashboardContract.quickActionTitles, HomeQuickActionTarget.entries.map { it.label })
}

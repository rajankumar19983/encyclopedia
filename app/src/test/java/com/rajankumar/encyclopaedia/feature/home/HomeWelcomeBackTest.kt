package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertTrue
import org.junit.Test

class HomeWelcomeBackTest {
  @Test fun sameDayEncouragesMomentum() = assertTrue(homeReturnMessage(0).contains("momentum"))
  @Test fun longerBreakUsesManageableRestart() = assertTrue(homeReturnMessage(5).contains("manageable"))
}

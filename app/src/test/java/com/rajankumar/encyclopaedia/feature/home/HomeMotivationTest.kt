package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertTrue
import org.junit.Test

class HomeMotivationTest {
  @Test fun everyStudyStatusHasGuidance() {
    HomeStudyStatus.entries.forEach { status ->
      assertTrue(homeMotivation(status).isNotBlank())
    }
  }
}

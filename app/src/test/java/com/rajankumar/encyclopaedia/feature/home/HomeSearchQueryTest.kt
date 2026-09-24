package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.*
import org.junit.Test

class HomeSearchQueryTest {
  @Test fun normalizesWhitespace() = assertEquals("operating systems", HomeSearchQuery("  operating   systems ").normalized)
  @Test fun blankQueryIsInactive() = assertFalse(HomeSearchQuery("   ").active)
}

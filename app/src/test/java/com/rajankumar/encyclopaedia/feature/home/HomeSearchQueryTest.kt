package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.*
import org.junit.Test

class HomeSearchQueryTest {
  @Test fun normalizesWhitespace() = assertEquals("operating systems", HomeSearchQuery("  operating   systems ").normalized)
  @Test fun blankQueryIsInactive() = assertFalse(HomeSearchQuery("   ").active)
  @Test fun singleCharacterQueryIsInactive() = assertFalse(HomeSearchQuery("a").active)
  @Test fun twoCharacterQueryIsActive() = assertTrue(HomeSearchQuery("os").active)
  @Test fun queryIsBounded() = assertEquals(120, HomeSearchQuery("a".repeat(200)).normalized.length)
}

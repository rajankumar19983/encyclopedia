package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class SpeechEmptyStateTest {
  @Test fun blankTextReturnsGuidance() = assertNotNull(speechEmptyState("   "))
  @Test fun readableTextNeedsNoGuidance() = assertNull(speechEmptyState("Operating systems"))
}

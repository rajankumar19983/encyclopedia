package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SpeechMilestoneTest {
  @Test fun completeRequiresEveryCapability() = assertTrue(SpeechMilestone(true, true, true, true, true).complete)
  @Test fun missingCapabilityKeepsMilestoneOpen() = assertFalse(SpeechMilestone(true, true, true, false, true).complete)
}
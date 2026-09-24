package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookPalmRejectionTest {
  @Test fun enabledGuidanceMentionsStylus() = assertTrue(palmRejectionGuidance(true).contains("Stylus"))
  @Test fun disabledGuidanceMentionsTouch() = assertTrue(palmRejectionGuidance(false).contains("Touch"))
}

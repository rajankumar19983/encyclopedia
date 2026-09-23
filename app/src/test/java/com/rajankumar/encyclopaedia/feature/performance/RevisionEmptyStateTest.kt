package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertTrue
import org.junit.Test

class RevisionEmptyStateTest {
  @Test fun guidanceReflectsQuestionAvailability() {
    assertTrue(revisionEmptyMessage(false).contains("Add questions"))
    assertTrue(revisionEmptyMessage(true).contains("Keep practising"))
  }
}

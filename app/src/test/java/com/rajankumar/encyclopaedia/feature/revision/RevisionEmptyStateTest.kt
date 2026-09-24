package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertTrue
import org.junit.Test

class RevisionEmptyStateTest {
  @Test fun emptyStateExplainsHowToPopulateQueue() {
    assertTrue(revisionEmptyMessage.contains("automatically"))
    assertTrue(revisionEmptyAction.contains("Practice MCQs"))
  }
}

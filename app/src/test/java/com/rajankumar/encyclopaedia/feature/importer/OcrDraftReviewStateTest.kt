package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrDraftReviewStateTest {
  @Test fun defaultsToPendingReadyAndUnchecked() {
    val state = OcrDraftReviewState()
    assertEquals(OcrReviewDecision.PENDING, state.decision)
    assertEquals(OcrDraftSaveStatus.READY, state.saveStatus)
    assertTrue(state.checklist.checked.isEmpty())
  }
}

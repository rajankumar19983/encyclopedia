package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrReviewSessionTest {
  @Test fun computesPendingReviewCount() {
    val session = OcrReviewSession(5, approved = 2, rejected = 1)
    assertEquals(2, session.pending)
    assertFalse(session.complete)
  }

  @Test fun completesWhenEveryDraftHasDecision() = assertTrue(OcrReviewSession(3, 2, 1).complete)
}

package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrExitGuardTest {
  @Test fun pendingDraftsRequireConfirmation() = assertTrue(OcrReviewSession(2, 1, 0).exitGuard().requiresConfirmation)
  @Test fun completedReviewCanExitDirectly() = assertFalse(OcrReviewSession(2, 1, 1).exitGuard().requiresConfirmation)
}

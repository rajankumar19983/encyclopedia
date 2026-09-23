package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReviewAccessibilityDescriptionTest {
  @Test fun pendingWarningsAreAnnounced() {
    val presentation = BackupRestoreReviewSessionPresentation("Warnings", "Review before restore.", emptyList(), "Review warnings", false, true)
    assertTrue(presentation.accessibilityDescription().contains("acknowledgement is required"))
  }

  @Test fun blockedRestoreIsAnnounced() {
    val presentation = BackupRestoreReviewSessionPresentation("Blocked", "Integrity errors found.", emptyList(), "Restore unavailable", false, false)
    assertTrue(presentation.accessibilityDescription().contains("Restore is unavailable"))
  }
}

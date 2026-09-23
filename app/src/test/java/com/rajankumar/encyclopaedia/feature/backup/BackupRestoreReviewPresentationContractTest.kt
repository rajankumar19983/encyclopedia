package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReviewPresentationContractTest {
  @Test fun blockedPresentationExplainsWhyActionIsUnavailable() {
    val presentation = BackupRestoreReviewSessionPresentation(
      headline = "1 blocking issue found",
      description = "Resolve or choose another backup because restore is blocked.",
      badges = listOf(BackupRestoreReviewBadge("Blocking", 1)),
      actionLabel = "Restore unavailable",
      actionEnabled = false,
      acknowledgementRequired = false
    )
    assertEquals("Restore unavailable", presentation.actionLabel)
    assertFalse(presentation.actionEnabled)
    assertTrue(presentation.badges.any { it.label == "Blocking" })
  }
}

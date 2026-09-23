package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreActionTextTest {
  @Test fun enabledRestoreUsesSafetyBackupAction() {
    val presentation = BackupRestoreReviewSessionPresentation("Ready", "", emptyList(), "Restore backup", true, false)
    assertEquals("Create safety backup & restore", presentation.restoreActionText())
  }

  @Test fun pendingWarningUsesReviewAction() {
    val presentation = BackupRestoreReviewSessionPresentation("Warnings", "", emptyList(), "Review warnings", false, true)
    assertEquals("Review warnings to continue", presentation.restoreActionText())
  }

  @Test fun blockedRestoreKeepsUnavailableLabel() {
    val presentation = BackupRestoreReviewSessionPresentation("Blocked", "", emptyList(), "Restore unavailable", false, false)
    assertEquals("Restore unavailable", presentation.restoreActionText())
  }
}

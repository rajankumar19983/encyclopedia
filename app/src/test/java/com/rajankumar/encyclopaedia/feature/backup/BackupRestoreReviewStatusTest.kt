package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreReviewStatusTest {
  private fun model(errors: Int, warnings: Int) = BackupRestoreReviewModel(
    title = "Review",
    message = "",
    restoreEnabled = errors == 0,
    sections = listOf(BackupRestoreReviewSection(BackupRestoreIssueGroup.CONTENT, errors, warnings, emptyList()))
  )

  @Test fun cleanStatus() = assertEquals(BackupRestoreReviewStatus.CLEAN, model(0, 0).status())
  @Test fun warningStatus() = assertEquals(BackupRestoreReviewStatus.WARNINGS, model(0, 1).status())
  @Test fun blockedStatus() = assertEquals(BackupRestoreReviewStatus.BLOCKED, model(1, 1).status())
}

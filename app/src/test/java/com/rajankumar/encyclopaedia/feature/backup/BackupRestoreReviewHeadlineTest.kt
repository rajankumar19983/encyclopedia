package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreReviewHeadlineTest {
  private fun model(errors: Int, warnings: Int) = BackupRestoreReviewModel(
    "Review", "", errors == 0,
    listOf(BackupRestoreReviewSection(BackupRestoreIssueGroup.CONTENT, errors, warnings, emptyList()))
  )

  @Test fun cleanHeadline() = assertEquals("Backup is ready to restore", model(0, 0).reviewHeadline())
  @Test fun warningHeadline() = assertEquals("2 warnings to review", model(0, 2).reviewHeadline())
  @Test fun blockedHeadline() = assertEquals("1 blocking issue found", model(1, 0).reviewHeadline())
}

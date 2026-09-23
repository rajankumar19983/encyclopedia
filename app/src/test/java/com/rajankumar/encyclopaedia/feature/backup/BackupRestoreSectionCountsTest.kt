package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreSectionCountsTest {
  @Test fun countsBlockingAndWarningOnlySections() {
    val model = BackupRestoreReviewModel(
      "Review", "", false,
      listOf(
        BackupRestoreReviewSection(BackupRestoreIssueGroup.CONTENT, 0, 2, emptyList()),
        BackupRestoreReviewSection(BackupRestoreIssueGroup.RELATIONSHIPS, 1, 1, emptyList())
      )
    )
    assertEquals(1, model.blockingSectionCount)
    assertEquals(1, model.warningOnlySectionCount)
  }
}

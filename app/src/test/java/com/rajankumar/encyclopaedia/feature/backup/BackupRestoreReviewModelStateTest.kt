package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReviewModelStateTest {
  @Test fun modelStateHelpersReflectCounts() {
    val blocked = BackupRestoreReviewModel("Review", "", false, listOf(BackupRestoreReviewSection(BackupRestoreIssueGroup.CONTENT, 1, 1, emptyList())))
    assertTrue(blocked.hasBlockingRestoreIssues())
    assertTrue(blocked.hasRestoreWarnings())
    assertFalse(blocked.isCleanForRestore())

    val clean = BackupRestoreReviewModel("Ready", "", true, emptyList())
    assertTrue(clean.isCleanForRestore())
  }
}

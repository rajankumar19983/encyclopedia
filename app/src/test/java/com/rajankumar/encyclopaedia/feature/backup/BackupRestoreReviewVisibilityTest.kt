package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReviewVisibilityTest {
  @Test fun warningReviewVisibilityMatchesCounts() {
    val model = BackupRestoreReviewModel(
      "Review", "", true,
      listOf(BackupRestoreReviewSection(BackupRestoreIssueGroup.CONTENT, 0, 1, emptyList()))
    )
    assertTrue(model.hasIssues)
    assertTrue(model.showWarningReview)
    assertFalse(model.showBlockingReview)
  }
}

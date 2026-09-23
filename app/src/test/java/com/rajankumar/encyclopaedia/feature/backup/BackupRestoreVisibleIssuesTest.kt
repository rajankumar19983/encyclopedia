package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreVisibleIssuesTest {
  @Test fun badgesControlIssueVisibility() {
    assertFalse(BackupRestoreReviewSessionPresentation("", "", emptyList(), "", true, false).hasVisibleIssues())
    assertTrue(BackupRestoreReviewSessionPresentation("", "", listOf(BackupRestoreReviewBadge("Warnings", 1)), "", false, true).hasVisibleIssues())
  }
}

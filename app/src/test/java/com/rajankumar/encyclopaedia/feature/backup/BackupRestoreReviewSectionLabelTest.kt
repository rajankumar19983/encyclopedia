package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreReviewSectionLabelTest {
  @Test fun issueCountLabelHandlesSingularAndPlural() {
    assertEquals("1 issue", BackupRestoreReviewSection(BackupRestoreIssueGroup.CONTENT, 1, 0, emptyList()).issueCountLabel())
    assertEquals("2 issues", BackupRestoreReviewSection(BackupRestoreIssueGroup.CONTENT, 1, 1, emptyList()).issueCountLabel())
  }
}

package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreIssueSummaryTextTest {
  @Test fun mixedIssuesAreSummarized() {
    val model = BackupRestoreReviewModel("", "", false, listOf(BackupRestoreReviewSection(BackupRestoreIssueGroup.CONTENT, 2, 3, emptyList())))
    assertEquals("2 blocking, 3 warnings", model.issueSummaryText())
  }
}

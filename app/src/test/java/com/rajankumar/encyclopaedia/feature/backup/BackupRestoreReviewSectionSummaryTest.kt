package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreReviewSectionSummaryTest {
  @Test fun summaryIncludesSeverityCounts() {
    val section = BackupRestoreReviewSection(BackupRestoreIssueGroup.RELATIONSHIPS, 1, 2, emptyList())
    assertEquals("Data relationships: 3 issues, 1 blocking, 2 warnings", section.summaryLabel())
  }
}

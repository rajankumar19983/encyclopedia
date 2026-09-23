package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegritySeverity
import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreReviewFormattingTest {
  @Test
  fun groupsHaveReadableLabels() {
    assertEquals("Backup structure", BackupRestoreIssueGroup.BACKUP_STRUCTURE.label())
    assertEquals("Content", BackupRestoreIssueGroup.CONTENT.label())
    assertEquals("Relationships", BackupRestoreIssueGroup.RELATIONSHIPS.label())
  }

  @Test
  fun sectionSummaryDescribesMixedSeverityCounts() {
    val section = BackupRestoreReviewSection(
      group = BackupRestoreIssueGroup.CONTENT,
      errorCount = 2,
      warningCount = 3,
      messages = emptyList()
    )
    assertEquals("2 blocking issue(s), 3 warning(s)", section.summaryText())
  }

  @Test
  fun emptySectionSummaryIsExplicit() {
    val section = BackupRestoreReviewSection(
      group = BackupRestoreIssueGroup.RELATIONSHIPS,
      errorCount = 0,
      warningCount = 0,
      messages = emptyList()
    )
    assertEquals("No issues", section.summaryText())
  }
}

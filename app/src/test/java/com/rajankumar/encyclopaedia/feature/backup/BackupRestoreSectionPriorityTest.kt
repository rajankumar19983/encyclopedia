package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreSectionPriorityTest {
  @Test fun errorsMakeSectionBlocking() {
    val section = BackupRestoreReviewSection(BackupRestoreIssueGroup.CONTENT, 1, 2, emptyList())
    assertEquals(BackupRestoreSectionPriority.BLOCKING, section.priority)
  }

  @Test fun warningOnlySectionHasWarningPriority() {
    val section = BackupRestoreReviewSection(BackupRestoreIssueGroup.CONTENT, 0, 2, emptyList())
    assertEquals(BackupRestoreSectionPriority.WARNING, section.priority)
  }
}

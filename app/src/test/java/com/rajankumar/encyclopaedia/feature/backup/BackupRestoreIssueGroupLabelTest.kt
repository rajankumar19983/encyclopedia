package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreIssueGroupLabelTest {
  @Test fun groupsHaveUserFacingLabels() {
    assertEquals("Backup structure", BackupRestoreIssueGroup.BACKUP_STRUCTURE.label())
    assertEquals("Study content", BackupRestoreIssueGroup.CONTENT.label())
    assertEquals("Data relationships", BackupRestoreIssueGroup.RELATIONSHIPS.label())
  }
}

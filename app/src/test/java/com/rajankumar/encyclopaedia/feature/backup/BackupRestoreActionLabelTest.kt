package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreActionLabelTest {
  @Test fun readinessHasClearActionLabels() {
    assertEquals("Restore backup", BackupRestoreReadiness.READY.actionLabel())
    assertEquals("Review warnings", BackupRestoreReadiness.REVIEW_WARNINGS.actionLabel())
    assertEquals("Restore unavailable", BackupRestoreReadiness.BLOCKED_BY_INTEGRITY.actionLabel())
  }
}

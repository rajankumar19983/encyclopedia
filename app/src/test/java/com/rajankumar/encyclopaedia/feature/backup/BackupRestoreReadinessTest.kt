package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReadinessTest {
  @Test fun supportedValidBackupIsReady() = assertTrue(restoreReadiness(BACKUP_FORMAT_VERSION, true).ready)
  @Test fun invalidBackupIsBlocked() = assertFalse(restoreReadiness(BACKUP_FORMAT_VERSION, false).ready)
  @Test fun futureBackupIsBlocked() = assertFalse(restoreReadiness(BACKUP_FORMAT_VERSION + 1, true).ready)
}

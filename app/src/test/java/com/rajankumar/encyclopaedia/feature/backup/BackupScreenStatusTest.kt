package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupScreenStatusTest {
  @Test fun noBackupStatusIsClear() = assertEquals("No recovery point yet", backupScreenStatus(BackupHealth.NO_BACKUP).headline)
  @Test fun staleBackupRequestsAction() = assertEquals("Backup recommended", backupScreenStatus(BackupHealth.NEEDS_BACKUP).headline)
}

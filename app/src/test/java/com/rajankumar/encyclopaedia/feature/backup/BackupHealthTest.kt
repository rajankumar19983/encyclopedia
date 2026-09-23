package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupHealthTest {
  @Test fun healthReflectsBackupFreshness() {
    val day = 86_400_000L
    assertEquals(BackupHealth.NO_BACKUP, backupHealth(null, 10 * day))
    assertEquals(BackupHealth.HEALTHY, backupHealth(9 * day, 10 * day))
    assertEquals(BackupHealth.NEEDS_BACKUP, backupHealth(1 * day, 10 * day))
  }
}

package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupActionAvailabilityTest {
  @Test fun restoreRequiresBackup() = assertFalse(backupActionAvailability(false, false).canRestore)
  @Test fun busyStateBlocksActions() {
    val state = backupActionAvailability(true, true)
    assertFalse(state.canCreate)
    assertFalse(state.canRestore)
  }
  @Test fun idleBackupAllowsBothActions() = assertTrue(backupActionAvailability(true, false).canRestore)
}

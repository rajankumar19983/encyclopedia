package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.*
import org.junit.Test

class BackupPreflightPresentationTest {
  @Test fun supportedBackupPresentationEnablesRestore() {
    val view = BackupPreflight(BackupCompatibility.SUPPORTED, 12, true, true).presentation()
    assertEquals("Backup ready", view.title)
    assertEquals(12, view.recordCount)
    assertTrue(view.restoreEnabled)
  }

  @Test fun incompatibleBackupPresentationDisablesRestore() {
    val view = BackupPreflight(BackupCompatibility.TOO_NEW, 12, true, false).presentation()
    assertFalse(view.restoreEnabled)
    assertTrue(view.description.contains("newer"))
  }
}

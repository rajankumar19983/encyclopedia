package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupCompatibilityTest {
  @Test fun classifiesSupportedAndUnsupportedVersions() {
    assertEquals(BackupCompatibility.TOO_OLD, backupCompatibility(0))
    assertEquals(BackupCompatibility.SUPPORTED, backupCompatibility(1))
    assertEquals(BackupCompatibility.SUPPORTED, backupCompatibility(BACKUP_FORMAT_VERSION))
    assertEquals(BackupCompatibility.TOO_NEW, backupCompatibility(BACKUP_FORMAT_VERSION + 1))
  }
}

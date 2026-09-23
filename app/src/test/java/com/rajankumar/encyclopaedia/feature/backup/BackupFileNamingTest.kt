package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupFileNamingTest {
  @Test
  fun fileNameUsesStableUtcTimestampAndCurrentRestoreMetadata() {
    assertEquals(
      "encyclopaedia_backup_1970-01-01_000000_manual_v2.encbackup",
      backupFileName(0L)
    )
    assertEquals(
      "encyclopaedia_backup_1970-01-01_000000_automatic_v2.encbackup",
      backupFileName(0L, BackupType.AUTOMATIC)
    )
  }

  @Test
  fun supportedNameAcceptsLegacyAndCurrentDiscoverableFormats() {
    assertTrue(isSupportedBackupFileName("encyclopaedia_backup_2026-09-19_172504_automatic_v1.encbackup"))
    assertTrue(isSupportedBackupFileName("encyclopaedia_backup_2026-09-19_172504_manual_v2.encbackup"))
    assertFalse(isSupportedBackupFileName("backup.encbackup"))
    assertFalse(isSupportedBackupFileName("encyclopaedia_backup_2026-09-19.json"))
    assertFalse(isSupportedBackupFileName("encyclopaedia_backup_2026-09-19_172504_unknown_v1.encbackup"))
  }
}

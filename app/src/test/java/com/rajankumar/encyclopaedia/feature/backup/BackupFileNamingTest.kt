package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupFileNamingTest {
  @Test
  fun fileNameUsesStableUtcTimestamp() {
    assertEquals(
      "encyclopaedia-backup-2026-09-19_17-25-04.json",
      backupFileName(1_758_302_704_000L)
    )
  }

  @Test
  fun supportedNameRequiresPrefixAndJsonExtension() {
    assertTrue(isSupportedBackupFileName("encyclopaedia-backup-2026-09-19_17-25-04.json"))
    assertFalse(isSupportedBackupFileName("backup.json"))
    assertFalse(isSupportedBackupFileName("encyclopaedia-backup-2026-09-19.txt"))
  }
}

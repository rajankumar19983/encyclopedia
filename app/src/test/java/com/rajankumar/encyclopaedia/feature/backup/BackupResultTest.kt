package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupResultTest {
  @Test fun labelsBackupResults() { assertEquals("Backup complete", BackupResult.SUCCESS.label()); assertEquals("Backup failed", BackupResult.FAILED.label()); assertEquals("Backup cancelled", BackupResult.CANCELLED.label()) }
}

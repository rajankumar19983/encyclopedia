package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertTrue
import org.junit.Test

class BackupFreshnessGuidanceTest {
  @Test fun staleBackupPromptsNewBackup() = assertTrue(BackupAge.STALE.guidance().contains("new backup"))
  @Test fun todaysBackupIsCurrent() = assertTrue(BackupAge.TODAY.guidance().contains("current"))
}

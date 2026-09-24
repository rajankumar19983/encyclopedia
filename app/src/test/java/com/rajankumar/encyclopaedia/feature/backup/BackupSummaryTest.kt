package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupSummaryTest {
  @Test fun combinesBackupMetadata() = assertEquals("3 items • 2 KB • Recent backup", BackupSummary(3, 2048, BackupAge.RECENT).label())
}

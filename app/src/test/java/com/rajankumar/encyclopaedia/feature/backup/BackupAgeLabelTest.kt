package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupAgeLabelTest {
  @Test fun labelsBackupAge() { assertEquals("Backed up today", BackupAge.TODAY.label()); assertEquals("Recent backup", BackupAge.RECENT.label()); assertEquals("Backup is outdated", BackupAge.STALE.label()) }
}

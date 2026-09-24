package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupSizeLabelTest {
  @Test fun formatsBackupSizes() { assertEquals("0 B", backupSizeLabel(-1)); assertEquals("2 KB", backupSizeLabel(2048)); assertEquals("3 MB", backupSizeLabel(3L * 1024 * 1024)) }
}

package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupScreenCopyTest {
  @Test fun copyNamesTheFeature() = assertEquals("Backup & Restore", defaultBackupScreenCopy.title)
  @Test fun copyExplainsLocalDataProtection() = assertTrue(defaultBackupScreenCopy.subtitle.contains("local study data"))
}

package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertTrue
import org.junit.Test

class BackupScreenAccessibilityTest {
  @Test fun accessibilityTextNamesFeatureAndState() {
    val text = backupScreenAccessibility(BackupHealth.NO_BACKUP)
    assertTrue(text.contains("Backup and restore"))
    assertTrue(text.contains("No recovery point"))
  }
}

package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupSafetyCopyTest {
  @Test fun safetyGuidanceCoversThreeRisks() = assertEquals(3, backupSafetyPoints.size)
  @Test fun restoreGuidanceIsExplicit() = assertTrue(backupSafetyPoints.any { it.contains("restore") })
}

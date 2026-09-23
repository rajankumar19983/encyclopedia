package com.rajankumar.encyclopaedia.data.local

import org.junit.Assert.assertTrue
import org.junit.Test

class MigrationSafetyTest {
  @Test fun migrationPolicyProtectsExistingStudyData() = assertTrue(currentMigrationSafety().safeForExistingData)
}

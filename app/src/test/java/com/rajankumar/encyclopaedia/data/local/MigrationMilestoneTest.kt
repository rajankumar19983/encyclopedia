package com.rajankumar.encyclopaedia.data.local

import org.junit.Assert.assertTrue
import org.junit.Test

class MigrationMilestoneTest {
  @Test fun migrationMilestoneIsComplete() = assertTrue(currentMigrationMilestone.complete)
}

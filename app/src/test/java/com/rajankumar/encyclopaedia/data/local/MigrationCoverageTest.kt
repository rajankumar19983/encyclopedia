package com.rajankumar.encyclopaedia.data.local

import org.junit.Assert.assertTrue
import org.junit.Test

class MigrationCoverageTest {
  @Test fun everyReleasedSchemaCanReachCurrentVersion() = assertTrue(currentMigrationCoverage().complete)
}

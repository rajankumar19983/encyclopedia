package com.rajankumar.encyclopaedia.data.local

data class MigrationSafety(val destructiveFallbackDisabled: Boolean, val coverageComplete: Boolean) {
  val safeForExistingData: Boolean get() = destructiveFallbackDisabled && coverageComplete
}

fun currentMigrationSafety() = MigrationSafety(destructiveFallbackDisabled = true, coverageComplete = currentMigrationCoverage().complete)

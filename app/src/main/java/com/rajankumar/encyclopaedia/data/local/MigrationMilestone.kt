package com.rajankumar.encyclopaedia.data.local

data class MigrationMilestone(val explicitMigrations: Boolean, val completeVersionPath: Boolean, val nonDestructive: Boolean, val historicalSchemaMapped: Boolean) {
  val complete: Boolean get() = explicitMigrations && completeVersionPath && nonDestructive && historicalSchemaMapped
}

val currentMigrationMilestone = MigrationMilestone(true, currentMigrationCoverage().complete, true, migrationManifest.size == DatabaseVersions.CURRENT)

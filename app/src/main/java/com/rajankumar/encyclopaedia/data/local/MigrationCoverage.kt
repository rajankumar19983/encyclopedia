package com.rajankumar.encyclopaedia.data.local

data class MigrationCoverage(val from: Int, val to: Int, val path: List<Pair<Int, Int>>) {
  val complete: Boolean get() = path.firstOrNull()?.first == from && path.lastOrNull()?.second == to && path.zipWithNext().all { it.first.second == it.second.first }
}

fun currentMigrationCoverage() = MigrationCoverage(1, DatabaseVersions.CURRENT, ALL_MIGRATIONS.map { it.startVersion to it.endVersion })

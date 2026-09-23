package com.rajankumar.encyclopaedia.data.local

data class MigrationRelease(val version: Int, val feature: String)

val migrationManifest = listOf(
  MigrationRelease(1, "Knowledge, lessons and questions"),
  MigrationRelease(2, "Question topics and attempt history"),
  MigrationRelease(3, "Daily study planner"),
  MigrationRelease(4, "Notebook pages, layers and vector strokes"),
)

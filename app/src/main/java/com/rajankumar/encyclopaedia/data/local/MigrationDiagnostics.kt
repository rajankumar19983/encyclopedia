package com.rajankumar.encyclopaedia.data.local

data class MigrationDiagnostics(val steps: Int, val statements: Int, val currentVersion: Int)

fun migrationDiagnostics() = MigrationDiagnostics(
  steps = ALL_MIGRATIONS.size,
  statements = MigrationSql.practiceTables.size +
    plannerMigrationSql.size +
    notebookMigrationSql.size +
    knowledgeProvenanceMigrationSql.size,
  currentVersion = DatabaseVersions.CURRENT,
)

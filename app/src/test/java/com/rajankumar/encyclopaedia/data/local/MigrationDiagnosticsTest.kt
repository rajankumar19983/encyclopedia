package com.rajankumar.encyclopaedia.data.local

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MigrationDiagnosticsTest {
  @Test fun diagnosticsDescribeNonEmptyMigrationChain() {
    val diagnostics = migrationDiagnostics()
    val expectedStatements = MigrationSql.practiceTables.size +
      plannerMigrationSql.size +
      notebookMigrationSql.size +
      knowledgeProvenanceMigrationSql.size

    assertEquals(ALL_MIGRATIONS.size, diagnostics.steps)
    assertEquals(DatabaseVersions.CURRENT, diagnostics.currentVersion)
    assertEquals(expectedStatements, diagnostics.statements)
    assertTrue(diagnostics.statements >= diagnostics.steps)
  }
}

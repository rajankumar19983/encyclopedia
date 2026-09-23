package com.rajankumar.encyclopaedia.data.local

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MigrationDiagnosticsTest {
  @Test fun diagnosticsDescribeNonEmptyMigrationChain() {
    val diagnostics = migrationDiagnostics()
    assertEquals(3, diagnostics.steps)
    assertEquals(4, diagnostics.currentVersion)
    assertTrue(diagnostics.statements > diagnostics.steps)
  }
}

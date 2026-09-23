package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreIssueSummaryTest {
  private fun inspection(issues: Set<IntegrityIssue>) = BackupInspection(
    preflight = BackupPreflight(BackupCompatibility.SUPPORTED, 4, true, true),
    integrity = IntegrityReport(issues, 4)
  )

  @Test
  fun separatesErrorsAndWarnings() {
    val summary = inspection(setOf(IntegrityIssue.IDS, IntegrityIssue.FIELDS)).restoreIssueSummary()

    assertEquals(1, summary.errorCount)
    assertEquals(1, summary.warningCount)
    assertTrue(summary.hasErrors)
    assertTrue(summary.hasWarnings)
  }

  @Test
  fun healthyInspectionHasNoIssues() {
    val summary = inspection(emptySet()).restoreIssueSummary()

    assertEquals(0, summary.errorCount)
    assertEquals(0, summary.warningCount)
    assertFalse(summary.hasErrors)
    assertFalse(summary.hasWarnings)
  }

  @Test
  fun incompatibleInspectionWithoutIntegrityHasNoIntegrityIssues() {
    val summary = BackupInspection(
      preflight = BackupPreflight(BackupCompatibility.TOO_NEW, 0, false, false),
      integrity = null
    ).restoreIssueSummary()

    assertEquals(0, summary.errorCount)
    assertEquals(0, summary.warningCount)
  }
}

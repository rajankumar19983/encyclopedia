package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.*
import org.junit.Test

class BackupInspectionTest {
  @Test
  fun supportedHealthyBackupCanRestore() {
    val inspection = BackupInspection(
      preflight = BackupPreflight(BackupCompatibility.SUPPORTED, 4, true, true),
      integrity = IntegrityReport(emptySet(), 4)
    )
    assertTrue(inspection.canRestore)
  }

  @Test
  fun blockingIntegrityIssuePreventsRestore() {
    val inspection = BackupInspection(
      preflight = BackupPreflight(BackupCompatibility.SUPPORTED, 4, true, true),
      integrity = IntegrityReport(setOf(IntegrityIssue.IDS), 4)
    )
    assertFalse(inspection.canRestore)
  }

  @Test
  fun incompatiblePreflightPreventsRestoreWithoutIntegrityReport() {
    val inspection = BackupInspection(
      preflight = BackupPreflight(BackupCompatibility.TOO_NEW, 4, true, false),
      integrity = null
    )
    assertFalse(inspection.canRestore)
  }
}

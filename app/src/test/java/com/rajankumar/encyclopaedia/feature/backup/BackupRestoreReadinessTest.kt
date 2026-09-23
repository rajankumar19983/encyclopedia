package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreReadinessTest {
  private fun inspection(
    compatibility: BackupCompatibility = BackupCompatibility.SUPPORTED,
    issues: Set<IntegrityIssue> = emptySet()
  ) = BackupInspection(
    preflight = BackupPreflight(
      compatibility = compatibility,
      recordCount = 3,
      canInspect = true,
      canRestore = compatibility == BackupCompatibility.SUPPORTED
    ),
    integrity = if (compatibility == BackupCompatibility.SUPPORTED) IntegrityReport(issues, 3) else null
  )

  @Test
  fun healthyBackupIsReady() {
    assertEquals(BackupRestoreReadiness.READY, inspection().restoreReadiness())
  }

  @Test
  fun warningBackupRequiresReview() {
    assertEquals(
      BackupRestoreReadiness.REVIEW_WARNINGS,
      inspection(issues = setOf(IntegrityIssue.FIELDS)).restoreReadiness()
    )
  }

  @Test
  fun blockingIssueBlocksByIntegrity() {
    assertEquals(
      BackupRestoreReadiness.BLOCKED_BY_INTEGRITY,
      inspection(issues = setOf(IntegrityIssue.IDS)).restoreReadiness()
    )
  }

  @Test
  fun incompatibleBackupBlocksBeforeIntegrity() {
    assertEquals(
      BackupRestoreReadiness.BLOCKED_BY_COMPATIBILITY,
      inspection(BackupCompatibility.TOO_NEW).restoreReadiness()
    )
  }
}

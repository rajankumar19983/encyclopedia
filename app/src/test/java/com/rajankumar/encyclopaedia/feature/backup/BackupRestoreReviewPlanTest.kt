package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReviewPlanTest {
  @Test fun warningPlanRequiresReviewAndAllowsRestore() {
    val inspection = BackupInspection(
      BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true),
      IntegrityReport(setOf(IntegrityIssue.FIELDS), 8)
    )
    val plan = inspection.restoreReviewPlan()
    assertEquals(BackupRestoreReadiness.REVIEW_WARNINGS, plan.readiness)
    assertEquals(BackupRestoreRisk.CAUTION, plan.risk)
    assertEquals("Review warnings", plan.actionLabel)
    assertTrue(plan.restoreEnabled)
  }
}

package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreGateTest {
  private fun inspection(issues: Set<IntegrityIssue>) = BackupInspection(
    BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true),
    IntegrityReport(issues, 8)
  )

  @Test fun warningsCanProceedAfterReview() {
    val gate = inspection(setOf(IntegrityIssue.FIELDS)).restoreGate()
    assertTrue(gate.canProceed)
    assertTrue(gate.needsReview)
  }

  @Test fun errorsCannotProceed() {
    val gate = inspection(setOf(IntegrityIssue.IDS)).restoreGate()
    assertFalse(gate.canProceed)
    assertFalse(gate.needsReview)
  }
}

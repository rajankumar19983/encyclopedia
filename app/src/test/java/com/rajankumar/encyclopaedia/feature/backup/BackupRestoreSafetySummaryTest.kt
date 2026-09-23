package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreSafetySummaryTest {
  private fun inspection(issues: Set<IntegrityIssue>) = BackupInspection(BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true), IntegrityReport(issues, 8))

  @Test fun cleanSummary() = assertEquals("No restore safety issues", inspection(emptySet()).restoreSafetySummary())
  @Test fun warningSummary() = assertEquals("1 warning requires review", inspection(setOf(IntegrityIssue.FIELDS)).restoreSafetySummary())
  @Test fun blockingSummary() = assertEquals("1 blocking issue", inspection(setOf(IntegrityIssue.IDS)).restoreSafetySummary())
}

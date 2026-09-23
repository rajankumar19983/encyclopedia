package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReviewModelTest {
  private fun inspection(issues: Set<IntegrityIssue>) = BackupInspection(
    preflight = BackupPreflight(BackupCompatibility.SUPPORTED, 6, true, true),
    integrity = IntegrityReport(issues, 6)
  )

  @Test
  fun modelAggregatesIssueCountsAcrossSections() {
    val model = inspection(
      setOf(IntegrityIssue.IDS, IntegrityIssue.FIELDS, IntegrityIssue.QUESTION_TOPICS)
    ).restoreReviewModel()

    assertEquals(3, model.issueCount)
    assertEquals(1, model.blockingIssueCount)
    assertEquals(2, model.warningCount)
    assertFalse(model.restoreEnabled)
    assertEquals("Backup integrity check failed", model.title)
  }

  @Test
  fun warningOnlyModelKeepsRestoreEnabled() {
    val model = inspection(setOf(IntegrityIssue.FIELDS)).restoreReviewModel()

    assertTrue(model.restoreEnabled)
    assertEquals(1, model.warningCount)
    assertEquals("Review backup warnings", model.title)
  }
}

package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReviewSectionsIntegrationTest {
  @Test fun blockingSectionsArePresentedBeforeWarningOnlySections() {
    val inspection = BackupInspection(
      BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true),
      IntegrityReport(setOf(IntegrityIssue.FIELDS, IntegrityIssue.STUDY_RELATIONSHIPS), 8)
    )
    val sections = inspection.restoreReviewSections().orderedForRestoreReview()
    assertTrue(sections.first().hasBlockingIssues)
    assertEquals(2, sections.sumOf { it.errorCount + it.warningCount })
  }
}

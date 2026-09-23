package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.*
import org.junit.Test

class BackupRestoreReviewTest {
  @Test
  fun warningReviewKeepsRestoreEnabled() {
    val review = BackupInspection(
      preflight = BackupPreflight(BackupCompatibility.SUPPORTED, 5, true, true),
      integrity = IntegrityReport(setOf(IntegrityIssue.FIELDS), 5)
    ).restoreReview()

    assertEquals(BackupRestoreReadiness.REVIEW_WARNINGS, review.readiness)
    assertTrue(review.restoreEnabled)
    assertEquals("Review backup warnings", review.title)
  }

  @Test
  fun integrityFailureDisablesRestore() {
    val review = BackupInspection(
      preflight = BackupPreflight(BackupCompatibility.SUPPORTED, 5, true, true),
      integrity = IntegrityReport(setOf(IntegrityIssue.IDS), 5)
    ).restoreReview()

    assertEquals(BackupRestoreReadiness.BLOCKED_BY_INTEGRITY, review.readiness)
    assertFalse(review.restoreEnabled)
    assertTrue(review.message.contains("blocked", ignoreCase = true))
  }
}

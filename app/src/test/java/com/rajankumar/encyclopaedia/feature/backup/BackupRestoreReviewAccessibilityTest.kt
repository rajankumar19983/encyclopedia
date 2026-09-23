package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReviewAccessibilityTest {
  @Test fun summaryIncludesHeadlineDescriptionAndCounts() {
    val presentation = BackupRestoreReviewPresentation(
      "2 warnings to review",
      "Review warnings before restore.",
      listOf(BackupRestoreReviewBadge("Warnings", 2)),
      "Review warnings",
      true
    )
    val summary = presentation.accessibilitySummary()
    assertTrue(summary.contains("2 warnings to review"))
    assertTrue(summary.contains("Warnings: 2"))
  }
}

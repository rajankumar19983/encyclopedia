package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreSectionOrderingTest {
  @Test fun blockingSectionsAppearBeforeWarnings() {
    val warning = BackupRestoreReviewSection(BackupRestoreIssueGroup.CONTENT, 0, 3, emptyList())
    val blocking = BackupRestoreReviewSection(BackupRestoreIssueGroup.RELATIONSHIPS, 1, 0, emptyList())
    val ordered = listOf(warning, blocking).orderedForRestoreReview()
    assertEquals(blocking, ordered.first())
  }
}

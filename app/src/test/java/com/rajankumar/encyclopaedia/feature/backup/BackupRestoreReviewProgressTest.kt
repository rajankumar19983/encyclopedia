package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReviewProgressTest {
  private val model = BackupRestoreReviewModel(
    "Review", "", true,
    listOf(BackupRestoreReviewSection(BackupRestoreIssueGroup.CONTENT, 0, 3, emptyList()))
  )

  @Test fun tracksRemainingIssues() {
    val progress = model.reviewProgress(1)
    assertEquals(2, progress.remaining)
    assertFalse(progress.complete)
  }

  @Test fun clampsReviewedCount() {
    val progress = model.reviewProgress(9)
    assertEquals(3, progress.reviewed)
    assertTrue(progress.complete)
  }
}

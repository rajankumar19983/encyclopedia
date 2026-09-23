package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReviewDescriptionTest {
  @Test fun blockedDescriptionExplainsNextStep() {
    val model = BackupRestoreReviewModel(
      "Review", "", false,
      listOf(BackupRestoreReviewSection(BackupRestoreIssueGroup.CONTENT, 1, 0, emptyList()))
    )
    assertTrue(model.reviewDescription().contains("restore is blocked"))
  }
}

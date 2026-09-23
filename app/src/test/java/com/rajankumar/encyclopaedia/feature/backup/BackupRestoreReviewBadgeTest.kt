package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreReviewBadgeTest {
  @Test fun badgesExposeIssueCounts() {
    val model = BackupRestoreReviewModel(
      "Review", "", false,
      listOf(BackupRestoreReviewSection(BackupRestoreIssueGroup.CONTENT, 2, 3, emptyList()))
    )
    val badges = model.reviewBadges()
    assertEquals(listOf("Blocking", "Warnings"), badges.map { it.label })
    assertEquals(listOf(2, 3), badges.map { it.count })
  }
}

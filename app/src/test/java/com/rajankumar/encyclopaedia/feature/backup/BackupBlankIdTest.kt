package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.assertFalse
import org.junit.Test

class BackupBlankIdTest {
  @Test fun rejectsBlankPrimaryId() {
    val tasks = listOf(PlannerTaskEntity(" ", "Study", "2026-09-22"))
    val snapshot = BackupSnapshot(
      BackupManifest(createdAt = 1, knowledgeNodeCount = 0, lessonCount = 0, questionCount = 0, questionTopicCount = 0, attemptCount = 0, plannerTaskCount = 1),
      emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), tasks
    )
    assertFalse(snapshot.isSafeToRestore())
  }
}

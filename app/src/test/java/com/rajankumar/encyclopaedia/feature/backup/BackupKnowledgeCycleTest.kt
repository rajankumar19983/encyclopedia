package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import org.junit.Assert.assertFalse
import org.junit.Test

class BackupKnowledgeCycleTest {
  @Test fun rejectsParentCycle() {
    val nodes = listOf(
      KnowledgeNodeEntity("a", "b", "A"),
      KnowledgeNodeEntity("b", "a", "B")
    )
    val snapshot = BackupSnapshot(
      BackupManifest(createdAt = 1, knowledgeNodeCount = 2, lessonCount = 0, questionCount = 0, questionTopicCount = 0, attemptCount = 0, plannerTaskCount = 0),
      nodes, emptyList(), emptyList(), emptyList(), emptyList(), emptyList()
    )
    assertFalse(snapshot.isSafeToRestore())
  }
}

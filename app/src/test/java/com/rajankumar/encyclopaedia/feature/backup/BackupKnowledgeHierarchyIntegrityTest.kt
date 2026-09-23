package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import org.junit.Assert.assertFalse
import org.junit.Test

class BackupKnowledgeHierarchyIntegrityTest {
  @Test fun rejectsKnowledgeNodeWithMissingParent() {
    val nodes = listOf(KnowledgeNodeEntity("child", "missing", "Child"))
    val snapshot = BackupSnapshot(
      BackupManifest(createdAt = 1, knowledgeNodeCount = 1, lessonCount = 0, questionCount = 0, questionTopicCount = 0, attemptCount = 0, plannerTaskCount = 0),
      nodes, emptyList(), emptyList(), emptyList(), emptyList(), emptyList()
    )
    assertFalse(snapshot.isSafeToRestore())
  }
}

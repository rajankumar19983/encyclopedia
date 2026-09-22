package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.data.local.NotebookPageEntity
import org.junit.Assert.assertFalse
import org.junit.Test

class BackupNotebookTopicIntegrityTest {
  @Test fun rejectsNotebookPageLinkedToMissingTopic() {
    val pages = listOf(NotebookPageEntity("p", "Notes", knowledgeNodeId = "missing"))
    val snapshot = BackupSnapshot(
      BackupManifest(createdAt = 1, knowledgeNodeCount = 0, lessonCount = 0, questionCount = 0, questionTopicCount = 0, attemptCount = 0, plannerTaskCount = 0, notebookPageCount = 1),
      emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), pages
    )
    assertFalse(snapshot.isSafeToRestore())
  }
}

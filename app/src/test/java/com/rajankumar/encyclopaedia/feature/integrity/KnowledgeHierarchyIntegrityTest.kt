package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.feature.backup.*
import org.junit.Assert.*
import org.junit.Test

class KnowledgeHierarchyIntegrityTest {
  private fun snapshot(nodes: List<KnowledgeNodeEntity>) = BackupSnapshot(
    manifest = BackupManifest(
      createdAt = 1L,
      knowledgeNodeCount = nodes.size,
      lessonCount = 0,
      questionCount = 0,
      questionTopicCount = 0,
      attemptCount = 0,
      plannerTaskCount = 0
    ),
    knowledgeNodes = nodes,
    lessons = emptyList(),
    questions = emptyList(),
    questionTopics = emptyList(),
    attempts = emptyList(),
    plannerTasks = emptyList()
  )

  @Test
  fun acceptsTree() = assertTrue(
    snapshot(
      listOf(
        KnowledgeNodeEntity("a", null, "A"),
        KnowledgeNodeEntity("b", "a", "B")
      )
    ).hasValidKnowledgeHierarchy()
  )

  @Test
  fun rejectsMissingParent() = assertFalse(
    snapshot(listOf(KnowledgeNodeEntity("a", "missing", "A"))).hasValidKnowledgeHierarchy()
  )

  @Test
  fun rejectsCycle() = assertFalse(
    snapshot(
      listOf(
        KnowledgeNodeEntity("a", "b", "A"),
        KnowledgeNodeEntity("b", "a", "B")
      )
    ).hasValidKnowledgeHierarchy()
  )
}

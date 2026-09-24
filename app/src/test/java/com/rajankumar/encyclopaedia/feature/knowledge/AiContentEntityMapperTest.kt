package com.rajankumar.encyclopaedia.feature.knowledge

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AiContentEntityMapperTest {
  @Test
  fun preservesHierarchyLessonOwnershipAndParentAttachment() {
    var id = 0
    val proposal = AiContentProposal(
      AiKnowledgeDraft(
        title = "Operating Systems",
        lessons = listOf(AiLessonDraft("Overview", "Core OS concepts")),
        children = listOf(
          AiKnowledgeDraft(
            title = "Processes",
            lessons = listOf(AiLessonDraft("PCB", "Process control block"))
          )
        )
      )
    )

    val batch = AiContentEntityMapper.map(
      proposal = proposal,
      parentNodeId = "computer-science",
      idFactory = { "id-${++id}" },
      now = 100L
    )

    assertEquals(2, batch.nodes.size)
    assertEquals("computer-science", batch.nodes[0].parentId)
    assertEquals(batch.nodes[0].id, batch.nodes[1].parentId)
    assertEquals(2, batch.lessons.size)
    assertEquals(batch.nodes[0].id, batch.lessons[0].knowledgeNodeId)
    assertEquals(batch.nodes[1].id, batch.lessons[1].knowledgeNodeId)
    assertTrue(batch.nodes.all { it.createdAt == 100L && it.updatedAt == 100L })
  }
}

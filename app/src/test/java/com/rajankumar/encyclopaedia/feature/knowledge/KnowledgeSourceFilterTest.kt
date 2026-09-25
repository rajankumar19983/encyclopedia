package com.rajankumar.encyclopaedia.feature.knowledge

import com.rajankumar.encyclopaedia.data.local.KnowledgeContentSource
import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.LessonEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class KnowledgeSourceFilterTest {
  @Test
  fun `all keeps known and future source values visible`() {
    assertTrue(KnowledgeSourceFilter.ALL.matches(KnowledgeContentSource.USER))
    assertTrue(KnowledgeSourceFilter.ALL.matches(KnowledgeContentSource.AI))
    assertTrue(KnowledgeSourceFilter.ALL.matches("IMPORTED"))
  }

  @Test
  fun `AI filter returns only AI knowledge nodes`() {
    val nodes = listOf(
      KnowledgeNodeEntity(id = "user", parentId = null, name = "Manual"),
      KnowledgeNodeEntity(id = "ai", parentId = null, name = "Generated", source = KnowledgeContentSource.AI),
      KnowledgeNodeEntity(id = "future", parentId = null, name = "Imported", source = "IMPORTED"),
    )

    val filtered = filterKnowledgeNodesBySource(nodes, KnowledgeSourceFilter.AI_GENERATED)

    assertEquals(listOf("ai"), filtered.map { it.id })
  }

  @Test
  fun `manual filter returns only user lessons`() {
    val lessons = listOf(
      LessonEntity(id = "user", knowledgeNodeId = "n", title = "Manual", content = "Body"),
      LessonEntity(
        id = "ai",
        knowledgeNodeId = "n",
        title = "Generated",
        content = "Body",
        source = KnowledgeContentSource.AI,
      ),
    )

    val filtered = filterLessonsBySource(lessons, KnowledgeSourceFilter.MANUAL)

    assertEquals(listOf("user"), filtered.map { it.id })
  }
}

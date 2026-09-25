package com.rajankumar.encyclopaedia.data.local

import org.junit.Assert.assertEquals
import org.junit.Test

class KnowledgeContentSourceDefaultsTest {
  @Test
  fun manuallyCreatedKnowledgeDefaultsToUserSource() {
    val node = KnowledgeNodeEntity(
      id = "node",
      parentId = null,
      name = "Computer Science",
    )
    val lesson = LessonEntity(
      id = "lesson",
      knowledgeNodeId = node.id,
      title = "Overview",
      content = "Permanent study material",
    )

    assertEquals(KnowledgeContentSource.USER, node.source)
    assertEquals(KnowledgeContentSource.USER, lesson.source)
  }
}

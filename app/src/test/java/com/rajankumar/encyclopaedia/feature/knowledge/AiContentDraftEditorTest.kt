package com.rajankumar.encyclopaedia.feature.knowledge

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AiContentDraftEditorTest {
  @Test
  fun `rename trims node title`() {
    val node = AiKnowledgeDraft(title = "Old")

    assertEquals("New title", AiContentDraftEditor.rename(node, "  New title  ").title)
  }

  @Test
  fun `blank description is normalized to null`() {
    val node = AiKnowledgeDraft(title = "Topic", description = "Existing")

    assertNull(AiContentDraftEditor.updateDescription(node, "   ").description)
  }

  @Test
  fun `child editing preserves remaining order`() {
    val first = AiKnowledgeDraft(title = "First")
    val second = AiKnowledgeDraft(title = "Second")
    val third = AiKnowledgeDraft(title = "Third")
    val parent = AiKnowledgeDraft(title = "Root", children = listOf(first, second, third))

    val edited = AiContentDraftEditor.removeChild(parent, 1)

    assertEquals(listOf("First", "Third"), edited.children.map { it.title })
  }

  @Test
  fun `lesson editing preserves remaining order`() {
    val node = AiKnowledgeDraft(
      title = "Root",
      lessons = listOf(
        AiLessonDraft("One", "1"),
        AiLessonDraft("Two", "2"),
        AiLessonDraft("Three", "3"),
      ),
    )

    val edited = AiContentDraftEditor.removeLesson(node, 0)

    assertEquals(listOf("Two", "Three"), edited.lessons.map { it.title })
  }
}

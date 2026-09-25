package com.rajankumar.encyclopaedia.feature.knowledge

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
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

  @Test
  fun `nested node can be edited by path`() {
    val root = AiKnowledgeDraft(
      title = "Root",
      children = listOf(
        AiKnowledgeDraft(
          title = "Module",
          children = listOf(AiKnowledgeDraft(title = "Topic")),
        ),
      ),
    )

    val edited = AiContentDraftEditor.updateNodeAtPath(root, listOf(0, 0)) {
      AiContentDraftEditor.rename(it, "Updated topic")
    }

    assertEquals("Updated topic", edited.children.single().children.single().title)
    assertEquals("Root", edited.title)
  }

  @Test
  fun `nested node can receive appended child by path`() {
    val root = AiKnowledgeDraft(
      title = "Root",
      children = listOf(
        AiKnowledgeDraft(
          title = "Module",
          children = listOf(AiKnowledgeDraft(title = "Existing")),
        ),
      ),
    )

    val edited = AiContentDraftEditor.addNodeAtPath(
      root,
      listOf(0),
      AiKnowledgeDraft(title = "Added"),
    )

    assertEquals(listOf("Existing", "Added"), edited.children.single().children.map { it.title })
  }

  @Test
  fun `nested node can receive appended lesson by path`() {
    val root = AiKnowledgeDraft(
      title = "Root",
      children = listOf(AiKnowledgeDraft(title = "Topic")),
    )

    val edited = AiContentDraftEditor.addLessonAtPath(
      root,
      listOf(0),
      AiLessonDraft("New lesson", "New content"),
    )

    val lesson = edited.children.single().lessons.single()
    assertEquals("New lesson", lesson.title)
    assertEquals("New content", lesson.content)
  }

  @Test
  fun `append operations reject invalid paths`() {
    val root = AiKnowledgeDraft(title = "Root")

    assertThrows(IllegalArgumentException::class.java) {
      AiContentDraftEditor.addNodeAtPath(root, listOf(0), AiKnowledgeDraft(title = "Child"))
    }
    assertThrows(IllegalArgumentException::class.java) {
      AiContentDraftEditor.addLessonAtPath(root, listOf(1), AiLessonDraft("Lesson", "Content"))
    }
  }

  @Test
  fun `nested lesson can be edited and removed by path`() {
    val root = AiKnowledgeDraft(
      title = "Root",
      children = listOf(
        AiKnowledgeDraft(
          title = "Topic",
          lessons = listOf(
            AiLessonDraft("First", "One"),
            AiLessonDraft("Second", "Two"),
          ),
        ),
      ),
    )

    val edited = AiContentDraftEditor.updateLessonAtPath(root, listOf(0), 1) {
      it.copy(title = "Updated", content = "Updated content")
    }
    val removed = AiContentDraftEditor.removeLessonAtPath(edited, listOf(0), 0)

    assertEquals(listOf("Updated"), removed.children.single().lessons.map { it.title })
    assertEquals("Updated content", removed.children.single().lessons.single().content)
  }

  @Test
  fun `nested child can be removed by path`() {
    val root = AiKnowledgeDraft(
      title = "Root",
      children = listOf(
        AiKnowledgeDraft(
          title = "Module",
          children = listOf(
            AiKnowledgeDraft(title = "Keep"),
            AiKnowledgeDraft(title = "Remove"),
          ),
        ),
      ),
    )

    val edited = AiContentDraftEditor.removeNodeAtPath(root, listOf(0, 1))

    assertEquals(listOf("Keep"), edited.children.single().children.map { it.title })
  }

  @Test
  fun `root cannot be removed`() {
    assertThrows(IllegalArgumentException::class.java) {
      AiContentDraftEditor.removeNodeAtPath(AiKnowledgeDraft(title = "Root"), emptyList())
    }
  }
}

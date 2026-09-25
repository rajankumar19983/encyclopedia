package com.rajankumar.encyclopaedia.feature.knowledge

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AiContentReviewModelTest {
  private val draft = AiKnowledgeDraft(
    title = "Computer Science",
    lessons = listOf(AiLessonDraft("Overview", "Overview body")),
    children = listOf(
      AiKnowledgeDraft(
        title = "Operating Systems",
        lessons = listOf(AiLessonDraft("Processes", "Process body")),
        children = listOf(
          AiKnowledgeDraft(title = "Scheduling"),
        ),
      ),
      AiKnowledgeDraft(
        title = "Networks",
        lessons = listOf(AiLessonDraft("TCP", "TCP body")),
      ),
    ),
  )

  @Test
  fun `review stats count nodes lessons and levels`() {
    val stats = draft.reviewStats()

    assertEquals(4, stats.nodeCount)
    assertEquals(3, stats.lessonCount)
    assertEquals(2, stats.maxDepth)
    assertEquals(3, stats.levelCount)
  }

  @Test
  fun `collapsing root keeps root row and hides all contents`() {
    val rows = draft.reviewRows(setOf("root"))

    assertEquals(1, rows.size)
    assertTrue(rows.single() is AiReviewRow.Node)
  }

  @Test
  fun `collapsing one child hides only that childs contents`() {
    val rows = draft.reviewRows(setOf("0"))
    val keys = rows.map { it.key }

    assertTrue("node:0" in keys)
    assertFalse("lesson:0:0" in keys)
    assertFalse("node:0.0" in keys)
    assertTrue("node:1" in keys)
    assertTrue("lesson:1:0" in keys)
  }

  @Test
  fun `collapsible keys include only nodes with nested visible content`() {
    val keys = draft.collapsibleNodeKeys()

    assertEquals(setOf("root", "0", "1"), keys)
    assertFalse("0.0" in keys)
  }

  @Test
  fun `review node keys are stable and readable`() {
    assertEquals("root", reviewNodeKey(emptyList()))
    assertEquals("0.2.1", reviewNodeKey(listOf(0, 2, 1)))
  }
}

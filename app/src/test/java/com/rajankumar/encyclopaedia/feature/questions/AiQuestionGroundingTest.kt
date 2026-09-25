package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.LessonEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AiQuestionGroundingTest {
  private val node = KnowledgeNodeEntity(
    id = "cpu",
    parentId = null,
    name = "CPU Organization",
    description = "Registers, instruction execution and control unit concepts",
  )

  @Test
  fun referenceContainsTopicLessonsAndDirectSubtopics() {
    val reference = buildAiQuestionReference(
      node = node,
      lessons = listOf(
        LessonEntity(
          id = "fetch",
          knowledgeNodeId = "cpu",
          title = "Fetch cycle",
          content = "The program counter supplies the address of the next instruction.",
        ),
      ),
      children = listOf(
        KnowledgeNodeEntity(id = "registers", parentId = "cpu", name = "CPU Registers"),
      ),
    )

    assertNotNull(reference)
    assertTrue(reference!!.contains("CPU Organization"))
    assertTrue(reference.contains("Fetch cycle"))
    assertTrue(reference.contains("program counter"))
    assertTrue(reference.contains("CPU Registers"))
  }

  @Test
  fun referenceIsStrictlyBounded() {
    val reference = buildAiQuestionReference(
      node = node,
      lessons = listOf(
        LessonEntity(
          id = "huge",
          knowledgeNodeId = "cpu",
          title = "Large lesson",
          content = "x".repeat(AI_QUESTION_REFERENCE_MAX_CHARS * 2),
        ),
      ),
      children = emptyList(),
    )

    assertEquals(AI_QUESTION_REFERENCE_MAX_CHARS, reference!!.length)
  }
}

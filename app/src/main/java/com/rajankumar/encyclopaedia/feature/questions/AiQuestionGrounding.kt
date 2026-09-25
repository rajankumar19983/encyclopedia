package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDao
import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.LessonEntity
import kotlinx.coroutines.flow.first

const val AI_QUESTION_REFERENCE_MAX_CHARS = 12_000

fun interface AiQuestionReferenceSource {
  suspend fun load(nodeId: String): String?
}

object EmptyAiQuestionReferenceSource : AiQuestionReferenceSource {
  override suspend fun load(nodeId: String): String? = null
}

class RoomAiQuestionReferenceSource(
  private val dao: EncyclopaediaDao,
) : AiQuestionReferenceSource {
  override suspend fun load(nodeId: String): String? {
    val node = dao.observeAllNodes().first().firstOrNull { it.id == nodeId } ?: return null
    val lessons = dao.observeLessons(nodeId).first()
    val children = dao.observeChildren(nodeId).first()
    return buildAiQuestionReference(node, lessons, children)
  }
}

fun buildAiQuestionReference(
  node: KnowledgeNodeEntity,
  lessons: List<LessonEntity>,
  children: List<KnowledgeNodeEntity>,
): String? {
  val raw = buildString {
    append("Knowledge topic: ")
    appendLine(node.name.trim())

    node.description?.trim()?.takeIf(String::isNotEmpty)?.let { description ->
      append("Description: ")
      appendLine(description)
    }

    lessons.forEach { lesson ->
      appendLine()
      append("Lesson: ")
      appendLine(lesson.title.trim())
      appendLine(lesson.content.trim())
    }

    if (children.isNotEmpty()) {
      appendLine()
      append("Direct subtopics: ")
      appendLine(children.joinToString { it.name.trim() })
    }
  }.trim()

  return raw.take(AI_QUESTION_REFERENCE_MAX_CHARS).takeIf(String::isNotBlank)
}

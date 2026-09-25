package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDao
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity

const val AI_QUESTION_AVOID_MAX_ITEMS = 40
const val AI_QUESTION_AVOID_MAX_CHARS = 6_000

fun interface AiQuestionAvoidanceSource {
  suspend fun load(topicId: String?): List<String>
}

object EmptyAiQuestionAvoidanceSource : AiQuestionAvoidanceSource {
  override suspend fun load(topicId: String?): List<String> = emptyList()
}

class RoomAiQuestionAvoidanceSource(
  private val dao: EncyclopaediaDao,
) : AiQuestionAvoidanceSource {
  override suspend fun load(topicId: String?): List<String> = selectAiQuestionAvoidanceTexts(
    questions = dao.getAllQuestionsOnce(),
    topicLinks = dao.getAllQuestionTopicsForBackup(),
    topicId = topicId,
  )
}

fun selectAiQuestionAvoidanceTexts(
  questions: List<QuestionEntity>,
  topicLinks: List<QuestionTopicEntity>,
  topicId: String?,
): List<String> {
  val linkedQuestionIds = topicId
    ?.let { selected -> topicLinks.asSequence().filter { it.knowledgeNodeId == selected }.map { it.questionId }.toSet() }
    .orEmpty()

  val newestFirst = questions.sortedByDescending(QuestionEntity::createdAt)
  val prioritized = if (linkedQuestionIds.isEmpty()) {
    newestFirst
  } else {
    newestFirst.filter { it.id in linkedQuestionIds } + newestFirst.filterNot { it.id in linkedQuestionIds }
  }

  val seenFingerprints = mutableSetOf<String>()
  return buildList {
    prioritized.forEach { question ->
      val text = question.questionText.trim()
      val fingerprint = aiQuestionFingerprint(text)
      if (text.isNotBlank() && fingerprint.isNotBlank() && seenFingerprints.add(fingerprint)) {
        add(text)
      }
      if (size >= AI_QUESTION_AVOID_MAX_ITEMS) return@buildList
    }
  }
}

fun buildAiQuestionAvoidanceText(questionTexts: List<String>): String? {
  val seenFingerprints = mutableSetOf<String>()
  val lines = questionTexts.asSequence()
    .map(String::trim)
    .filter(String::isNotBlank)
    .filter { seenFingerprints.add(aiQuestionFingerprint(it)) }
    .take(AI_QUESTION_AVOID_MAX_ITEMS)
    .mapIndexed { index, text -> "${index + 1}. $text" }
    .toList()

  return lines.joinToString("\n")
    .take(AI_QUESTION_AVOID_MAX_CHARS)
    .takeIf(String::isNotBlank)
}

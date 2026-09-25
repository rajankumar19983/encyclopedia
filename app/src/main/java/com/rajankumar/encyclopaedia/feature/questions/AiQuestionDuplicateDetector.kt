package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

data class AiQuestionDuplicateConflict(
  val proposalIndex: Int,
  val existingQuestionId: String,
  val existingQuestionText: String,
)

fun findAiQuestionDuplicateConflicts(
  proposal: AiQuestionProposal,
  existingQuestions: List<QuestionEntity>,
): List<AiQuestionDuplicateConflict> {
  val existingByFingerprint = existingQuestions
    .mapNotNull { question ->
      aiQuestionFingerprint(question.questionText)
        .takeIf(String::isNotBlank)
        ?.let { fingerprint -> fingerprint to question }
    }
    .groupBy(keySelector = { it.first }, valueTransform = { it.second })

  return proposal.questions.mapIndexedNotNull { index, draft ->
    val fingerprint = aiQuestionFingerprint(draft.questionText)
    val existing = existingByFingerprint[fingerprint]?.firstOrNull()
    if (fingerprint.isBlank() || existing == null) {
      null
    } else {
      AiQuestionDuplicateConflict(
        proposalIndex = index,
        existingQuestionId = existing.id,
        existingQuestionText = existing.questionText,
      )
    }
  }
}

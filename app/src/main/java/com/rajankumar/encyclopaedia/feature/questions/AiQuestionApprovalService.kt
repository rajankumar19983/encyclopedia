package com.rajankumar.encyclopaedia.feature.questions

import androidx.room.withTransaction
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase
import com.rajankumar.encyclopaedia.data.local.QuestionEntity

class AiQuestionApprovalService(
  private val database: EncyclopaediaDatabase,
) {
  suspend fun approve(
    proposal: AiQuestionProposal,
    topicId: String?,
  ): List<QuestionEntity> {
    val validation = AiQuestionValidator.validate(proposal)
    require(validation.isValid) {
      "AI question proposal is invalid: ${validation.errors.joinToString()}"
    }

    val questions = AiQuestionEntityMapper.map(proposal)
    database.withTransaction {
      val dao = database.dao()
      val duplicateConflicts = findAiQuestionDuplicateConflicts(proposal, dao.getAllQuestionsOnce())
      require(duplicateConflicts.isEmpty()) {
        val numbers = duplicateConflicts.joinToString { "#${it.proposalIndex + 1}" }
        "AI questions $numbers already exist in the Question Bank. Edit or remove duplicates before approval."
      }
      questions.forEach { dao.saveQuestion(it, topicId) }
    }
    return questions
  }
}

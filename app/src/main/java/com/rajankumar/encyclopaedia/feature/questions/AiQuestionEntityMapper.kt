package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import java.util.UUID

object AiQuestionEntityMapper {
  fun map(
    proposal: AiQuestionProposal,
    now: Long = System.currentTimeMillis(),
    idFactory: () -> String = { UUID.randomUUID().toString() },
  ): List<QuestionEntity> = proposal.questions.map { draft ->
    QuestionEntity(
      id = idFactory(),
      questionText = draft.questionText.trim(),
      options = draft.options.joinToString("\n") { it.trim() },
      correctAnswer = optionLetter(draft.correctIndex),
      explanation = draft.explanation.trim(),
      source = "AI",
      difficulty = normalizeDifficulty(draft.difficulty),
      createdAt = now,
      updatedAt = now,
    )
  }
}

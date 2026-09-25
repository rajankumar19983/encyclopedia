package com.rajankumar.encyclopaedia.feature.questions

const val AI_QUESTION_PROPOSAL_MAX_SIZE = 30

fun updateAiQuestion(
  proposal: AiQuestionProposal,
  index: Int,
  transform: (AiQuestionDraft) -> AiQuestionDraft,
): AiQuestionProposal {
  require(index in proposal.questions.indices) { "Invalid question index $index" }
  return proposal.copy(
    questions = proposal.questions.mapIndexed { current, draft ->
      if (current == index) transform(draft) else draft
    },
  )
}

fun removeAiQuestion(proposal: AiQuestionProposal, index: Int): AiQuestionProposal {
  require(index in proposal.questions.indices) { "Invalid question index $index" }
  return proposal.copy(questions = proposal.questions.filterIndexed { current, _ -> current != index })
}

fun appendAiQuestion(
  proposal: AiQuestionProposal,
  draft: AiQuestionDraft,
): AiQuestionProposal {
  require(proposal.questions.size < AI_QUESTION_PROPOSAL_MAX_SIZE) {
    "AI question proposal cannot contain more than $AI_QUESTION_PROPOSAL_MAX_SIZE questions"
  }
  return proposal.copy(questions = proposal.questions + draft)
}

fun newManualAiQuestionDraft(): AiQuestionDraft = AiQuestionDraft(
  questionText = "",
  options = List(4) { "" },
  correctIndex = 0,
  explanation = "",
  difficulty = "MEDIUM",
  origin = AiQuestionDraftOrigin.USER,
)

fun updateAiQuestionOption(draft: AiQuestionDraft, index: Int, value: String): AiQuestionDraft {
  require(index in draft.options.indices) { "Invalid option index $index" }
  return draft.copy(options = draft.options.mapIndexed { current, option -> if (current == index) value else option })
}

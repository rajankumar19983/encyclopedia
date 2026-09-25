package com.rajankumar.encyclopaedia.feature.questions

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

fun updateAiQuestionOption(draft: AiQuestionDraft, index: Int, value: String): AiQuestionDraft {
  require(index in draft.options.indices) { "Invalid option index $index" }
  return draft.copy(options = draft.options.mapIndexed { current, option -> if (current == index) value else option })
}

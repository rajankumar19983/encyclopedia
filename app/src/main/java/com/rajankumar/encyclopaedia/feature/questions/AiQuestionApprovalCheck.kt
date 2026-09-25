package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

data class AiQuestionApprovalCheck(
  val validation: AiQuestionValidation,
  val duplicateConflicts: List<AiQuestionDuplicateConflict>,
) {
  val isApprovable: Boolean
    get() = validation.isValid && duplicateConflicts.isEmpty()
}

fun checkAiQuestionApproval(
  proposal: AiQuestionProposal,
  existingQuestions: List<QuestionEntity>,
): AiQuestionApprovalCheck = AiQuestionApprovalCheck(
  validation = AiQuestionValidator.validate(proposal),
  duplicateConflicts = findAiQuestionDuplicateConflicts(proposal, existingQuestions),
)

package com.rajankumar.encyclopaedia.feature.questions

data class AiQuestionValidation(
  val isValid: Boolean,
  val errors: List<String>,
)

object AiQuestionValidator {
  private val allowedDifficulties = setOf("EASY", "MEDIUM", "HARD")

  fun validate(proposal: AiQuestionProposal): AiQuestionValidation {
    val errors = mutableListOf<String>()
    if (proposal.questions.isEmpty()) errors += "The proposal contains no questions."
    if (proposal.questions.size > 30) errors += "The proposal contains more than 30 questions."

    val seenQuestions = mutableSetOf<String>()
    proposal.questions.forEachIndexed { index, draft ->
      val number = index + 1
      val fingerprint = aiQuestionFingerprint(draft.questionText)
      if (draft.questionText.isBlank()) errors += "Question $number has empty question text."
      if (fingerprint.isNotBlank() && !seenQuestions.add(fingerprint)) errors += "Question $number duplicates another generated question."
      if (draft.options.size !in 4..6) errors += "Question $number must contain 4 to 6 options."
      if (draft.options.any(String::isBlank)) errors += "Question $number contains a blank option."
      if (draft.options.map { it.trim().lowercase() }.distinct().size != draft.options.size) errors += "Question $number contains duplicate options."
      if (draft.correctIndex !in draft.options.indices) errors += "Question $number has an invalid correct answer."
      if (draft.explanation.isBlank()) errors += "Question $number needs an explanation."
      if (draft.difficulty.trim().uppercase() !in allowedDifficulties) errors += "Question $number has an invalid difficulty."
    }

    return AiQuestionValidation(errors.isEmpty(), errors.distinct())
  }
}

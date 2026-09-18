package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

data class PracticeValidation(val valid: Boolean, val reason: String? = null)

fun QuestionEntity.validateForPractice(): PracticeValidation {
  if (questionText.isBlank()) return PracticeValidation(false, "Question text is empty")
  val choices = optionList()
  if (choices.size !in 2..6) return PracticeValidation(false, "Question must have 2–6 options")
  if (correctOptionIndex() == null) return PracticeValidation(false, "Correct answer does not match an option")
  return PracticeValidation(true)
}

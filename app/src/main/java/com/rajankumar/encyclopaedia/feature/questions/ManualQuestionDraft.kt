package com.rajankumar.encyclopaedia.feature.questions

data class ManualQuestionDraft(val question: String, val optionsText: String, val answer: String, val explanation: String = "") {
  val options: List<String> get() = optionsText.lines().map(String::trim).filter(String::isNotBlank)
  val normalizedAnswer: String get() = answer.trim().uppercase()
  private val answerIndex: Int? get() = normalizedAnswer.toIntOrNull()?.minus(1) ?: normalizedAnswer.singleOrNull()?.let { it.code - 'A'.code }

  fun validationMessage(): String? = when {
    question.isBlank() -> "Enter the question text."
    options.size !in 2..6 -> "Enter between 2 and 6 options, one per line."
    normalizedAnswer.isBlank() -> "Enter the correct option."
    answerIndex == null || answerIndex !in options.indices -> "The correct answer must point to one of the listed options."
    else -> null
  }

  fun isValid(): Boolean = validationMessage() == null
}

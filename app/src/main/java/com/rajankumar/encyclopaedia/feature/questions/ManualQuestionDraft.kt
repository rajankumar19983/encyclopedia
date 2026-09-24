package com.rajankumar.encyclopaedia.feature.questions

data class ManualQuestionDraft(
  val question: String,
  val optionsText: String,
  val answer: String,
  val explanation: String = ""
) {
  val options: List<String>
    get() = optionsText.lines().map(String::trim).filter(String::isNotBlank)

  val normalizedAnswer: String
    get() = answer.trim().uppercase()

  fun isValid(): Boolean {
    if (question.isBlank() || options.size !in 2..6 || normalizedAnswer.isBlank()) return false
    val index = normalizedAnswer.toIntOrNull()?.minus(1)
      ?: normalizedAnswer.singleOrNull()?.let { it.code - 'A'.code }
      ?: return false
    return index in options.indices
  }
}

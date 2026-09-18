package com.rajankumar.encyclopaedia.feature.questions

data class PracticeGoal(
  val targetQuestions: Int = 20,
  val completedQuestions: Int = 0
) {
  val progress: Float get() = if (targetQuestions <= 0) 0f else (completedQuestions.toFloat() / targetQuestions).coerceIn(0f, 1f)
  val complete: Boolean get() = completedQuestions >= targetQuestions
}

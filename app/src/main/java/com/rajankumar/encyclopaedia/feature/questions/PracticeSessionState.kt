package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

data class PracticeSessionState(
  val questions: List<QuestionEntity> = emptyList(),
  val currentIndex: Int = 0,
  val selectedAnswer: String? = null,
  val submitted: Boolean = false,
  val reviews: List<PracticeAnswerReview> = emptyList()
) {
  val currentQuestion: QuestionEntity? get() = questions.getOrNull(currentIndex)
  val complete: Boolean get() = questions.isNotEmpty() && currentIndex >= questions.size
  val correctCount: Int get() = reviews.count { it.wasCorrect }
}

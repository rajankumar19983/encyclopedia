package com.rajankumar.encyclopaedia.feature.questions

data class PracticeReviewSummary(
  val mistakes: Int,
  val explainedMistakes: Int
)

fun List<PracticeAnswerReview>.reviewSummary(): PracticeReviewSummary {
  val mistakes = incorrectOnly()
  return PracticeReviewSummary(
    mistakes = mistakes.size,
    explainedMistakes = mistakes.count { !it.question.explanation.isNullOrBlank() }
  )
}

package com.rajankumar.encyclopaedia.feature.questions

enum class PracticeAnswerStatus { UNANSWERED, CORRECT, INCORRECT }

fun PracticeAnswerReview.status(): PracticeAnswerStatus =
  if (wasCorrect) PracticeAnswerStatus.CORRECT else PracticeAnswerStatus.INCORRECT

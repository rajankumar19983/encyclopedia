package com.rajankumar.encyclopaedia.feature.questions

fun List<PracticeAnswerReview>.incorrectOnly(): List<PracticeAnswerReview> = filterNot { it.wasCorrect }

fun List<PracticeAnswerReview>.totalTimeMs(): Long = sumOf { it.timeTakenMs.coerceAtLeast(0) }

fun List<PracticeAnswerReview>.summary(): PracticeSessionSummary = PracticeSessionSummary(
  total = size,
  correct = count { it.wasCorrect },
  totalTimeMs = totalTimeMs()
)

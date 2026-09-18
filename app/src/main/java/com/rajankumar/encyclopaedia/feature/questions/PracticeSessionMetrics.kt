package com.rajankumar.encyclopaedia.feature.questions

data class PracticeSessionMetrics(
  val summary: PracticeSessionSummary,
  val quickAnswers: Int,
  val deliberateAnswers: Int
)

fun List<PracticeAnswerReview>.sessionMetrics(): PracticeSessionMetrics = PracticeSessionMetrics(
  summary = summary(),
  quickAnswers = count { answerPace(it.timeTakenMs) == AnswerPace.QUICK },
  deliberateAnswers = count { answerPace(it.timeTakenMs) == AnswerPace.DELIBERATE }
)

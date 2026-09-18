package com.rajankumar.encyclopaedia.feature.questions

enum class AnswerPace(val label: String) { QUICK("Quick"), NORMAL("Normal"), DELIBERATE("Deliberate") }

fun answerPace(timeTakenMs: Long): AnswerPace = when {
  timeTakenMs < 15_000 -> AnswerPace.QUICK
  timeTakenMs < 60_000 -> AnswerPace.NORMAL
  else -> AnswerPace.DELIBERATE
}

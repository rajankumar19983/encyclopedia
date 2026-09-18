package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

data class AnswerEvaluation(
  val selected: String,
  val correct: String,
  val isCorrect: Boolean
)

fun QuestionEntity.evaluateAnswer(selectedAnswer: String): AnswerEvaluation {
  val selected = selectedAnswer.trim().uppercase()
  val correct = correctAnswer.trim().uppercase()
  return AnswerEvaluation(selected, correct, selected == correct)
}

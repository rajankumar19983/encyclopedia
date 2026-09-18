package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

data class PracticeSessionSummary(
  val total: Int,
  val correct: Int,
  val totalTimeMs: Long
) {
  val incorrect: Int get() = (total - correct).coerceAtLeast(0)
  val accuracyPercent: Int get() = if (total == 0) 0 else ((correct * 100f) / total).toInt().coerceIn(0, 100)
  val averageTimeMs: Long get() = if (total == 0) 0 else totalTimeMs / total
}

data class PracticeAnswerReview(
  val question: QuestionEntity,
  val selectedAnswer: String,
  val wasCorrect: Boolean,
  val timeTakenMs: Long
)

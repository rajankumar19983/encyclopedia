package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity

data class QuestionPerformance(val questionId: String, val attempts: Int, val correct: Int, val averageTimeMs: Long) {
  val accuracy get() = if (attempts == 0) 0 else correct * 100 / attempts
}

fun List<QuestionAttemptEntity>.questionPerformance(): List<QuestionPerformance> = groupBy { it.questionId }.map { (id, history) ->
  QuestionPerformance(id, history.size, history.count { it.isCorrect }, history.sumOf { it.timeTakenMs.coerceAtLeast(0) } / history.size)
}

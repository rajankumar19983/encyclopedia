package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity

data class PerformanceData(
  val summary: PerformanceSummary,
  val trend: AccuracyTrend,
  val speedTrend: SpeedTrend,
  val weakQuestions: List<WeakQuestion>
)

fun buildPerformanceData(questions: List<QuestionEntity>, attempts: List<QuestionAttemptEntity>): PerformanceData {
  val average = if (attempts.isEmpty()) 0 else attempts.sumOf { it.timeTakenMs.coerceAtLeast(0) } / attempts.size
  return PerformanceData(
    PerformanceSummary(attempts.size, attempts.count { it.isCorrect }, attempts.map { it.questionId }.distinct().size, questions.size, average),
    attempts.accuracyTrend(),
    attempts.speedTrend(),
    buildWeakQuestions(questions, attempts)
  )
}

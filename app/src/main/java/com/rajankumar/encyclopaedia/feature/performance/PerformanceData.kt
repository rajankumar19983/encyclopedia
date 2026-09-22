package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity
import com.rajankumar.encyclopaedia.feature.revision.TopicRevisionState
import com.rajankumar.encyclopaedia.feature.revision.buildTopicRevisionStates

data class PerformanceData(
  val summary: PerformanceSummary,
  val trend: AccuracyTrend,
  val speedTrend: SpeedTrend,
  val weakQuestions: List<WeakQuestion>,
  val topicPerformance: List<TopicPerformance> = emptyList(),
  val topicsNeedingRevision: List<TopicRevisionState> = emptyList(),
)

fun buildPerformanceData(
  questions: List<QuestionEntity>,
  attempts: List<QuestionAttemptEntity>,
  topics: List<KnowledgeNodeEntity> = emptyList(),
  questionTopics: List<QuestionTopicEntity> = emptyList(),
): PerformanceData {
  val average = if (attempts.isEmpty()) 0 else attempts.sumOf { it.timeTakenMs.coerceAtLeast(0) } / attempts.size
  return PerformanceData(
    summary = PerformanceSummary(attempts.size, attempts.count { it.isCorrect }, attempts.map { it.questionId }.distinct().size, questions.size, average),
    trend = attempts.accuracyTrend(),
    speedTrend = attempts.speedTrend(),
    weakQuestions = buildWeakQuestions(questions, attempts),
    topicPerformance = buildTopicPerformance(topics, questionTopics, attempts),
    topicsNeedingRevision = buildTopicRevisionStates(topics, questionTopics, attempts),
  )
}

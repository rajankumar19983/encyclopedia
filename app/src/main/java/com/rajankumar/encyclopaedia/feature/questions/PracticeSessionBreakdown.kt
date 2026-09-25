package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity

data class PracticeBreakdownRow(
  val key: String,
  val label: String,
  val total: Int,
  val correct: Int,
  val totalTimeMs: Long,
) {
  val mistakes: Int
    get() = (total - correct).coerceAtLeast(0)

  val accuracyPercent: Int
    get() = if (total == 0) 0 else (correct * 100 / total).coerceIn(0, 100)

  val averageTimeMs: Long
    get() = if (total == 0) 0 else totalTimeMs.coerceAtLeast(0) / total
}

enum class PracticeFocusDimension(val label: String) {
  TOPIC("Topic"),
  DIFFICULTY("Difficulty"),
}

data class PracticeFocusInsight(
  val dimension: PracticeFocusDimension,
  val row: PracticeBreakdownRow,
)

data class PracticeSlowQuestion(
  val questionId: String,
  val questionText: String,
  val timeTakenMs: Long,
  val wasCorrect: Boolean,
)

data class PracticeSessionBreakdown(
  val byDifficulty: List<PracticeBreakdownRow>,
  val bySource: List<PracticeBreakdownRow>,
  val byTopic: List<PracticeBreakdownRow>,
  val slowestQuestions: List<PracticeSlowQuestion>,
  val focusInsight: PracticeFocusInsight?,
)

fun buildPracticeSessionBreakdown(
  reviews: List<PracticeAnswerReview>,
  topics: List<KnowledgeNodeEntity> = emptyList(),
  questionTopics: List<QuestionTopicEntity> = emptyList(),
): PracticeSessionBreakdown {
  val topicNamesById = topics.associate { it.id to it.name }
  val topicLabelByQuestionId = questionTopics
    .groupBy { it.questionId }
    .mapValues { (_, links) ->
      links.asSequence()
        .mapNotNull { topicNamesById[it.knowledgeNodeId] }
        .map(String::trim)
        .filter(String::isNotBlank)
        .distinct()
        .sortedBy { it.lowercase() }
        .toList()
        .takeIf { it.isNotEmpty() }
        ?.joinToString(" + ")
        ?: "Unlinked"
    }

  val byDifficulty = reviews.toBreakdownRows { review ->
    val key = normalizeDifficulty(review.question.difficulty)
    key to key.lowercase().replaceFirstChar(Char::uppercase)
  }
  val bySource = reviews.toBreakdownRows { review ->
    val key = review.question.source.trim().uppercase().ifBlank { "UNKNOWN" }
    key to displayQuestionSource(review.question.source)
  }
  val byTopic = reviews.toBreakdownRows { review ->
    val label = topicLabelByQuestionId[review.question.id] ?: "Unlinked"
    label.lowercase() to label
  }

  val topicFocus = byTopic
    .asSequence()
    .filter { it.label != "Unlinked" && it.total >= 2 && it.accuracyPercent < 80 }
    .minWithOrNull(compareBy<PracticeBreakdownRow> { it.accuracyPercent }.thenByDescending { it.total }.thenBy { it.label.lowercase() })
    ?.let { PracticeFocusInsight(PracticeFocusDimension.TOPIC, it) }
  val difficultyFocus = byDifficulty
    .asSequence()
    .filter { it.total >= 2 && it.accuracyPercent < 80 }
    .minWithOrNull(compareBy<PracticeBreakdownRow> { it.accuracyPercent }.thenByDescending { it.total }.thenBy { it.label.lowercase() })
    ?.let { PracticeFocusInsight(PracticeFocusDimension.DIFFICULTY, it) }

  val slowest = reviews
    .sortedWith(compareByDescending<PracticeAnswerReview> { it.timeTakenMs.coerceAtLeast(0) }.thenBy { it.question.id })
    .take(3)
    .map { review ->
      PracticeSlowQuestion(
        questionId = review.question.id,
        questionText = review.question.questionText,
        timeTakenMs = review.timeTakenMs.coerceAtLeast(0),
        wasCorrect = review.wasCorrect,
      )
    }

  return PracticeSessionBreakdown(
    byDifficulty = byDifficulty,
    bySource = bySource,
    byTopic = byTopic,
    slowestQuestions = slowest,
    focusInsight = topicFocus ?: difficultyFocus,
  )
}

private fun List<PracticeAnswerReview>.toBreakdownRows(
  classifier: (PracticeAnswerReview) -> Pair<String, String>,
): List<PracticeBreakdownRow> = groupBy(classifier).map { (classification, groupedReviews) ->
  val (key, label) = classification
  PracticeBreakdownRow(
    key = key,
    label = label,
    total = groupedReviews.size,
    correct = groupedReviews.count { it.wasCorrect },
    totalTimeMs = groupedReviews.sumOf { it.timeTakenMs.coerceAtLeast(0) },
  )
}.sortedWith(
  compareBy<PracticeBreakdownRow> { it.accuracyPercent }
    .thenByDescending { it.total }
    .thenBy { it.label.lowercase() },
)

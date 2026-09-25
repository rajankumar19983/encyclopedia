package com.rajankumar.encyclopaedia.feature.questions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity

@Composable
fun PracticeSessionResultScreen(
  reviews: List<PracticeAnswerReview>,
  topics: List<KnowledgeNodeEntity>,
  questionTopics: List<QuestionTopicEntity>,
  allowNewSession: Boolean,
  onRetryMistakes: () -> Unit,
  onNewSession: () -> Unit,
  onDone: () -> Unit,
) {
  val summary = remember(reviews) { reviews.summary() }
  val metrics = remember(reviews) { reviews.sessionMetrics() }
  val breakdown = remember(reviews, topics, questionTopics) {
    buildPracticeSessionBreakdown(reviews, topics, questionTopics)
  }
  val incorrect = remember(reviews) { reviews.incorrectOnly() }

  LazyColumn(
    Modifier.fillMaxSize().padding(28.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item { Text(summary.resultHeadline(), style = MaterialTheme.typography.headlineMedium) }
    item { Text(summary.resultDetail(), style = MaterialTheme.typography.titleLarge) }
    item {
      Text(
        practiceFeedback(summary.correct, summary.total),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }

    item {
      Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Text("Session overview", style = MaterialTheme.typography.titleMedium)
          Text("Accuracy: ${summary.accuracyPercent}% • ${summary.correct}/${summary.total} correct")
          Text(
            "Time: ${formatPracticeDuration(summary.totalTimeMs)} total • " +
              "${formatPracticeDuration(summary.averageTimeMs)} average",
          )
          Text(
            "Pace: ${metrics.quickAnswers} quick • ${metrics.deliberateAnswers} deliberate",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
    }

    breakdown.focusInsight?.let { insight ->
      item {
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Focus next", style = MaterialTheme.typography.titleMedium)
            Text("${insight.dimension.label}: ${insight.row.label}")
            Text(
              "${insight.row.accuracyPercent}% accuracy • ${insight.row.mistakes} mistake${if (insight.row.mistakes == 1) "" else "s"} in ${insight.row.total} questions",
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      }
    }

    if (breakdown.byTopic.isNotEmpty()) {
      item { PracticeBreakdownSection("By Knowledge topic", breakdown.byTopic) }
    }
    if (breakdown.byDifficulty.isNotEmpty()) {
      item { PracticeBreakdownSection("By difficulty", breakdown.byDifficulty) }
    }
    if (breakdown.bySource.isNotEmpty()) {
      item { PracticeBreakdownSection("By source", breakdown.bySource) }
    }

    if (breakdown.slowestQuestions.isNotEmpty()) {
      item {
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Slowest answers", style = MaterialTheme.typography.titleMedium)
            breakdown.slowestQuestions.forEachIndexed { index, item ->
              Text(
                "${index + 1}. ${if (item.wasCorrect) "✓" else "✗"} ${item.questionText} • ${formatPracticeDuration(item.timeTakenMs)}",
              )
            }
          }
        }
      }
    }

    if (incorrect.isNotEmpty()) {
      item { Text("Review mistakes", style = MaterialTheme.typography.titleLarge) }
      incorrect.forEach { review ->
        item(key = "mistake:${review.question.id}") {
          Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Text(review.question.questionText, style = MaterialTheme.typography.titleMedium)
              Text("Your answer: ${review.question.answerLabel(review.selectedAnswer)}")
              Text(
                "Correct: ${review.question.answerLabel(review.question.correctAnswer)}",
                color = MaterialTheme.colorScheme.primary,
              )
              review.question.explanation?.takeIf(String::isNotBlank)?.let {
                Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
          }
        }
      }
    }

    item {
      Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Button(onClick = onRetryMistakes, enabled = incorrect.isNotEmpty()) {
          Text("Practise these mistakes")
        }
        if (allowNewSession) {
          Button(onClick = onNewSession) { Text("New session") }
        }
      }
    }
    item { TextButton(onClick = onDone) { Text("Back to Question Bank") } }
  }
}

@Composable
private fun PracticeBreakdownSection(
  title: String,
  rows: List<PracticeBreakdownRow>,
) {
  Card(Modifier.fillMaxWidth()) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Text(title, style = MaterialTheme.typography.titleMedium)
      rows.forEach { row ->
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
          Text(row.label, style = MaterialTheme.typography.titleSmall)
          Text(
            "${row.correct}/${row.total} correct • ${row.accuracyPercent}% • avg ${formatPracticeDuration(row.averageTimeMs)}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
    }
  }
}

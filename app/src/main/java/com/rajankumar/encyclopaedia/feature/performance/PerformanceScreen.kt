package com.rajankumar.encyclopaedia.feature.performance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase

@Composable
fun PerformanceScreen() {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val questions by dao.observeQuestions().collectAsStateWithLifecycle(emptyList())
  val attempts by dao.observeAllAttempts().collectAsStateWithLifecycle(emptyList())
  val topics by dao.observeAllNodes().collectAsStateWithLifecycle(emptyList())
  val questionTopics by dao.observeQuestionTopics().collectAsStateWithLifecycle(emptyList())
  var dashboardState by remember { mutableStateOf(PerformanceDashboardState()) }
  val filteredAttempts = remember(attempts, dashboardState.period) { attempts.withinPeriod(dashboardState.period) }
  val data = remember(questions, filteredAttempts, topics, questionTopics) { buildPerformanceData(questions, filteredAttempts, topics, questionTopics) }
  val visibleWeakQuestions = remember(data, dashboardState.weakQuestionQuery, dashboardState.weakQuestionSort) { data.visibleWeakQuestions(dashboardState) }
  val summary = data.summary
  val topicCoverage = remember(topics, data.topicPerformance) { performanceCoverage(topics.size, data.topicPerformance) }
  val topicPriorities = remember(data.topicPerformance) { data.topicPerformance.studyPriorities() }
  val topicInsight = remember(topicCoverage, topicPriorities) { topicPerformanceInsight(topicCoverage, topicPriorities) }
  val consistencyFeedback = remember(data.consistency) { studyConsistencyFeedback(data.consistency) }

  LazyColumn(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
    item { Text("Performance", style = MaterialTheme.typography.headlineMedium) }
    item {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        PerformancePeriod.entries.forEach { option -> FilterChip(selected = dashboardState.period == option, onClick = { dashboardState = dashboardState.copy(period = option) }, label = { Text(option.label) }) }
      }
    }
    item { Text(data.narrative(), color = MaterialTheme.colorScheme.onSurfaceVariant) }
    item {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        summary.stats().forEach { stat -> Card(Modifier.weight(1f)) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) { Text(stat.label, style = MaterialTheme.typography.labelLarge); Text(stat.value, style = MaterialTheme.typography.headlineSmall); Text(stat.detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
      }
    }
    item {
      Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) { Text("Study consistency", style = MaterialTheme.typography.titleMedium); Text(consistencyFeedback.headline, style = MaterialTheme.typography.titleSmall); Text("${data.consistency.activeDays} active days • current streak ${data.consistency.currentStreak} • longest ${data.consistency.longestStreak}"); Text(consistencyFeedback.detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) } }
    }
    item {
      Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text("Question coverage", style = MaterialTheme.typography.titleMedium); LinearProgressIndicator(progress = { summary.coverage / 100f }, modifier = Modifier.fillMaxWidth()); Text("${summary.uniqueQuestions} of ${summary.totalQuestions} questions practised") } }
    }
    item {
      Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
          Text("Topic coverage", style = MaterialTheme.typography.titleMedium)
          LinearProgressIndicator(progress = { topicCoverage.coveragePercent / 100f }, modifier = Modifier.fillMaxWidth())
          Text("${topicCoverage.practisedTopicCount} of ${topicCoverage.topicCount} topics practised • ${topicCoverage.coveragePercent}%")
          Text("${topicCoverage.strongTopicCount} strong • ${topicCoverage.weakTopicCount} weak • ${topicCoverage.unpractisedTopicCount} unpractised", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(topicInsight.headline, style = MaterialTheme.typography.titleSmall)
          Text(topicInsight.detail, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }
    }
    if (filteredAttempts.isNotEmpty()) {
      item { Text("Recent trend", style = MaterialTheme.typography.titleLarge) }
      item { Text("${trendLabel(data.trend.change)} • recent ${data.trend.recent}% vs previous ${data.trend.previous}% (${signedPercent(data.trend.change)})") }
      item {
        Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) { Text("Answer speed", style = MaterialTheme.typography.titleMedium); Text("Recent average ${formatDurationMs(data.speedTrend.recentAverageMs)}"); if (data.speedTrend.previousAverageMs > 0) { val difference = data.speedTrend.improvementMs; Text(when { difference > 0 -> "${formatDurationMs(difference)} faster than the previous window"; difference < 0 -> "${formatDurationMs(-difference)} slower than the previous window"; else -> "Same average speed as the previous window" }, color = MaterialTheme.colorScheme.onSurfaceVariant) } else Text("Keep practising to establish a comparison window.", color = MaterialTheme.colorScheme.onSurfaceVariant) } }
      }
      if (topicPriorities.isNotEmpty()) {
        item { Text("Study priorities", style = MaterialTheme.typography.titleLarge) }
        items(topicPriorities, key = { "priority-${it.performance.topic.id}" }) { priority -> Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) { Text(priority.performance.topic.name, style = MaterialTheme.typography.titleMedium); LinearProgressIndicator(progress = { priority.performance.accuracyPercent / 100f }, modifier = Modifier.fillMaxWidth()); Text("${priority.performance.accuracyPercent}% accuracy • ${priority.performance.mistakes} mistakes • ${priority.reason}") } } }
      }
      if (data.topicsNeedingRevision.isNotEmpty()) {
        item { Text("Topic revision", style = MaterialTheme.typography.titleLarge) }
        items(data.topicsNeedingRevision.take(10), key = { "performance-topic-${it.topic.id}" }) { topic -> Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(topic.topic.name, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f)); Text(topic.priority.label, color = MaterialTheme.colorScheme.primary) }; LinearProgressIndicator(progress = { topic.accuracyPercent / 100f }, modifier = Modifier.fillMaxWidth()); Text("${topic.accuracyPercent}% accuracy • ${topic.mistakes} mistakes • ${topic.questionsNeedingRevision} due") } } }
      }
      item { Text("Weak questions", style = MaterialTheme.typography.titleLarge) }
      if (data.weakQuestions.isNotEmpty()) {
        item { OutlinedTextField(value = dashboardState.weakQuestionQuery, onValueChange = { dashboardState = dashboardState.copy(weakQuestionQuery = it) }, modifier = Modifier.fillMaxWidth(), label = { Text("Search weak questions") }, singleLine = true) }
        item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) { PerformanceSort.entries.forEach { sort -> FilterChip(selected = dashboardState.weakQuestionSort == sort, onClick = { dashboardState = dashboardState.copy(weakQuestionSort = sort) }, label = { Text(sort.label()) }) } } }
      }
      if (data.weakQuestions.isEmpty()) item { Text("No incorrect answers recorded.") } else if (visibleWeakQuestions.isEmpty()) item { Text("No weak questions match your search.") }
      items(visibleWeakQuestions.take(20), key = { it.question.id }) { weak -> Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) { Text(weak.question.questionText, style = MaterialTheme.typography.titleMedium); Text("${weak.mistakes} mistakes • ${weak.attempts} attempts • ${weak.accuracy}% accuracy") } } }
    }
    item { Text("Recommendation", style = MaterialTheme.typography.titleLarge) }
    item { Text(data.recommendation()) }
  }
}

private fun PerformanceSort.label(): String = when (this) {
  PerformanceSort.MOST_MISTAKES -> "Most mistakes"
  PerformanceSort.LOWEST_ACCURACY -> "Lowest accuracy"
  PerformanceSort.MOST_ATTEMPTED -> "Most attempted"
}

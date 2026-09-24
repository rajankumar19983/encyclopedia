package com.rajankumar.encyclopaedia.feature.revision

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase

@Composable
fun RevisionScreen(onStartPractice: () -> Unit = {}) {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val questions by dao.observeQuestions().collectAsStateWithLifecycle(emptyList())
  val attempts by dao.observeAllAttempts().collectAsStateWithLifecycle(emptyList())
  val topics by dao.observeAllNodes().collectAsStateWithLifecycle(emptyList())
  val questionTopics by dao.observeQuestionTopics().collectAsStateWithLifecycle(emptyList())
  val queue = remember(questions, attempts) { buildRevisionQueue(questions, attempts) }
  val topicRevision = remember(topics, questionTopics, attempts) { buildTopicRevisionStates(topics, questionTopics, attempts) }
  var query by remember { mutableStateOf("") }
  var priority by remember { mutableStateOf<RevisionPriority?>(null) }
  var sort by remember { mutableStateOf(RevisionSort.PRIORITY) }
  val visible = remember(queue, query, priority, sort) { queue.filterRevisionQueue(priority, query).sortedForRevision(sort) }
  val snapshot = remember(queue) { queue.snapshot() }
  val badge = remember(queue) { queue.revisionBadge() }
  val recommendedLimit = remember(queue.size) { recommendedRevisionLimit(queue.size) }
  val session = remember(queue, recommendedLimit) { if (recommendedLimit == 0) null else RevisionSession(queue, recommendedLimit) }
  var sessionState by remember(session?.questions?.map { it.id }) { mutableStateOf(session?.state()) }
  val sessionProgress = sessionState?.progress ?: RevisionProgress(0, 0)

  LazyColumn(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Revision", style = MaterialTheme.typography.headlineMedium); if (badge.count > 0) Text(badge.label, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge) } }
    item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(snapshot.stats.summaryText(), style = MaterialTheme.typography.titleMedium); Text(snapshot.health.label, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge) } }
    item { Text(snapshot.recommendation, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(snapshot.health.guidance(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    session?.let { currentSession -> item { Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) { Text("Recommended session", style = MaterialTheme.typography.titleMedium); Text(revisionSessionLimitLabel(queue.size)); LinearProgressIndicator(progress = { sessionProgress.percent / 100f }, modifier = Modifier.fillMaxWidth()); Text(sessionProgress.summary, style = MaterialTheme.typography.bodySmall); Text("The highest-priority questions are placed first.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant); Button(onClick = { RevisionPracticeRequest.set(currentSession.questions.map { it.id }); onStartPractice() }) { Text("Start revision practice") } } } } }
    if (topicRevision.isNotEmpty()) { item { Text("Topics to revise", style = MaterialTheme.typography.titleLarge) }; items(topicRevision, key = { "topic-${it.topic.id}" }) { topicState -> Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(topicState.topic.name, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f)); Text(topicState.priority.label, color = MaterialTheme.colorScheme.primary) }; Text("${topicState.questionsNeedingRevision} question(s) due • ${topicState.mistakes} mistakes"); Text("${topicState.accuracyPercent}% accuracy across ${topicState.attempts} attempts", color = MaterialTheme.colorScheme.onSurfaceVariant); Button(onClick = { RevisionPracticeRequest.set(topicState.revisionQuestionIds); onStartPractice() }) { Text("Practise this topic") } } } } }
    if (queue.isNotEmpty()) { item { OutlinedTextField(query, { query = it }, label = { Text("Search revision queue") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }; item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { FilterChip(selected = priority == null, onClick = { priority = null }, label = { Text("All") }); RevisionPriority.entries.forEach { option -> FilterChip(selected = priority == option, onClick = { priority = option }, label = { Text(option.label) }) } } }; item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { RevisionSort.entries.forEach { option -> FilterChip(selected = sort == option, onClick = { sort = option }, label = { Text(option.label()) }) } } } }
    if (queue.isEmpty()) item { Column(verticalArrangement = Arrangement.spacedBy(6.dp)) { Text(revisionEmptyMessage); Text(revisionEmptyAction, color = MaterialTheme.colorScheme.primary) } } else if (visible.isEmpty()) item { Text("No revision questions match this filter.") }
    items(visible, key = { it.question.id }) { item -> Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(item.question.questionText, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f)); Text(item.priority.label, color = MaterialTheme.colorScheme.primary) }; Text("${item.mistakes} mistakes across ${item.attempts} attempts"); Text(item.reasons.summary(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant); item.question.explanation?.takeIf(String::isNotBlank)?.let { Text("Explanation available", style = MaterialTheme.typography.labelMedium) } } } }
  }
}

private fun RevisionSort.label(): String = when (this) { RevisionSort.PRIORITY -> "Priority"; RevisionSort.MOST_MISTAKES -> "Mistakes"; RevisionSort.MOST_ATTEMPTED -> "Attempts" }

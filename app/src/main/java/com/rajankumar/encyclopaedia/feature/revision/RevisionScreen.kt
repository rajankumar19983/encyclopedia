package com.rajankumar.encyclopaedia.feature.revision

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
fun RevisionScreen() {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val questions by dao.observeQuestions().collectAsStateWithLifecycle(emptyList())
  val attempts by dao.observeAllAttempts().collectAsStateWithLifecycle(emptyList())
  val queue = remember(questions, attempts) { buildRevisionQueue(questions, attempts) }
  var query by remember { mutableStateOf("") }
  var priority by remember { mutableStateOf<RevisionPriority?>(null) }
  val visible = remember(queue, query, priority) { queue.filterRevisionQueue(priority, query) }
  val snapshot = remember(queue) { queue.snapshot() }

  LazyColumn(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    item { Text("Revision", style = MaterialTheme.typography.headlineMedium) }
    item { Text(snapshot.stats.summaryText(), style = MaterialTheme.typography.titleMedium) }
    item { Text(snapshot.recommendation, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    if (queue.isNotEmpty()) {
      item { OutlinedTextField(query, { query = it }, label = { Text("Search revision queue") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
      item {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          FilterChip(selected = priority == null, onClick = { priority = null }, label = { Text("All") })
          RevisionPriority.entries.forEach { option -> FilterChip(selected = priority == option, onClick = { priority = option }, label = { Text(option.label) }) }
        }
      }
    }
    if (queue.isEmpty()) item { Text(revisionEmptyMessage) }
    else if (visible.isEmpty()) item { Text("No revision questions match this filter.") }
    items(visible, key = { it.question.id }) { item ->
      Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(item.question.questionText, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
            Text(item.priority.label, color = MaterialTheme.colorScheme.primary)
          }
          Text("${item.mistakes} mistakes across ${item.attempts} attempts")
          Text(item.reasonLabel(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          item.question.explanation?.takeIf(String::isNotBlank)?.let { Text("Explanation available", style = MaterialTheme.typography.labelMedium) }
        }
      }
    }
  }
}

package com.rajankumar.encyclopaedia.feature.planner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase

@Composable
fun PlannerHistoryScreen(modifier: Modifier = Modifier) {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val tasks by dao.observeAllPlannerTasks().collectAsStateWithLifecycle(emptyList())
  val history = tasks.plannerHistory()
  val streak = history.completionStreak(plannerDate())
  val summary = history.historySummary()
  val consistency = history.consistencyPercent()

  LazyColumn(
    modifier = modifier.padding(28.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Text("Planner history", style = MaterialTheme.typography.headlineMedium)
      Text(
        "Review how much of each day's study plan you completed.",
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    if (history.isNotEmpty()) {
      item {
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Overall progress", style = MaterialTheme.typography.titleMedium)
            LinearProgressIndicator(progress = { summary.overallPercent / 100f }, modifier = Modifier.fillMaxWidth())
            Text("${summary.completedTasks} of ${summary.totalTasks} tasks completed (${summary.overallPercent}%)")
            Text("${summary.fullyCompletedDays} of ${summary.plannedDays} planned days fully completed", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
      item {
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Consistency", style = MaterialTheme.typography.titleMedium)
            LinearProgressIndicator(progress = { consistency / 100f }, modifier = Modifier.fillMaxWidth())
            Text("$consistency% of planned days fully completed")
          }
        }
      }
    }

    if (streak > 0) {
      item {
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Completion streak", style = MaterialTheme.typography.titleMedium)
            Text("$streak ${if (streak == 1) "day" else "days"} in a row")
          }
        }
      }
    }

    if (history.isEmpty()) {
      item { Text("No planner history yet.") }
    } else {
      items(history, key = { it.date }) { day ->
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(plannerDisplayDate(day.date), style = MaterialTheme.typography.titleMedium)
            LinearProgressIndicator(progress = { day.percent / 100f }, modifier = Modifier.fillMaxWidth())
            Text("${day.completed} of ${day.total} completed (${day.percent}%)")
          }
        }
      }
    }
  }
}

package com.rajankumar.encyclopaedia.feature.planner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase
import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PlannerScreen() {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val scope = rememberCoroutineScope()
  val today = remember { plannerDate() }
  val tasks by dao.observePlannerTasks(today).collectAsStateWithLifecycle(emptyList())
  val orderedTasks = tasks.orderedForPlanner()
  val progress = tasks.plannerProgress()
  val carryOver = tasks.carryOverSummary()
  val stats = tasks.completionStats()
  var title by remember { mutableStateOf("") }

  LaunchedEffect(today) {
    withContext(Dispatchers.IO) {
      val overdue = dao.getIncompletePlannerTasksBefore(today)
      carryIncompleteTasks(overdue, today).forEach { dao.upsertPlannerTask(it) }
    }
  }

  LazyColumn(
    Modifier.fillMaxSize().padding(28.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Text("Daily Planner", style = MaterialTheme.typography.headlineMedium)
      Text(plannerDisplayDate(today), color = MaterialTheme.colorScheme.onSurfaceVariant)
      Text(progress.summaryText(), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 6.dp))
    }
    if (carryOver.carriedCount > 0) {
      item {
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Carried-over work", style = MaterialTheme.typography.titleMedium)
            Text("${carryOver.carriedCount} unfinished ${if (carryOver.carriedCount == 1) "task was" else "tasks were"} moved into today.")
            carryOver.oldestSourceDate?.let {
              Text("Oldest pending since ${plannerDisplayDate(it)}.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        }
      }
    }
    if (progress.total > 0) {
      item {
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Today's progress", style = MaterialTheme.typography.titleMedium)
            LinearProgressIndicator(
              progress = { progress.percent / 100f },
              modifier = Modifier.fillMaxWidth()
            )
            Text("${progress.completed} of ${progress.total} completed • ${progress.remaining} remaining (${progress.percent}%)")
            Text(
              "Completed ${stats.completedToday} • Carried pending ${stats.carriedPending} • New pending ${stats.newlyPlannedPending}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
    item {
      Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("Add today's task", style = MaterialTheme.typography.titleMedium)
          OutlinedTextField(
            value = title,
            onValueChange = { title = it.take(160) },
            label = { Text("Study task") },
            supportingText = { Text("${title.length}/160") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
          Button(
            enabled = isValidPlannerTaskTitle(title),
            onClick = {
              val cleanTitle = normalizePlannerTaskTitle(title)
              title = ""
              scope.launch(Dispatchers.IO) {
                dao.upsertPlannerTask(
                  PlannerTaskEntity(
                    id = UUID.randomUUID().toString(),
                    title = cleanTitle,
                    scheduledDate = today
                  )
                )
              }
            }
          ) { Text("Add task") }
        }
      }
    }
    if (orderedTasks.isEmpty()) {
      item { Text("No tasks planned for today yet.") }
    } else {
      items(orderedTasks, key = { it.id }) { task ->
        Card(Modifier.fillMaxWidth()) {
          Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Checkbox(
              checked = task.isCompleted,
              onCheckedChange = { completed ->
                scope.launch(Dispatchers.IO) {
                  dao.setPlannerTaskCompleted(task.id, completed, if (completed) System.currentTimeMillis() else null)
                }
              }
            )
            Column(Modifier.weight(1f)) {
              Text(task.title, style = MaterialTheme.typography.titleMedium)
              task.carriedFromDate?.let {
                Text("Carried from ${plannerDisplayDate(it)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
            IconButton(onClick = {
              scope.launch(Dispatchers.IO) { dao.deletePlannerTask(task.id) }
            }) { Text("×") }
          }
        }
      }
    }
  }
}

package com.rajankumar.encyclopaedia.feature.planner

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
  val todaySummary = tasks.plannerTodaySummary()
  val carryWarning = tasks.plannerCarryWarning()
  val capacity = tasks.plannerCapacity()
  val recommendation = tasks.dailyPlannerRecommendation()
  val priorities = tasks.plannerPriorities()
  val nextAction = tasks.plannerNextAction()
  val backlogHealth = tasks.backlogHealth()
  val backlogPressure = tasks.backlogPressurePercent()
  var title by remember { mutableStateOf("") }
  var showHistory by remember { mutableStateOf(false) }
  var editingTaskId by remember { mutableStateOf<String?>(null) }
  var editTitle by remember { mutableStateOf("") }
  var taskToDelete by remember { mutableStateOf<PlannerTaskEntity?>(null) }

  LaunchedEffect(today) { withContext(Dispatchers.IO) { carryIncompleteTasks(dao.getIncompletePlannerTasksBefore(today), today).forEach { dao.upsertPlannerTask(it) } } }

  taskToDelete?.let { task -> AlertDialog(onDismissRequest = { taskToDelete = null }, title = { Text("Delete task?") }, text = { Text(plannerDeleteMessage(task.title)) }, confirmButton = { TextButton(onClick = { taskToDelete = null; if (editingTaskId == task.id) { editingTaskId = null; editTitle = "" }; scope.launch(Dispatchers.IO) { dao.deletePlannerTask(task.id) } }) { Text("Delete") } }, dismissButton = { TextButton(onClick = { taskToDelete = null }) { Text("Cancel") } }) }

  if (showHistory) {
    Column(Modifier.fillMaxSize()) { OutlinedButton(onClick = { showHistory = false }, modifier = Modifier.padding(start = 28.dp, top = 20.dp)) { Text("Back to today") }; PlannerHistoryScreen(Modifier.weight(1f)) }
    return
  }

  LazyColumn(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    item { Text("Daily Planner", style = MaterialTheme.typography.headlineMedium); Text(plannerDisplayDate(today), color = MaterialTheme.colorScheme.onSurfaceVariant); Text(progress.accessibilitySummary, modifier = Modifier.padding(top = 6.dp)); OutlinedButton(onClick = { showHistory = true }, modifier = Modifier.padding(top = 10.dp)) { Text("View history") } }
    item { Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) { Text(todaySummary.status.displayText(), style = MaterialTheme.typography.titleMedium); Text(todaySummary.focus.headline()); Text(todaySummary.supportingText(), color = MaterialTheme.colorScheme.onSurfaceVariant); carryWarning?.let { Text(it.message, style = MaterialTheme.typography.bodySmall) }; Text(capacity.message, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant); Text("${backlogHealth.label()} • ${backlogPressureLabel(backlogPressure)}", style = MaterialTheme.typography.bodySmall); Text(backlogHealth.guidance(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
    item { Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) { Text(recommendation.title, style = MaterialTheme.typography.titleMedium); Text(recommendation.detail, color = MaterialTheme.colorScheme.onSurfaceVariant); nextAction?.let { Text("Next: ${it.title}", style = MaterialTheme.typography.bodyLarge); Text(it.reason, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }; if (priorities.size > 1) { Text("Up next", style = MaterialTheme.typography.labelLarge); priorities.drop(1).forEach { Text("${it.rank}. ${it.task.title} • ${it.reason}", style = MaterialTheme.typography.bodySmall) } } } } }
    if (carryOver.hasBacklog) item { Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) { Text("Carried-over work", style = MaterialTheme.typography.titleMedium); Text(carryOver.summary); carryOver.oldestSourceDate?.let { Text("Oldest pending since ${plannerDisplayDate(it)}.", color = MaterialTheme.colorScheme.onSurfaceVariant) } } } }
    if (progress.safeTotal > 0) item { Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text("Today's progress", style = MaterialTheme.typography.titleMedium); LinearProgressIndicator(progress = { progress.percent / 100f }, modifier = Modifier.fillMaxWidth()); Text("${progress.accessibilitySummary} • ${progress.remaining} remaining (${progress.percent}%)"); Text(stats.summary, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
    item { Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) { Text("Add today's task", style = MaterialTheme.typography.titleMedium); OutlinedTextField(title, { title = it.take(MAX_PLANNER_TASK_LENGTH) }, label = { Text("Study task") }, supportingText = { Text(plannerTaskValidationMessage(title) ?: "${title.length}/$MAX_PLANNER_TASK_LENGTH") }, modifier = Modifier.fillMaxWidth(), singleLine = true); Button(enabled = isValidPlannerTaskTitle(title), onClick = { val clean = normalizePlannerTaskTitle(title); title = ""; scope.launch(Dispatchers.IO) { dao.upsertPlannerTask(PlannerTaskEntity(UUID.randomUUID().toString(), clean, today)) } }) { Text("Add task") } } } }
    if (orderedTasks.isEmpty()) item { Text("No tasks planned for today yet.") } else items(orderedTasks, key = { it.id }) { task -> Card(Modifier.fillMaxWidth()) { Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) { Checkbox(task.isCompleted, { completed -> scope.launch(Dispatchers.IO) { dao.setPlannerTaskCompleted(task.id, completed, if (completed) System.currentTimeMillis() else null) } }); Column(Modifier.weight(1f)) { if (editingTaskId == task.id) { OutlinedTextField(editTitle, { editTitle = plannerTaskEditValue(it) }, label = { Text("Study task") }, modifier = Modifier.fillMaxWidth(), singleLine = true); Row { Button(enabled = canSavePlannerTaskEdit(editTitle), onClick = { val saved = savedPlannerTaskTitle(editTitle); editingTaskId = null; editTitle = ""; scope.launch(Dispatchers.IO) { dao.updatePlannerTaskTitle(task.id, saved) } }) { Text("Save") }; OutlinedButton(onClick = { editingTaskId = null; editTitle = "" }) { Text("Cancel") } } } else { Text(task.title, style = MaterialTheme.typography.titleMedium); task.carriedFromDate?.let { Text("Carried from ${plannerDisplayDate(it)}", style = MaterialTheme.typography.bodySmall) }; TextButton(onClick = { editingTaskId = task.id; editTitle = plannerTaskEditValue(task.title) }) { Text("Edit") } } }; IconButton(onClick = { taskToDelete = task }) { Text("×") } } } }
  }
}

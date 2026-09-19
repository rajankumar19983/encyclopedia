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
import java.util.Locale

@Composable
fun PlannerHistoryScreen(modifier: Modifier = Modifier) {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val tasks by dao.observeAllPlannerTasks().collectAsStateWithLifecycle(emptyList())
  val history = tasks.plannerHistory()
  val streak = history.completionStreak(plannerDate())
  val longestStreak = history.longestCompletionStreak()
  val summary = history.historySummary()
  val consistency = history.consistencyPercent()
  val recentCompletion = history.recentCompletionPercent()
  val recentPerfectDays = history.recentPerfectDayPercent()
  val taskBalance = history.taskBalance()
  val activeSpan = history.activeDaySpan()
  val bestDay = history.bestPlannerDay()
  val busiestDay = history.busiestPlannerDay()
  val averageTasks = history.averageTasksPerPlannedDay()
  val recentAverageTasks = history.recentAveragePlanSize()
  val trend = history.completionTrend()
  val carryRate = tasks.carryOverRatePercent()
  val pendingAge = tasks.oldestPendingAgeDays(plannerDate())
  val pendingLoad = tasks.pendingLoad()
  val carriedRecovery = tasks.carriedCompletionPercent()
  val backlogPressure = tasks.backlogPressurePercent()

  LazyColumn(modifier = modifier.padding(28.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    item {
      Text("Planner history", style = MaterialTheme.typography.headlineMedium)
      Text("Review how much of each day's study plan you completed.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    if (history.isNotEmpty()) {
      item {
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Overall progress", style = MaterialTheme.typography.titleMedium)
            LinearProgressIndicator(progress = { summary.overallPercent / 100f }, modifier = Modifier.fillMaxWidth())
            Text("${summary.completedTasks} of ${summary.totalTasks} tasks completed (${summary.overallPercent}%)")
            Text("${summary.fullyCompletedDays} of ${summary.plannedDays} planned days fully completed", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Task balance: ${taskBalance.completed} completed • ${taskBalance.pending} pending")
          }
        }
      }
      item {
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Consistency", style = MaterialTheme.typography.titleMedium)
            LinearProgressIndicator(progress = { consistency / 100f }, modifier = Modifier.fillMaxWidth())
            Text("$consistency% of planned days fully completed")
            Text("Recent perfect days: $recentPerfectDays%")
            Text("Current streak: $streak ${if (streak == 1) "day" else "days"} • Best streak: $longestStreak ${if (longestStreak == 1) "day" else "days"}")
          }
        }
      }
      item {
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Study planning insights", style = MaterialTheme.typography.titleMedium)
            Text("Recent completion: $recentCompletion%")
            Text("Trend: ${trend.displayText()}")
            Text("Planner history span: $activeSpan ${if (activeSpan == 1L) "day" else "days"}")
            Text("Average plan size: ${String.format(Locale.US, "%.1f", averageTasks)} tasks/day")
            Text("Recent average plan size: ${String.format(Locale.US, "%.1f", recentAverageTasks)} tasks/day")
            Text("Carry-over rate: $carryRate%")
            Text("Carried-task recovery: $carriedRecovery%")
            Text("Backlog pressure: $backlogPressure% of pending work is carried")
            Text("Pending workload: ${pendingLoad.totalPending} (${pendingLoad.carriedPending} carried, ${pendingLoad.newPending} new)")
            Text("Oldest pending task: $pendingAge ${if (pendingAge == 1L) "day" else "days"}")
            bestDay?.let { Text("Best day: ${plannerDisplayDate(it.date)} • ${it.completed}/${it.total} completed") }
            busiestDay?.let { Text("Busiest day: ${plannerDisplayDate(it.date)} • ${it.total} tasks planned") }
          }
        }
      }
    }
    if (history.isEmpty()) item { Text("No planner history yet.") }
    else items(history, key = { it.date }) { day ->
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

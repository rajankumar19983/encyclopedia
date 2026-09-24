package com.rajankumar.encyclopaedia.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase
import com.rajankumar.encyclopaedia.feature.planner.plannerDate

private val statIcons = listOf(Icons.Default.AutoStories, Icons.Default.Quiz, Icons.Default.LocalFireDepartment, Icons.Default.CheckCircle)

@Composable
fun HomeScreen(onOpenPlanner: () -> Unit = {}, onPractice: () -> Unit = {}, onImport: () -> Unit = {}, onAddNotes: () -> Unit = {}, onRevision: () -> Unit = {}) {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val topicCount by dao.observeTopicCount().collectAsStateWithLifecycle(0)
  val questionCount by dao.observeQuestionCount().collectAsStateWithLifecycle(0)
  val attemptCount by dao.observeAttemptCount().collectAsStateWithLifecycle(0)
  val correctCount by dao.observeCorrectAttemptCount().collectAsStateWithLifecycle(0)
  val practisedCount by dao.observePractisedQuestionCount().collectAsStateWithLifecycle(0)
  val todayTasks by dao.observePlannerTasks(remember { plannerDate() }).collectAsStateWithLifecycle(emptyList())
  val todayPlan = todayTasks.toHomeTodayPlan()
  val dashboard = HomeDashboardSnapshot(topicCount, questionCount, attemptCount, correctCount, practisedCount).normalized().dashboard()

  Row(Modifier.fillMaxSize().padding(24.dp), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(18.dp)) {
      HomeHeader()
      LazyVerticalGrid(columns = GridCells.Fixed(4), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.heightIn(max = 150.dp)) { items(dashboard.stats.zip(statIcons)) { (stat, icon) -> StatCard(stat, icon) } }
      TodayPlanCard(todayPlan, onOpenPlanner)
      QuickActionsCard(onPractice, onImport, onAddNotes, onRevision)
    }
    Column(Modifier.width(280.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
      CalendarCard(onOpenPlanner)
      UpcomingCard(todayPlan, onOpenPlanner)
      MotivationCard(dashboard.motivation)
    }
  }
}

@Composable private fun HomeHeader() { var query by remember { mutableStateOf("") }; Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)) { Column(Modifier.weight(1f)) { Text(homeGreeting(), style = MaterialTheme.typography.headlineMedium); Text("Let's continue your preparation journey.", color = MaterialTheme.colorScheme.onSurfaceVariant) }; OutlinedTextField(query, { query = it }, leadingIcon = { Icon(Icons.Default.Search, null) }, placeholder = { Text("Search topics, questions, notes...") }, singleLine = true, shape = RoundedCornerShape(14.dp), modifier = Modifier.width(330.dp)) } }
@Composable private fun StatCard(stat: HomeStatModel, icon: ImageVector) { Card(Modifier.fillMaxWidth().semantics { contentDescription = stat.accessibilityDescription() }, shape = RoundedCornerShape(16.dp)) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Icon(icon, null, tint = MaterialTheme.colorScheme.primary); Text(stat.title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(stat.value, style = MaterialTheme.typography.headlineMedium); LinearProgressIndicator(progress = { stat.progress }, modifier = Modifier.fillMaxWidth()) } } }
@Composable private fun TodayPlanCard(plan: HomeTodayPlan, onOpenPlanner: () -> Unit) { Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) { Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { Text("Today's Plan", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f)); TextButton(onClick = onOpenPlanner) { Text("View Daily Routine") } }; Text(plan.nextTask ?: "Plan your study day", style = MaterialTheme.typography.titleMedium); Text(plan.summary, color = MaterialTheme.colorScheme.onSurfaceVariant); LinearProgressIndicator(progress = { plan.progress }, modifier = Modifier.fillMaxWidth()) } } }
@Composable private fun QuickActionsCard(onPractice: () -> Unit, onImport: () -> Unit, onAddNotes: () -> Unit, onRevision: () -> Unit) { Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) { Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { Text("Quick Actions", style = MaterialTheme.typography.titleLarge); Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) { QuickAction(Icons.Default.Quiz, "Practice MCQs", onPractice, Modifier.weight(1f)); QuickAction(Icons.Default.UploadFile, "Import PYQs", onImport, Modifier.weight(1f)); QuickAction(Icons.Default.EditNote, "Add Notes", onAddNotes, Modifier.weight(1f)); QuickAction(Icons.Default.Refresh, "Start Revision", onRevision, Modifier.weight(1f)) } } } }
@Composable private fun QuickAction(icon: ImageVector, label: String, onClick: () -> Unit, modifier: Modifier = Modifier) { OutlinedCard(onClick = onClick, modifier = modifier, shape = RoundedCornerShape(14.dp)) { Column(Modifier.fillMaxWidth().padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) { Icon(icon, null, tint = MaterialTheme.colorScheme.primary); Text(label, style = MaterialTheme.typography.labelLarge) } } }
@Composable private fun CalendarCard(onOpenPlanner: () -> Unit) { Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) { Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) { Text("Calendar", style = MaterialTheme.typography.titleMedium); Text("Plan and review your study days from Daily Routine.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant); OutlinedButton(onClick = onOpenPlanner, modifier = Modifier.fillMaxWidth()) { Text("Open Daily Routine") } } } }
@Composable private fun UpcomingCard(plan: HomeTodayPlan, onOpenPlanner: () -> Unit) { Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) { Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { Text("Upcoming", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f)); TextButton(onClick = onOpenPlanner) { Text("View") } }; Text(plan.nextTask ?: "No upcoming study tasks yet.", color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
@Composable private fun MotivationCard(message: String) { Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), shape = RoundedCornerShape(16.dp)) { Row(Modifier.fillMaxWidth().padding(18.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.LocalFireDepartment, null, tint = MaterialTheme.colorScheme.primary); Column { Text("Keep the streak alive!", style = MaterialTheme.typography.titleSmall); Text(message, style = MaterialTheme.typography.bodySmall) } } } }

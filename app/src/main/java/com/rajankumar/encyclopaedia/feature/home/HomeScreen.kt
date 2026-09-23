package com.rajankumar.encyclopaedia.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase

private val statIcons = listOf(Icons.Default.AutoStories, Icons.Default.Quiz, Icons.Default.LocalFireDepartment, Icons.Default.CheckCircle)

@Composable
fun HomeScreen() {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val topicCount by dao.observeTopicCount().collectAsStateWithLifecycle(0)
  val questionCount by dao.observeQuestionCount().collectAsStateWithLifecycle(0)
  val attemptCount by dao.observeAttemptCount().collectAsStateWithLifecycle(0)
  val correctCount by dao.observeCorrectAttemptCount().collectAsStateWithLifecycle(0)
  val practisedCount by dao.observePractisedQuestionCount().collectAsStateWithLifecycle(0)
  val snapshot = HomeDashboardSnapshot(topicCount, questionCount, attemptCount, correctCount, practisedCount).normalized()
  val dashboard = snapshot.dashboard()
  val emptyState = homeEmptyState(snapshot.topicCount, snapshot.questionCount)

  Column(Modifier.fillMaxSize().padding(horizontal = 28.dp, vertical = 24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
      Text(homeGreeting(), style = MaterialTheme.typography.headlineMedium)
      Text(dashboard.motivation, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    LazyVerticalGrid(columns = GridCells.Adaptive(210.dp), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxSize()) {
      items(dashboard.stats.zip(statIcons)) { (stat, icon) -> StatCard(stat, icon) }
      if (emptyState.visible) item(span = { GridItemSpan(maxLineSpan) }) { EmptyStateCard(emptyState) }
      item(span = { GridItemSpan(maxLineSpan) }) { TodayPlanCard(dashboard.recommendation) }
    }
  }
}

@Composable
private fun StatCard(stat: HomeStatModel, icon: ImageVector) {
  Card(
    Modifier.fillMaxWidth().semantics { contentDescription = stat.accessibilityDescription() },
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
        Text(stat.title, style = MaterialTheme.typography.titleMedium)
      }
      Text(stat.value, style = MaterialTheme.typography.headlineLarge)
      Text(stat.detail, color = MaterialTheme.colorScheme.onSurfaceVariant)
      LinearProgressIndicator(progress = { stat.progress }, modifier = Modifier.fillMaxWidth())
    }
  }
}

@Composable
private fun TodayPlanCard(recommendation: HomePlanRecommendation) {
  Card(
    Modifier.fillMaxWidth().semantics { contentDescription = recommendation.accessibilityDescription() },
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
  ) {
    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Bolt, null, tint = MaterialTheme.colorScheme.primary)
        Text("Today's Focus", style = MaterialTheme.typography.titleLarge)
      }
      Text(recommendation.title, style = MaterialTheme.typography.titleMedium)
      Text(recommendation.detail)
    }
  }
}

@Composable
private fun EmptyStateCard(state: HomeEmptyState) {
  Card(Modifier.fillMaxWidth()) {
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
      Text(state.title, style = MaterialTheme.typography.titleMedium)
      Text(state.detail, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

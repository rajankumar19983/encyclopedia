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
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Scanner
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase

private data class Stat(val title: String, val value: String, val detail: String, val icon: ImageVector, val progress: Float)

@Composable
fun HomeScreen() {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val topicCount by dao.observeTopicCount().collectAsStateWithLifecycle(0)
  val questionCount by dao.observeQuestionCount().collectAsStateWithLifecycle(0)

  Column(
    modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp, vertical = 24.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
      Text("Good morning, Rajan! 👋", style = MaterialTheme.typography.headlineMedium)
      Text("Let's make today a productive study day.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }

    LazyVerticalGrid(
      columns = GridCells.Adaptive(210.dp),
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      items(listOf(
        Stat("Topics", topicCount.toString(), if (topicCount == 0) "Build your knowledge tree" else "Stored in your knowledge tree", Icons.Default.AutoStories, if (topicCount > 0) 1f else 0f),
        Stat("Questions", questionCount.toString(), if (questionCount == 0) "Your question bank is ready" else "Stored locally on this device", Icons.Default.Quiz, if (questionCount > 0) 1f else 0f),
        Stat("Day Streak", "0", "Practice tracking comes next", Icons.Default.LocalFireDepartment, 0f),
        Stat("Overall Progress", "0%", "Practice to build progress", Icons.Default.CheckCircle, 0f)
      )) { StatCard(it) }

      item(span = { GridItemSpan(maxLineSpan) }) { TodayPlanCard() }
      item(span = { GridItemSpan(maxLineSpan) }) { QuickActionsCard() }
    }
  }
}

@Composable
private fun StatCard(stat: Stat) {
  Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Icon(stat.icon, null, tint = MaterialTheme.colorScheme.primary)
        Text(stat.title, style = MaterialTheme.typography.titleMedium)
      }
      Text(stat.value, style = MaterialTheme.typography.headlineLarge)
      Text(stat.detail, color = MaterialTheme.colorScheme.onSurfaceVariant)
      LinearProgressIndicator(progress = { stat.progress }, modifier = Modifier.fillMaxWidth())
    }
  }
}

@Composable
private fun TodayPlanCard() {
  Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Bolt, null, tint = MaterialTheme.colorScheme.primary)
        Text("Today's Plan", style = MaterialTheme.typography.titleLarge)
      }
      Text("Your daily learning, revision and practice tasks will appear here.")
      Text("Complete the plan first, then explore anything you want.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

@Composable
private fun QuickActionsCard() {
  Card(Modifier.fillMaxWidth()) {
    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
      Text("Quick Actions", style = MaterialTheme.typography.titleLarge)
      Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
        Action(Icons.Default.Quiz, "Add Question")
        Action(Icons.Default.Scanner, "Scan Questions")
        Action(Icons.Default.PictureAsPdf, "Import PDF")
        Action(Icons.Default.Bolt, "Random Practice")
      }
    }
  }
}

@Composable
private fun Action(icon: ImageVector, label: String) {
  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
    Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
    Text(label, style = MaterialTheme.typography.titleSmall)
  }
}

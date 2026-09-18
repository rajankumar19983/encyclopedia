package com.rajankumar.encyclopaedia.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rajankumar.encyclopaedia.feature.common.FeaturePlaceholderScreen
import com.rajankumar.encyclopaedia.feature.home.HomeScreen
import com.rajankumar.encyclopaedia.feature.knowledge.KnowledgeScreen
import com.rajankumar.encyclopaedia.feature.questions.QuestionBankScreen

@Composable
fun EncyclopaediaShell(navController: NavHostController, useNavigationRail: Boolean) {
  val backStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = backStackEntry?.destination?.route

  if (useNavigationRail) {
    Row(Modifier.fillMaxSize()) {
      NavigationRail(header = {
        Text("Encyclopaedia", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
      }) {
        AppDestination.entries.forEach { destination ->
          NavigationRailItem(
            selected = currentRoute == destination.route,
            onClick = { navigateSingleTop(navController, destination.route) },
            icon = { Icon(destination.icon, destination.label) },
            label = { Text(destination.label) }
          )
        }
      }
      Box(Modifier.weight(1f)) { EncyclopaediaNavHost(navController) }
    }
  } else {
    val compact = listOf(AppDestination.Home, AppDestination.Learn, AppDestination.Questions, AppDestination.Practice, AppDestination.Settings)
    Scaffold(bottomBar = {
      NavigationBar {
        compact.forEach { destination ->
          NavigationBarItem(
            selected = currentRoute == destination.route,
            onClick = { navigateSingleTop(navController, destination.route) },
            icon = { Icon(destination.icon, destination.label) },
            label = { Text(destination.label) }
          )
        }
      }
    }) { padding ->
      Box(Modifier.padding(padding)) { EncyclopaediaNavHost(navController) }
    }
  }
}

@Composable
private fun EncyclopaediaNavHost(navController: NavHostController) {
  NavHost(navController, startDestination = AppDestination.Home.route) {
    composable(AppDestination.Home.route) { HomeScreen() }
    composable(AppDestination.Learn.route) { KnowledgeScreen() }
    composable(AppDestination.Questions.route) { QuestionBankScreen() }
    composable(AppDestination.Practice.route) { FeaturePlaceholderScreen("Practice", "Practice by topic, difficulty, source, mistakes, bookmarks or random selection.") }
    composable(AppDestination.Revision.route) { FeaturePlaceholderScreen("Revision", "Your mistakes, weak topics, bookmarks and scheduled revisions will collect here.") }
    composable(AppDestination.Planner.route) { FeaturePlaceholderScreen("Daily Planner", "Plan learning, revision, practice and PYQ work for each day.") }
    composable(AppDestination.Pyq.route) { FeaturePlaceholderScreen("PYQ Papers", "Organize official previous-year questions by exam, year, paper and shift.") }
    composable(AppDestination.Performance.route) { FeaturePlaceholderScreen("Performance", "Accuracy, attempts, streaks, topic performance and weak areas will use your real study data.") }
    composable(AppDestination.Backup.route) { FeaturePlaceholderScreen("Backup & Restore", "Export and safely restore your complete local Encyclopaedia data.") }
    composable(AppDestination.Settings.route) { FeaturePlaceholderScreen("Settings", "Configure study preferences, accessibility, OCR/import behavior and optional AI services.") }
  }
}

private fun navigateSingleTop(navController: NavHostController, route: String) {
  navController.navigate(route) {
    launchSingleTop = true
    restoreState = true
    popUpTo(AppDestination.Home.route) { saveState = true }
  }
}

package com.rajankumar.encyclopaedia.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppDestination(
  val route: String,
  val label: String,
  val icon: ImageVector
) {
  Home("home", "Home", Icons.Default.Home),
  Learn("learn", "Knowledge & Lessons", Icons.Default.AutoStories),
  Questions("questions", "Question Bank", Icons.Default.Quiz),
  Practice("practice", "Practice", Icons.Default.Psychology),
  Revision("revision", "Revision", Icons.Default.Sync),
  Planner("planner", "Daily Planner", Icons.Default.CalendarMonth),
  Notebook("notebook", "Notebook", Icons.Default.EditNote),
  Pyq("pyq", "PYQ Papers", Icons.Default.Description),
  Performance("performance", "Performance", Icons.Default.Analytics),
  Backup("backup", "Backup & Restore", Icons.Default.Backup),
  Settings("settings", "Settings", Icons.Default.Settings)
}

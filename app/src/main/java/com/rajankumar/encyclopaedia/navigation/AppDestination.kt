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
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppDestination(
  val route: String,
  val label: String,
  val icon: ImageVector
) {
  Home("home", "Home", Icons.Default.Home),
  Learn("learn", "Topics & Knowledge", Icons.Default.AutoStories),
  Questions("questions", "Questions", Icons.Default.Quiz),
  Revision("revision", "Revision", Icons.Default.Sync),
  Planner("planner", "Daily Routine", Icons.Default.CalendarMonth),
  Pyq("pyq", "PYQ Papers", Icons.Default.Description),
  Performance("performance", "Performance", Icons.Default.Analytics),
  QuestionEditor("question-editor", "Question Editor", Icons.Default.EditNote),
  Import("import", "Import / OCR", Icons.Default.UploadFile),
  Backup("backup", "Backup & Restore", Icons.Default.Backup),
  Settings("settings", "Settings", Icons.Default.Settings),
  Practice("practice", "Practice", Icons.Default.Psychology),
  Notebook("notebook", "Notebook", Icons.Default.EditNote)
}

val AppDestination.showInTabletSidebar: Boolean
  get() = this != AppDestination.Practice && this != AppDestination.Notebook

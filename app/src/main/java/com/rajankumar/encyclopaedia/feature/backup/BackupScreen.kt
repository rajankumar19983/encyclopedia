package com.rajankumar.encyclopaedia.feature.backup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BackupScreen() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(28.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text(defaultBackupScreenCopy.title, style = MaterialTheme.typography.headlineMedium)
    Text(defaultBackupScreenCopy.subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
    BackupSettingsSection()
    Text(BackupScreenSection.SAFETY.title, style = MaterialTheme.typography.titleMedium)
    backupSafetyPoints.forEach { point ->
      Text("• $point", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

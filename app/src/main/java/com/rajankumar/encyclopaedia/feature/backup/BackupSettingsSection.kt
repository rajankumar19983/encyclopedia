package com.rajankumar.encyclopaedia.feature.backup

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun BackupSettingsSection() {
  val context = LocalContext.current.applicationContext
  val scope = rememberCoroutineScope()
  val store = remember(context) { DeviceBackupStore(context) }
  val manager = remember(store) { DeviceBackupManager(store) }
  var configured by remember { mutableStateOf(store.configuredDirectory() != null) }
  var busy by remember { mutableStateOf(false) }
  var status by remember {
    mutableStateOf(if (configured) "Device backup folder configured." else "Choose a folder to enable device backups.")
  }
  var restorePoints by remember {
    mutableStateOf(runCatching { manager.discoverRestorePoints() }.getOrDefault(emptyList()))
  }

  val folderPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
    if (uri != null) {
      runCatching { store.setDirectory(uri) }
        .onSuccess {
          configured = true
          AutomaticBackupScheduler.schedule(context)
          restorePoints = runCatching { manager.discoverRestorePoints() }.getOrDefault(emptyList())
          status = "Backup folder saved. Automatic backup runs daily around midnight."
        }
        .onFailure { status = it.message ?: "Could not use that folder." }
    }
  }

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text("Backup & restore", style = MaterialTheme.typography.titleLarge)
    Text(
      "Keep up to five validated restore points on your device. Automatic backup runs daily around local midnight.",
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(status, color = MaterialTheme.colorScheme.onSurfaceVariant)

    OutlinedButton(onClick = { folderPicker.launch(store.configuredDirectory()) }) {
      Text(if (configured) "Change backup folder" else "Choose backup folder")
    }

    Button(
      enabled = configured && !busy,
      onClick = {
        busy = true
        status = "Creating backup…"
        scope.launch {
          runCatching {
            withContext(Dispatchers.IO) {
              val dao = EncyclopaediaDatabase.get(context).dao()
              manager.create(createBackupSnapshot(dao), BackupType.MANUAL)
            }
          }.onSuccess { point ->
            restorePoints = runCatching { manager.discoverRestorePoints() }.getOrDefault(emptyList())
            status = "Backup created: ${point.name}"
          }.onFailure { status = it.message ?: "Backup failed." }
          busy = false
        }
      }
    ) {
      Text(if (busy) "Backing up…" else "Back up now")
    }

    Text("Restore points (${restorePoints.count { it.valid }}/$MAX_RESTORE_POINTS)", style = MaterialTheme.typography.titleMedium)
    if (restorePoints.isEmpty()) {
      Text("No backups found yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    } else {
      restorePoints.take(MAX_RESTORE_POINTS).forEach { point ->
        Text(
          "${if (point.valid) "✓" else "⚠"} ${point.name}",
          style = MaterialTheme.typography.bodySmall,
          color = if (point.valid) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
        )
      }
    }
  }
}

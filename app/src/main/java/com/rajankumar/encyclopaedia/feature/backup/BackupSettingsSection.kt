package com.rajankumar.encyclopaedia.feature.backup

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
    mutableStateOf(
      if (configured) "Backup folder configured."
      else "Reinstalled the app? Choose your previous backup folder to find existing restore points."
    )
  }
  var restorePoints by remember { mutableStateOf(runCatching { manager.discoverRestorePoints() }.getOrDefault(emptyList())) }
  var pendingRestore by remember { mutableStateOf<BackupRestorePoint?>(null) }
  var discoveredAfterReconnect by remember { mutableStateOf<List<BackupRestorePoint>>(emptyList()) }

  val folderPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
    if (uri != null) runCatching { store.setDirectory(uri) }
      .onSuccess {
        configured = true
        AutomaticBackupScheduler.schedule(context)
        restorePoints = runCatching { manager.discoverRestorePoints() }.getOrDefault(emptyList())
        val valid = restorePoints.filter { it.valid }.take(MAX_RESTORE_POINTS)
        if (valid.isNotEmpty()) {
          discoveredAfterReconnect = valid
          status = "Found ${valid.size} existing backup${if (valid.size == 1) "" else "s"} in this location."
        } else {
          status = "Backup location saved. No existing restore points were found. Automatic backup runs daily around midnight."
        }
      }.onFailure { status = it.message ?: "Could not use that backup location." }
  }

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text("Backup & restore", style = MaterialTheme.typography.titleLarge)
    Text(
      "Choose any folder Android makes available, including device storage or a cloud provider such as Google Drive. After reinstalling, choose the same folder and Encyclopaedia will discover its backups automatically.",
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(status, color = MaterialTheme.colorScheme.onSurfaceVariant)
    OutlinedButton(onClick = { folderPicker.launch(store.configuredDirectory()) }) {
      Text(if (configured) "Change backup location" else "Choose or reconnect backup location")
    }
    Button(enabled = configured && !busy, onClick = {
      busy = true
      status = "Creating backup…"
      scope.launch {
        runCatching { withContext(Dispatchers.IO) {
          val dao = EncyclopaediaDatabase.get(context).dao()
          manager.create(createBackupSnapshot(dao), BackupType.MANUAL)
        }}.onSuccess { point ->
          restorePoints = runCatching { manager.discoverRestorePoints() }.getOrDefault(emptyList())
          status = "Backup created: ${point.name}"
        }.onFailure { status = it.message ?: "Backup failed." }
        busy = false
      }
    }) { Text(if (busy) "Working…" else "Back up now") }

    Text("Restore points (${restorePoints.count { it.valid }}/$MAX_RESTORE_POINTS)", style = MaterialTheme.typography.titleMedium)
    if (restorePoints.isEmpty()) Text("No backups found yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    restorePoints.take(MAX_RESTORE_POINTS).forEach { point ->
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("${if (point.valid) "✓" else "⚠"} ${point.name}", style = MaterialTheme.typography.bodySmall, color = if (point.valid) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error)
        if (point.valid) OutlinedButton(enabled = !busy, onClick = { pendingRestore = point }) { Text("Restore this backup") }
      }
    }
  }

  if (discoveredAfterReconnect.isNotEmpty()) {
    val newest = discoveredAfterReconnect.first()
    AlertDialog(
      onDismissRequest = { discoveredAfterReconnect = emptyList() },
      title = { Text("Existing backups found") },
      text = {
        Text("Found ${discoveredAfterReconnect.size} valid restore point${if (discoveredAfterReconnect.size == 1) "" else "s"}. The newest is ${newest.name}. You can restore it now or choose another version from the list.")
      },
      confirmButton = {
        TextButton(onClick = {
          discoveredAfterReconnect = emptyList()
          pendingRestore = newest
        }) { Text("Restore newest") }
      },
      dismissButton = {
        TextButton(onClick = { discoveredAfterReconnect = emptyList() }) { Text("Choose another") }
      }
    )
  }

  pendingRestore?.let { point ->
    AlertDialog(
      onDismissRequest = { pendingRestore = null },
      title = { Text("Restore this backup?") },
      text = { Text("Your current study data will be replaced with ${point.name}. A safety backup of the current data will be created first. Continue only if you want to replace the current data.") },
      confirmButton = {
        TextButton(onClick = {
          pendingRestore = null
          busy = true
          status = "Creating safety backup before restore…"
          scope.launch {
            runCatching { withContext(Dispatchers.IO) {
              val dao = EncyclopaediaDatabase.get(context).dao()
              manager.create(createBackupSnapshot(dao), BackupType.MANUAL)
              val file = store.discover().firstOrNull { it.name == point.name }
                ?: error("Selected backup file is no longer available")
              val snapshot = manager.load(file.uri)
              dao.restoreSnapshot(snapshot)
            }}.onSuccess {
              restorePoints = runCatching { manager.discoverRestorePoints() }.getOrDefault(emptyList())
              status = "Restore completed successfully. A pre-restore safety backup was kept."
            }.onFailure { status = "Restore stopped: ${it.message ?: "unknown error"}. Current data was not intentionally changed unless the database transaction completed." }
            busy = false
          }
        }) { Text("Create safety backup & restore") }
      },
      dismissButton = { TextButton(onClick = { pendingRestore = null }) { Text("Cancel") } }
    )
  }
}

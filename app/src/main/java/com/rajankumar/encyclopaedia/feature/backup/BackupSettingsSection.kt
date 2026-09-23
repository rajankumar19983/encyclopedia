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
  var status by remember { mutableStateOf(if (configured) "Backup folder configured." else "Reinstalled the app? Choose your previous backup folder to find existing restore points.") }
  var restorePoints by remember { mutableStateOf(runCatching { manager.discoverRestorePoints() }.getOrDefault(emptyList())) }
  var restoreCandidate by remember { mutableStateOf<BackupRestoreCandidate?>(null) }
  var pendingDelete by remember { mutableStateOf<BackupRestorePoint?>(null) }
  var confirmDeleteAll by remember { mutableStateOf(false) }
  var discoveredAfterReconnect by remember { mutableStateOf<List<BackupRestorePoint>>(emptyList()) }

  fun refresh() { restorePoints = runCatching { manager.discoverRestorePoints() }.getOrDefault(emptyList()) }
  fun reviewForRestore(point: BackupRestorePoint) {
    busy = true
    status = "Inspecting backup before restore…"
    scope.launch {
      runCatching { withContext(Dispatchers.IO) { manager.prepareRestore(point) } }
        .onSuccess { candidate ->
          restoreCandidate = candidate
          val model = candidate.inspection.restoreReviewModel()
          status = when {
            model.blockingIssueCount > 0 -> "Restore blocked: ${model.blockingIssueCount} integrity issue${if (model.blockingIssueCount == 1) "" else "s"} must not be restored."
            model.warningCount > 0 -> "Backup inspected. Review ${model.warningCount} warning${if (model.warningCount == 1) "" else "s"} before restoring."
            else -> "Backup inspected and ready to restore."
          }
        }
        .onFailure { status = "Could not inspect backup: ${it.message ?: "unknown error"}." }
      busy = false
    }
  }

  val folderPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
    if (uri != null) runCatching { store.setDirectory(uri) }.onSuccess {
      configured = true
      AutomaticBackupScheduler.schedule(context)
      refresh()
      val valid = restorePoints.filter { it.valid }.take(MAX_RESTORE_POINTS)
      if (valid.isNotEmpty()) {
        discoveredAfterReconnect = valid
        status = "Found ${valid.size} existing backup${if (valid.size == 1) "" else "s"} in this location."
      } else status = "Backup location saved. No existing restore points were found. Automatic backup runs daily around midnight."
    }.onFailure { status = it.message ?: "Could not use that backup location." }
  }

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text("Backup & restore", style = MaterialTheme.typography.titleLarge)
    Text("Choose any folder Android makes available, including device storage or a cloud provider such as Google Drive. After reinstalling, choose the same folder and Encyclopaedia will discover its backups automatically.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    Text(status, color = MaterialTheme.colorScheme.onSurfaceVariant)
    OutlinedButton(onClick = { folderPicker.launch(store.configuredDirectory()) }) { Text(if (configured) "Change backup location" else "Choose or reconnect backup location") }
    Button(enabled = configured && !busy, onClick = {
      busy = true; status = "Creating backup…"
      scope.launch {
        runCatching { withContext(Dispatchers.IO) { manager.create(createBackupSnapshot(EncyclopaediaDatabase.get(context).dao()), BackupType.MANUAL) } }
          .onSuccess { point -> refresh(); status = "Backup created: ${point.name}" }
          .onFailure { status = it.message ?: "Backup failed." }
        busy = false
      }
    }) { Text(if (busy) "Working…" else "Back up now") }

    Text("Restore points (${restorePoints.count { it.valid }}/$MAX_RESTORE_POINTS)", style = MaterialTheme.typography.titleMedium)
    if (restorePoints.isEmpty()) Text("No backups found yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    restorePoints.take(MAX_RESTORE_POINTS).forEach { point ->
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("${if (point.valid) "✓" else "⚠"} ${point.name}", style = MaterialTheme.typography.bodySmall, color = if (point.valid) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error)
        if (point.valid) OutlinedButton(enabled = !busy, onClick = { reviewForRestore(point) }) { Text("Inspect & restore") }
        TextButton(enabled = !busy, onClick = { pendingDelete = point }) { Text("Delete backup", color = MaterialTheme.colorScheme.error) }
      }
    }
    if (configured && restorePoints.isNotEmpty()) {
      TextButton(enabled = !busy, onClick = { confirmDeleteAll = true }) { Text("Delete all backup data", color = MaterialTheme.colorScheme.error) }
      Text("Deleting the app does not automatically delete backups stored in your chosen folder. Delete them here first if you want the backup copies removed too.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }

  if (discoveredAfterReconnect.isNotEmpty()) {
    val newest = discoveredAfterReconnect.first()
    AlertDialog(onDismissRequest = { discoveredAfterReconnect = emptyList() }, title = { Text("Existing backups found") }, text = { Text("Found ${discoveredAfterReconnect.size} valid restore point${if (discoveredAfterReconnect.size == 1) "" else "s"}. The newest is ${newest.name}. It will be inspected before restore.") }, confirmButton = { TextButton(onClick = { discoveredAfterReconnect = emptyList(); reviewForRestore(newest) }) { Text("Inspect newest") } }, dismissButton = { TextButton(onClick = { discoveredAfterReconnect = emptyList() }) { Text("Choose another") } })
  }

  pendingDelete?.let { point ->
    AlertDialog(onDismissRequest = { pendingDelete = null }, title = { Text("Delete this backup?") }, text = { Text("${point.name} will be permanently deleted from the selected backup location. This cannot be undone.") }, confirmButton = { TextButton(onClick = {
      pendingDelete = null; busy = true
      scope.launch {
        runCatching { withContext(Dispatchers.IO) { store.deleteByName(point.name) } }.onSuccess { deleted -> refresh(); status = if (deleted) "Backup deleted." else "Backup was not found." }.onFailure { status = it.message ?: "Could not delete backup." }
        busy = false
      }
    }) { Text("Delete permanently", color = MaterialTheme.colorScheme.error) } }, dismissButton = { TextButton(onClick = { pendingDelete = null }) { Text("Cancel") } })
  }

  if (confirmDeleteAll) {
    AlertDialog(onDismissRequest = { confirmDeleteAll = false }, title = { Text("Delete all backup data?") }, text = { Text("Every Encyclopaedia backup in the selected location will be permanently deleted. Your current in-app study data is not deleted, but you will lose these recovery versions.") }, confirmButton = { TextButton(onClick = {
      confirmDeleteAll = false; busy = true
      scope.launch {
        runCatching { withContext(Dispatchers.IO) { store.deleteAllBackups() } }.onSuccess { count -> refresh(); status = "Deleted $count backup${if (count == 1) "" else "s"}." }.onFailure { status = it.message ?: "Could not delete backups." }
        busy = false
      }
    }) { Text("Delete all permanently", color = MaterialTheme.colorScheme.error) } }, dismissButton = { TextButton(onClick = { confirmDeleteAll = false }) { Text("Cancel") } })
  }

  restoreCandidate?.let { candidate ->
    BackupRestoreReviewDialog(
      candidate = candidate,
      onDismiss = { restoreCandidate = null },
      onRestore = {
        restoreCandidate = null; busy = true; status = "Creating safety backup before restore…"
        scope.launch {
          runCatching { withContext(Dispatchers.IO) {
            val dao = EncyclopaediaDatabase.get(context).dao()
            manager.create(createBackupSnapshot(dao), BackupType.MANUAL)
            dao.restoreSnapshot(manager.load(candidate.uri))
          } }.onSuccess { refresh(); status = "Restore completed successfully. A pre-restore safety backup was kept." }.onFailure { status = "Restore stopped: ${it.message ?: "unknown error"}." }
          busy = false
        }
      }
    )
  }
}

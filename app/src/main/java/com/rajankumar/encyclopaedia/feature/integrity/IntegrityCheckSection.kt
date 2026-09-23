package com.rajankumar.encyclopaedia.feature.integrity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase
import com.rajankumar.encyclopaedia.feature.backup.createBackupSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun IntegrityCheckSection() {
  val context = LocalContext.current.applicationContext
  val scope = rememberCoroutineScope()
  var running by remember { mutableStateOf(false) }
  var result by remember { mutableStateOf<IntegrityCheckPresentation?>(null) }
  var failure by remember { mutableStateOf<String?>(null) }

  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Text(IntegrityCheckCopy.heading, style = MaterialTheme.typography.titleMedium)
    Text(IntegrityCheckCopy.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
    failure?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
    Button(enabled = !running, onClick = {
      running = true
      failure = null
      scope.launch {
        runCatching {
          withContext(Dispatchers.IO) {
            createBackupSnapshot(EncyclopaediaDatabase.get(context).dao()).integrityReport().toCheckPresentation()
          }
        }.onSuccess { result = it }
          .onFailure { failure = "Integrity check could not complete: ${it.message ?: "unknown error"}." }
        running = false
      }
    }) { Text(if (running) IntegrityCheckCopy.runningAction else IntegrityCheckCopy.runAction) }
  }

  result?.let { presentation -> IntegrityCheckDialog(presentation, onDismiss = { result = null }) }
}

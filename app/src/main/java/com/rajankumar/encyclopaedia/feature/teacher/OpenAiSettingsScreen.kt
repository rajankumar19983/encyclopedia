package com.rajankumar.encyclopaedia.feature.teacher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun OpenAiSettingsScreen(
  manager: OpenAiConnectionManager,
  settings: OpenAiSettings
) {
  val scope = rememberCoroutineScope()
  val guidance = remember { openAiKeyGuidance() }
  var apiKey by remember { mutableStateOf("") }
  var config by remember { mutableStateOf(settings.read()) }
  var configured by remember { mutableStateOf(manager.isConfigured()) }
  var testing by remember { mutableStateOf(false) }
  var status by remember { mutableStateOf(if (configured) "OpenAI API is connected on this device." else "Not connected") }
  var confirmRemove by remember { mutableStateOf(false) }

  Column(
    Modifier.fillMaxWidth().padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text("AI Teacher", style = MaterialTheme.typography.headlineSmall)
    Text(guidance.localStorageNotice, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Text(guidance.riskNotice, color = MaterialTheme.colorScheme.error)
    Text(guidance.rotationNotice, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Text(status, color = if (configured) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)

    OutlinedTextField(
      value = apiKey,
      onValueChange = { apiKey = it },
      modifier = Modifier.fillMaxWidth(),
      label = { Text(if (configured) "Replace API key" else "OpenAI API key") },
      visualTransformation = PasswordVisualTransformation(),
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
      singleLine = true
    )

    Button(
      onClick = {
        testing = true
        status = "Testing connection…"
        scope.launch {
          manager.verify(apiKey)
            .onSuccess { count ->
              manager.saveVerifiedKey(apiKey)
              apiKey = ""
              configured = true
              status = "Connected. $count compatible model${if (count == 1) "" else "s"} available."
            }
            .onFailure { error -> status = classifyTeacherFailure(error.message).userMessage() }
          testing = false
        }
      },
      enabled = apiKey.isNotBlank() && !testing
    ) {
      if (testing) CircularProgressIndicator() else Text(if (configured) "Test & replace key" else "Test & connect")
    }

    Text("Response depth", style = MaterialTheme.typography.titleMedium)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      TeacherResponseDepth.entries.forEach { depth ->
        FilterChip(
          selected = config.responseDepth == depth,
          onClick = {
            config = config.copy(responseDepth = depth)
            settings.save(config)
          },
          label = { Text(depth.label) }
        )
      }
    }

    Text(
      "Model: Automatic. Encyclopaedia discovers compatible models and can move away from a retired model.",
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    if (configured) {
      TextButton(onClick = { confirmRemove = true }) { Text("Remove OpenAI connection") }
    }
  }

  if (confirmRemove) {
    AlertDialog(
      onDismissRequest = { confirmRemove = false },
      title = { Text("Remove OpenAI connection?") },
      text = { Text("The API key stored on this device will be deleted. Your study data will not be affected.") },
      confirmButton = {
        TextButton(onClick = {
          manager.removeKey()
          configured = false
          status = "Not connected"
          confirmRemove = false
        }) { Text("Remove") }
      },
      dismissButton = { TextButton(onClick = { confirmRemove = false }) { Text("Cancel") } }
    )
  }
}

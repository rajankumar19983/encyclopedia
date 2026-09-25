package com.rajankumar.encyclopaedia.feature.knowledge

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun AiApiKeyDialog(
  hasSavedKey: Boolean,
  onSave: (String) -> Unit,
  onClear: () -> Unit,
  onDismiss: () -> Unit,
) {
  var apiKey by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("OpenAI API key") },
    text = {
      OutlinedTextField(
        value = apiKey,
        onValueChange = { apiKey = it },
        label = { Text(if (hasSavedKey) "Replace saved key" else "API key") },
        supportingText = {
          Text("Stored only in this app's private device storage. The key is never added to study data.")
        },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
      )
    },
    confirmButton = {
      TextButton(
        onClick = { onSave(apiKey.trim()) },
        enabled = apiKey.isNotBlank(),
      ) {
        Text(if (hasSavedKey) "Replace" else "Save")
      }
    },
    dismissButton = {
      if (hasSavedKey) {
        TextButton(onClick = onClear) { Text("Remove key") }
      } else {
        TextButton(onClick = onDismiss) { Text("Cancel") }
      }
    },
  )
}

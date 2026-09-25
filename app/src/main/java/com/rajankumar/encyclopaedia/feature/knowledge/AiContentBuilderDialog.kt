package com.rajankumar.encyclopaedia.feature.knowledge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AiContentBuilderDialog(
  state: AiGenerationUiState,
  hasApiKey: Boolean,
  destinationLabel: String?,
  onTopicChange: (String) -> Unit,
  onDepthChange: (AiContentDepth) -> Unit,
  onIncludeLessonsChange: (Boolean) -> Unit,
  onGenerate: () -> Unit,
  onManageApiKey: () -> Unit,
  onDismiss: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = { if (!state.isGenerating) onDismiss() },
    title = { Text("Build knowledge with AI") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
          if (destinationLabel == null) {
            "Generate a new subject hierarchy. Nothing is saved until you review and approve it."
          } else {
            "Generate content inside $destinationLabel. Nothing is saved until you review and approve it."
          },
        )
        OutlinedTextField(
          value = state.topic,
          onValueChange = onTopicChange,
          label = { Text("Topic or syllabus area") },
          enabled = !state.isGenerating,
          modifier = Modifier.fillMaxWidth(),
        )
        Text("Depth")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          AiContentDepth.entries.forEach { depth ->
            if (state.depth == depth) {
              Button(
                onClick = { onDepthChange(depth) },
                enabled = !state.isGenerating,
              ) { Text(depth.displayName()) }
            } else {
              OutlinedButton(
                onClick = { onDepthChange(depth) },
                enabled = !state.isGenerating,
              ) { Text(depth.displayName()) }
            }
          }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Checkbox(
            checked = state.includeLessons,
            onCheckedChange = onIncludeLessonsChange,
            enabled = !state.isGenerating,
          )
          Text("Generate permanent lesson content")
        }
        if (!hasApiKey) {
          Text("An OpenAI API key is required before generation.")
        }
        TextButton(onClick = onManageApiKey, enabled = !state.isGenerating) {
          Text(if (hasApiKey) "Manage OpenAI API key" else "Add OpenAI API key")
        }
        state.errorMessage?.let { Text(it) }
        if (state.isGenerating) {
          Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CircularProgressIndicator()
            Text("Generating a reviewable draft…")
          }
        }
      }
    },
    confirmButton = {
      TextButton(
        onClick = onGenerate,
        enabled = state.canGenerate && hasApiKey,
      ) { Text("Generate draft") }
    },
    dismissButton = {
      TextButton(onClick = onDismiss, enabled = !state.isGenerating) { Text("Cancel") }
    },
  )
}

private fun AiContentDepth.displayName(): String = when (this) {
  AiContentDepth.QUICK -> "Quick"
  AiContentDepth.STANDARD -> "Standard"
  AiContentDepth.DEEP -> "Deep"
}

package com.rajankumar.encyclopaedia.feature.backup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BackupRestoreReviewDialog(candidate: BackupRestoreCandidate, onDismiss: () -> Unit, onRestore: (BackupRestoreCandidate) -> Unit) {
  var warningsAcknowledged by remember(candidate.point.name) { mutableStateOf(false) }
  val presentation = candidate.presentation(warningsAcknowledged)
  val sections = candidate.inspection.restoreReviewSections().orderedForRestoreReview()
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(presentation.headline) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(presentation.description)
        if (presentation.badges.isNotEmpty()) Text(presentation.badges.joinToString(" • ") { "${it.label}: ${it.count}" }, style = MaterialTheme.typography.labelLarge)
        sections.forEach { section ->
          Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(section.group.label(), style = MaterialTheme.typography.titleSmall)
            section.messages.forEach { message -> Text("• $message", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
          }
        }
        if (presentation.acknowledgementRequired || warningsAcknowledged) {
          Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = warningsAcknowledged, onCheckedChange = { warningsAcknowledged = it })
            Text("I reviewed the warnings and want to continue.", style = MaterialTheme.typography.bodySmall)
          }
        }
        Text("A safety backup of your current study data will be created before anything is replaced.", style = MaterialTheme.typography.bodySmall)
      }
    },
    confirmButton = { TextButton(enabled = presentation.actionEnabled, onClick = { onRestore(candidate) }) { Text(presentation.restoreActionText()) } },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
  )
}

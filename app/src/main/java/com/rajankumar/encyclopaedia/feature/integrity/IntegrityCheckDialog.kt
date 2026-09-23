package com.rajankumar.encyclopaedia.feature.integrity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun IntegrityCheckDialog(
  presentation: IntegrityCheckPresentation,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(presentation.title) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(presentation.summary)
        Text("Status: ${presentation.status}", style = MaterialTheme.typography.titleSmall)
        Text("${presentation.recordsChecked} records checked • ${presentation.blockingIssues} blocking • ${presentation.warnings} warnings", style = MaterialTheme.typography.bodySmall)
        presentation.details.forEach { detail ->
          Text("• ${detail.message}", style = MaterialTheme.typography.bodySmall, color = if (detail.severity == IntegritySeverity.ERROR) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }
    },
    confirmButton = { TextButton(onClick = onDismiss) { Text(IntegrityCheckCopy.closeAction) } }
  )
}

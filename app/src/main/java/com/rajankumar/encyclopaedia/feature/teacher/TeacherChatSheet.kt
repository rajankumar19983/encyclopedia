package com.rajankumar.encyclopaedia.feature.teacher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherChatSheet(
  onDismiss: () -> Unit,
  onSend: (TeacherRequest) -> Unit
) {
  var input by remember { mutableStateOf("") }
  val messages = TeacherConversation.messages
  val context = TeacherContextStore.current
  val contextSummary = context.summary()
  val suggestions = teacherQuestionSuggestions(context)
  val validation = validateTeacherInput(input)

  ModalBottomSheet(onDismissRequest = onDismiss) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.weight(1f)) {
          Text("AI Teacher", style = MaterialTheme.typography.headlineSmall)
          Text(contextSummary.label, style = MaterialTheme.typography.titleSmall)
          Text(contextSummary.detail, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (messages.isNotEmpty()) TextButton(onClick = TeacherConversation::clear) { Text("Clear") }
      }

      LazyColumn(
        modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp, max = 420.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        if (messages.isEmpty()) {
          item { Text("Ask for an explanation, a simpler version, exam traps, or a concept check.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
        items(messages, key = { it.id }) { message ->
          Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp)) {
              Text(if (message.role == TeacherMessageRole.STUDENT) "You" else "Teacher", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
              Text(message.text)
            }
          }
        }
      }

      Text("Try asking", style = MaterialTheme.typography.labelLarge)
      suggestions.forEach { suggestion ->
        SuggestionChip(onClick = { input = suggestion }, label = { Text(suggestion) })
      }

      OutlinedTextField(
        value = input,
        onValueChange = { input = it },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Ask your teacher") },
        supportingText = { if (!validation.valid && input.isNotEmpty()) Text(validation.message.orEmpty()) },
        minLines = 2,
        maxLines = 5
      )
      Text(teacherPrivacyNotice(context), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Button(
        onClick = {
          val checked = validateTeacherInput(input)
          if (checked.valid) {
            TeacherConversation.addStudentMessage(checked.normalized)
            onSend(createTeacherRequest(checked.normalized))
            input = ""
          }
        },
        enabled = validation.valid,
        modifier = Modifier.fillMaxWidth()
      ) { Text("Ask") }
    }
  }
}

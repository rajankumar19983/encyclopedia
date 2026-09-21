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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TeacherChatSheet(
  onDismiss: () -> Unit,
  onSend: (TeacherRequest) -> Unit
) {
  var input by remember { mutableStateOf("") }
  val messages = TeacherConversation.messages
  val context = TeacherContextStore.current

  ModalBottomSheet(onDismissRequest = onDismiss) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
          Text("AI Teacher", style = MaterialTheme.typography.headlineSmall)
          Text(
            if (context == null) "Ask any study question" else "Using context from ${context.screen}",
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        if (messages.isNotEmpty()) {
          TextButton(onClick = TeacherConversation::clear) { Text("Clear") }
        }
      }

      LazyColumn(
        modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp, max = 420.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        if (messages.isEmpty()) {
          item {
            Text(
              "Ask for an explanation, a simpler version, exam traps, or why an option is right or wrong.",
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
        items(messages, key = { it.id }) { message ->
          Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp)) {
              Text(
                if (message.role == TeacherMessageRole.STUDENT) "You" else "Teacher",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
              )
              Text(message.text)
            }
          }
        }
      }

      OutlinedTextField(
        value = input,
        onValueChange = { input = it },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Ask your teacher") },
        minLines = 2,
        maxLines = 5
      )
      Button(
        onClick = {
          val question = input.trim()
          if (question.isNotEmpty()) {
            TeacherConversation.addStudentMessage(question)
            onSend(createTeacherRequest(question))
            input = ""
          }
        },
        enabled = input.isNotBlank(),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text("Ask")
      }
    }
  }
}

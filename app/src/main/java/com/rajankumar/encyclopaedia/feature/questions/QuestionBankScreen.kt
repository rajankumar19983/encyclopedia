package com.rajankumar.encyclopaedia.feature.questions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import java.util.UUID
import kotlinx.coroutines.launch

@Composable
fun QuestionBankScreen() {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val questions by dao.observeQuestions().collectAsStateWithLifecycle(emptyList())
  val scope = rememberCoroutineScope()
  var adding by remember { mutableStateOf(false) }

  Column(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Column {
        Text("Question Bank", style = MaterialTheme.typography.headlineMedium)
        Text("${questions.size} questions stored locally", color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
      Button(onClick = { adding = true }) {
        Icon(Icons.Default.Add, null)
        Text(" Add Question")
      }
    }

    if (questions.isEmpty()) Text("No questions yet. Add one manually; scanner and PDF import are coming next.")
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      items(questions, key = { it.id }) { question ->
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(question.questionText, style = MaterialTheme.typography.titleMedium)
            Text("Answer: ${question.correctAnswer}", color = MaterialTheme.colorScheme.primary)
            Text("${question.source} • ${question.difficulty}", style = MaterialTheme.typography.labelMedium)
          }
        }
      }
    }
  }

  if (adding) {
    AddQuestionDialog(
      onDismiss = { adding = false },
      onSave = { text, options, answer, explanation ->
        scope.launch {
          dao.upsertQuestion(
            QuestionEntity(
              id = UUID.randomUUID().toString(),
              questionText = text.trim(),
              options = options.trim(),
              correctAnswer = answer.trim(),
              explanation = explanation.trim().ifBlank { null }
            )
          )
        }
        adding = false
      }
    )
  }
}

@Composable
private fun AddQuestionDialog(
  onDismiss: () -> Unit,
  onSave: (String, String, String, String) -> Unit
) {
  var text by remember { mutableStateOf("") }
  var options by remember { mutableStateOf("") }
  var answer by remember { mutableStateOf("") }
  var explanation by remember { mutableStateOf("") }
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Add question") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(text, { text = it }, label = { Text("Question") })
        OutlinedTextField(options, { options = it }, label = { Text("Options (one per line)") })
        OutlinedTextField(answer, { answer = it }, label = { Text("Correct answer") })
        OutlinedTextField(explanation, { explanation = it }, label = { Text("Explanation (optional)") })
      }
    },
    confirmButton = {
      TextButton(
        onClick = { onSave(text, options, answer, explanation) },
        enabled = text.isNotBlank() && answer.isNotBlank()
      ) { Text("Save") }
    },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
  )
}

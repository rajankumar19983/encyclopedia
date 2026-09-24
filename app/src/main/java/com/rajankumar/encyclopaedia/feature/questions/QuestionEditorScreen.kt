package com.rajankumar.encyclopaedia.feature.questions

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import java.util.UUID
import kotlinx.coroutines.launch

@Composable
fun QuestionEditorScreen(onDone: () -> Unit) {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val scope = rememberCoroutineScope()
  var question by remember { mutableStateOf("") }
  var options by remember { mutableStateOf("") }
  var answer by remember { mutableStateOf("") }
  var explanation by remember { mutableStateOf("") }
  var status by remember { mutableStateOf<String?>(null) }
  val draft = ManualQuestionDraft(question, options, answer, explanation)
  val started = question.isNotBlank() || options.isNotBlank() || answer.isNotBlank()

  Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    Text("Question Editor", style = MaterialTheme.typography.headlineMedium)
    Text("Create an English MCQ manually. Questions are stored only on this device.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    OutlinedTextField(question, { question = it; status = null }, label = { Text("Question") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
    OutlinedTextField(options, { options = it; status = null }, label = { Text("Options — one per line (2–6)") }, modifier = Modifier.fillMaxWidth(), minLines = 4)
    OutlinedTextField(answer, { answer = it.take(2).uppercase(); status = null }, label = { Text("Correct option (A, B… or 1, 2…)") })
    OutlinedTextField(explanation, { explanation = it }, label = { Text("Explanation (optional)") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
    if (started) draft.validationMessage()?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
    status?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      Button(enabled = draft.isValid(), onClick = {
        scope.launch {
          val inserted = dao.saveImportedQuestionIfUnique(QuestionEntity(UUID.randomUUID().toString(), question.trim(), draft.options.joinToString("\n"), draft.normalizedAnswer, explanation.trim(), "USER", "UNRATED"), null)
          if (inserted) { question = ""; options = ""; answer = ""; explanation = ""; status = "Question saved." }
          else status = "This question already exists in the Question Bank."
        }
      }) { Text("Save Question") }
      TextButton(onClick = onDone) { Text("Back") }
    }
  }
}

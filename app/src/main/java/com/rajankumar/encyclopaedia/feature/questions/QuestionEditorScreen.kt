package com.rajankumar.encyclopaedia.feature.questions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
  val cleanedOptions = options.lines().map(String::trim).filter(String::isNotBlank)
  val valid = question.isNotBlank() && cleanedOptions.size in 2..6 && answer.trim().isNotBlank()

  Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    Text("Question Editor", style = MaterialTheme.typography.headlineMedium)
    Text("Create an English MCQ manually. Questions are stored only on this device.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    OutlinedTextField(question, { question = it }, label = { Text("Question") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
    OutlinedTextField(options, { options = it }, label = { Text("Options — one per line (2–6)") }, modifier = Modifier.fillMaxWidth(), minLines = 4)
    OutlinedTextField(answer, { answer = it.take(2).uppercase() }, label = { Text("Correct option (A, B… or 1, 2…)") })
    OutlinedTextField(explanation, { explanation = it }, label = { Text("Explanation (optional)") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
    status?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      Button(enabled = valid, onClick = {
        scope.launch {
          dao.insertQuestion(QuestionEntity(UUID.randomUUID().toString(), question.trim(), cleanedOptions.joinToString("\n"), answer.trim(), explanation.trim(), "USER", "UNRATED"))
          question = ""; options = ""; answer = ""; explanation = ""; status = "Question saved."
        }
      }) { Text("Save Question") }
      TextButton(onClick = onDone) { Text("Back") }
    }
  }
}

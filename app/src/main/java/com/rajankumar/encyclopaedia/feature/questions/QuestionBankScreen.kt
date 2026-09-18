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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase
import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import java.util.UUID
import kotlinx.coroutines.launch

@Composable
fun QuestionBankScreen() {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val questions by dao.observeQuestions().collectAsStateWithLifecycle(emptyList())
  val topics by dao.observeAllNodes().collectAsStateWithLifecycle(emptyList())
  val scope = rememberCoroutineScope()
  var editing by remember { mutableStateOf<QuestionEntity?>(null) }
  var showEditor by remember { mutableStateOf(false) }

  Column(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Column {
        Text("Question Bank", style = MaterialTheme.typography.headlineMedium)
        Text("${questions.size} questions stored locally", color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
      Button(onClick = { editing = null; showEditor = true }) {
        Icon(Icons.Default.Add, null)
        Text(" Add Question")
      }
    }

    if (questions.isEmpty()) Text("No questions yet. Add one manually; scanner and PDF import will feed this same bank.")
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      items(questions, key = { it.id }) { question ->
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text(question.questionText, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
              Row {
                IconButton(onClick = { editing = question; showEditor = true }) { Icon(Icons.Default.Edit, "Edit") }
                IconButton(onClick = { scope.launch { dao.deleteQuestion(question.id) } }) { Icon(Icons.Default.Delete, "Delete") }
              }
            }
            question.options.lines().filter { it.isNotBlank() }.forEachIndexed { index, option ->
              Text("${('A'.code + index).toChar()}. $option")
            }
            Text("Answer: ${question.correctAnswer}", color = MaterialTheme.colorScheme.primary)
            question.explanation?.let { Text("Explanation: $it", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            Text("${question.source} • ${question.difficulty}", style = MaterialTheme.typography.labelMedium)
          }
        }
      }
    }
  }

  if (showEditor) {
    QuestionEditorDialog(
      existing = editing,
      topics = topics,
      onDismiss = { showEditor = false; editing = null },
      onSave = { question, topicId ->
        scope.launch { dao.saveQuestion(question, topicId) }
        showEditor = false
        editing = null
      }
    )
  }
}

@Composable
private fun QuestionEditorDialog(
  existing: QuestionEntity?,
  topics: List<KnowledgeNodeEntity>,
  onDismiss: () -> Unit,
  onSave: (QuestionEntity, String?) -> Unit
) {
  var questionText by remember(existing?.id) { mutableStateOf(existing?.questionText.orEmpty()) }
  val initialOptions = existing?.options?.lines()?.filter { it.isNotBlank() }.orEmpty().let {
    if (it.size >= 2) it else listOf("", "", "", "")
  }
  val options = remember(existing?.id) { mutableStateListOf<String>().apply { addAll(initialOptions.take(6)) } }
  var answerIndex by remember(existing?.id) {
    mutableStateOf(('A'..'F').indexOf(existing?.correctAnswer?.trim()?.uppercase()?.firstOrNull()).coerceAtLeast(0))
  }
  var explanation by remember(existing?.id) { mutableStateOf(existing?.explanation.orEmpty()) }
  var selectedTopicId by remember(existing?.id) { mutableStateOf<String?>(null) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(if (existing == null) "Add MCQ" else "Edit MCQ") },
    text = {
      LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { OutlinedTextField(questionText, { questionText = it }, label = { Text("Question") }, modifier = Modifier.fillMaxWidth()) }
        items(options.size) { index ->
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            TextButton(onClick = { answerIndex = index }) { Text(if (answerIndex == index) "✓ ${('A'.code + index).toChar()}" else "${('A'.code + index).toChar()}") }
            OutlinedTextField(options[index], { options[index] = it }, label = { Text("Option ${index + 1}") }, modifier = Modifier.weight(1f))
            if (options.size > 2) TextButton(onClick = { options.removeAt(index); if (answerIndex >= options.size) answerIndex = 0 }) { Text("−") }
          }
        }
        if (options.size < 6) item { TextButton(onClick = { options.add("") }) { Text("+ Add option (${options.size}/6)") } }
        item { OutlinedTextField(explanation, { explanation = it }, label = { Text("Explanation (optional)") }, modifier = Modifier.fillMaxWidth()) }
        if (topics.isNotEmpty()) {
          item { Text("Topic (optional)", style = MaterialTheme.typography.titleSmall) }
          items(topics.take(12), key = { it.id }) { topic ->
            TextButton(onClick = { selectedTopicId = if (selectedTopicId == topic.id) null else topic.id }) {
              Text(if (selectedTopicId == topic.id) "✓ ${topic.name}" else topic.name)
            }
          }
        }
      }
    },
    confirmButton = {
      val validOptions = options.map { it.trim() }.filter { it.isNotBlank() }
      TextButton(
        enabled = questionText.isNotBlank() && validOptions.size >= 2 && answerIndex < validOptions.size,
        onClick = {
          val now = System.currentTimeMillis()
          onSave(
            QuestionEntity(
              id = existing?.id ?: UUID.randomUUID().toString(),
              questionText = questionText.trim(),
              options = validOptions.joinToString("\n"),
              correctAnswer = ('A'.code + answerIndex).toChar().toString(),
              explanation = explanation.trim().ifBlank { null },
              source = existing?.source ?: "USER",
              difficulty = existing?.difficulty ?: "MEDIUM",
              createdAt = existing?.createdAt ?: now,
              updatedAt = now
            ),
            selectedTopicId
          )
        }
      ) { Text("Save") }
    },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
  )
}

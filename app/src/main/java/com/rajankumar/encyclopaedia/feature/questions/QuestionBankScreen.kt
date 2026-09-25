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
import com.rajankumar.encyclopaedia.feature.importer.QuestionImportScreen
import java.util.UUID
import kotlinx.coroutines.launch

@Composable
fun QuestionBankScreen() {
  var importing by remember { mutableStateOf(false) }
  var practising by remember { mutableStateOf(false) }
  var generatingAi by remember { mutableStateOf(false) }
  if (importing) { QuestionImportScreen(onDone = { importing = false }); return }
  if (practising) { PracticeScreen(onDone = { practising = false }); return }
  if (generatingAi) { AiQuestionFlowScreen(onDone = { generatingAi = false }); return }

  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val questions by dao.observeQuestions().collectAsStateWithLifecycle(emptyList())
  val topics by dao.observeAllNodes().collectAsStateWithLifecycle(emptyList())
  val scope = rememberCoroutineScope()
  var editing by remember { mutableStateOf<QuestionEntity?>(null) }
  var showEditor by remember { mutableStateOf(false) }
  var query by remember { mutableStateOf("") }
  val visibleQuestions = remember(questions, query) { questions.searchQuestions(query) }
  val readiness = remember(questions) { questions.practiceReadiness() }

  Column(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Column {
        Text("Question Bank", style = MaterialTheme.typography.headlineMedium)
        Text("${questions.size} questions • ${readiness.ready} practice-ready", color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = { practising = true }, enabled = readiness.ready > 0) { Text("Practice") }
        Button(onClick = { generatingAi = true }) { Text("AI Generate") }
        Button(onClick = { importing = true }) { Text("Scan / PDF") }
        Button(onClick = { editing = null; showEditor = true }) { Icon(Icons.Default.Add, null); Text(" Add Question") }
      }
    }
    OutlinedTextField(query, { query = it }, label = { Text("Search questions, options or explanations") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    if (questions.isEmpty()) Text("No questions yet. Add one manually, generate a reviewed AI draft, or import printed MCQs from an image/PDF.")
    else if (visibleQuestions.isEmpty()) Text("No questions match your search.")
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      items(visibleQuestions, key = { it.id }) { question ->
        val quality = question.assessQuality()
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text(question.questionText, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
              Row {
                IconButton(onClick = { editing = question; showEditor = true }) { Icon(Icons.Default.Edit, "Edit") }
                IconButton(onClick = { scope.launch { dao.deleteQuestion(question.id) } }) { Icon(Icons.Default.Delete, "Delete") }
              }
            }
            question.optionList().forEachIndexed { index, option -> Text("${optionLetter(index)}. $option") }
            Text("Answer: ${question.correctAnswer}", color = MaterialTheme.colorScheme.primary)
            question.explanation?.takeIf(String::isNotBlank)?.let { Text("Explanation: $it", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            Text("${displayQuestionSource(question.source)} • ${normalizeDifficulty(question.difficulty)}", style = MaterialTheme.typography.labelMedium)
            if (!quality.usable) Text("Needs review: ${quality.issues.joinToString()}", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
          }
        }
      }
    }
  }

  if (showEditor) QuestionEditorDialog(existing = editing, topics = topics, onDismiss = { showEditor = false; editing = null }, onSave = { question, topicId -> scope.launch { dao.saveQuestion(question, topicId) }; showEditor = false; editing = null })
}

@Composable
private fun QuestionEditorDialog(existing: QuestionEntity?, topics: List<KnowledgeNodeEntity>, onDismiss: () -> Unit, onSave: (QuestionEntity, String?) -> Unit) {
  var questionText by remember(existing) { mutableStateOf(existing?.questionText.orEmpty()) }
  val initialOptions = existing?.options?.lines()?.filter { it.isNotBlank() }.orEmpty().ifEmpty { listOf("", "", "", "") }
  val options = remember(existing) { mutableStateListOf<String>().apply { addAll(initialOptions.take(6)) } }
  var correctIndex by remember(existing) { mutableStateOf((existing?.correctAnswer?.firstOrNull()?.uppercaseChar()?.code?.minus('A'.code) ?: 0).coerceIn(0, options.lastIndex)) }
  var explanation by remember(existing) { mutableStateOf(existing?.explanation.orEmpty()) }
  var difficulty by remember(existing) { mutableStateOf(existing?.difficulty ?: "MEDIUM") }
  var selectedTopic by remember { mutableStateOf<String?>(null) }
  val valid = questionText.isNotBlank() && options.size in 2..6 && options.all { it.isNotBlank() } && options.distinctBy { it.trim().lowercase() }.size == options.size && correctIndex in options.indices

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(if (existing == null) "Add MCQ" else "Edit MCQ") },
    text = {
      LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { OutlinedTextField(questionText, { questionText = it }, label = { Text("Question") }, modifier = Modifier.fillMaxWidth()) }
        items(options.size) { index ->
          Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            TextButton(onClick = { correctIndex = index }) { Text(if (correctIndex == index) "✓ ${optionLetter(index)}" else optionLetter(index)) }
            OutlinedTextField(options[index], { options[index] = it }, label = { Text("Option ${index + 1}") }, modifier = Modifier.weight(1f))
            if (options.size > 2) TextButton(onClick = { options.removeAt(index); correctIndex = correctIndex.coerceAtMost(options.lastIndex) }) { Text("−") }
          }
        }
        if (options.size < 6) item { TextButton(onClick = { options.add("") }) { Text("+ Add option") } }
        item { OutlinedTextField(explanation, { explanation = it }, label = { Text("Explanation (optional)") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(difficulty, { difficulty = it.uppercase() }, label = { Text("Difficulty") }) }
        if (topics.isNotEmpty()) {
          item { Text("Topic (optional)", style = MaterialTheme.typography.titleSmall) }
          items(topics.take(20)) { topic -> TextButton(onClick = { selectedTopic = if (selectedTopic == topic.id) null else topic.id }) { Text(if (selectedTopic == topic.id) "✓ ${topic.name}" else topic.name) } }
        }
      }
    },
    confirmButton = {
      TextButton(enabled = valid, onClick = {
        val now = System.currentTimeMillis()
        onSave(QuestionEntity(existing?.id ?: UUID.randomUUID().toString(), questionText.trim(), options.joinToString("\n") { it.trim() }, optionLetter(correctIndex), explanation.trim().ifBlank { null }, existing?.source ?: "USER", normalizeDifficulty(difficulty), existing?.createdAt ?: now, now), selectedTopic)
      }) { Text("Save") }
    },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
  )
}

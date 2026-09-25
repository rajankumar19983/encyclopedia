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
  var practicePreset by remember { mutableStateOf(QuestionBankFilterState()) }
  if (importing) { QuestionImportScreen(onDone = { importing = false }); return }
  if (practising) {
    PracticeScreen(
      onDone = { practising = false },
      initialFilters = practicePreset,
    )
    return
  }
  if (generatingAi) { AiQuestionFlowScreen(onDone = { generatingAi = false }); return }

  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val questions by dao.observeQuestions().collectAsStateWithLifecycle(emptyList())
  val topics by dao.observeAllNodes().collectAsStateWithLifecycle(emptyList())
  val questionTopics by dao.observeQuestionTopics().collectAsStateWithLifecycle(emptyList())
  val scope = rememberCoroutineScope()
  var editing by remember { mutableStateOf<QuestionEntity?>(null) }
  var showEditor by remember { mutableStateOf(false) }
  var filters by remember { mutableStateOf(QuestionBankFilterState()) }

  val visibleQuestions = remember(questions, questionTopics, filters) {
    questions.applyQuestionBankFilters(filters, questionTopics)
  }
  val visibleReadiness = remember(visibleQuestions) { visibleQuestions.practiceReadiness() }
  val sourceOptions = remember(questions) { availableQuestionSources(questions) }
  val topicNamesById = remember(topics) { topics.associate { it.id to it.name } }
  val topicIdByQuestionId = remember(questionTopics) {
    questionTopics.associate { it.questionId to it.knowledgeNodeId }
  }

  Column(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Column {
        Text("Question Bank", style = MaterialTheme.typography.headlineMedium)
        Text(
          "${visibleQuestions.size} shown • ${questions.size} total • ${visibleReadiness.ready} shown practice-ready",
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
          onClick = {
            practicePreset = filters
            practising = true
          },
          enabled = visibleReadiness.ready > 0,
        ) { Text("Practice") }
        Button(onClick = { generatingAi = true }) { Text("AI Generate") }
        Button(onClick = { importing = true }) { Text("Scan / PDF") }
        Button(onClick = { editing = null; showEditor = true }) { Icon(Icons.Default.Add, null); Text(" Add Question") }
      }
    }

    OutlinedTextField(
      value = filters.query,
      onValueChange = { filters = filters.copy(query = it) },
      label = { Text("Search questions, options or explanations") },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true,
    )

    QuestionFilterChoiceRow(
      title = "Source",
      choices = listOf(null to "All") + sourceOptions.map { source -> source to displayQuestionSource(source) },
      selected = filters.source,
      onSelect = { filters = filters.copy(source = it) },
    )
    QuestionFilterChoiceRow(
      title = "Difficulty",
      choices = listOf(null to "All") + questionDifficultyOptions.map { value -> value to value.lowercase().replaceFirstChar(Char::uppercase) },
      selected = filters.difficulty,
      onSelect = { filters = filters.copy(difficulty = it) },
    )
    if (topics.isNotEmpty()) {
      QuestionFilterChoiceRow(
        title = "Knowledge topic",
        choices = listOf(null to "All") + topics.map { topic -> topic.id to topic.name },
        selected = filters.topicId,
        onSelect = { filters = filters.copy(topicId = it) },
      )
    }

    if (filters.hasActiveFilters) {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
          "Showing ${visibleQuestions.size} of ${questions.size} questions",
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          style = MaterialTheme.typography.bodySmall,
        )
        TextButton(onClick = { filters = QuestionBankFilterState() }) { Text("Clear filters") }
      }
    }

    if (questions.isEmpty()) {
      Text("No questions yet. Add one manually, generate a reviewed AI draft, or import printed MCQs from an image/PDF.")
    } else if (visibleQuestions.isEmpty()) {
      Text("No questions match the current search and filters.")
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      items(visibleQuestions, key = { it.id }) { question ->
        val quality = question.assessQuality()
        val topicLabel = topicIdByQuestionId[question.id]?.let(topicNamesById::get)
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
            question.explanation?.takeIf(String::isNotBlank)?.let {
              Text("Explanation: $it", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
              buildString {
                append(displayQuestionSource(question.source))
                append(" • ")
                append(normalizeDifficulty(question.difficulty))
                topicLabel?.let {
                  append(" • ")
                  append(it)
                }
              },
              style = MaterialTheme.typography.labelMedium,
            )
            if (!quality.usable) {
              Text(
                "Needs review: ${quality.issues.joinToString()}",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
              )
            }
          }
        }
      }
    }
  }

  if (showEditor) {
    val initialTopicId = editing?.let { question -> questionTopics.topicIdForQuestion(question.id) }
    QuestionEditorDialog(
      existing = editing,
      topics = topics,
      initialTopicId = initialTopicId,
      onDismiss = { showEditor = false; editing = null },
      onSave = { question, topicId ->
        scope.launch { dao.saveQuestion(question, topicId) }
        showEditor = false
        editing = null
      },
    )
  }
}

@Composable
private fun QuestionEditorDialog(
  existing: QuestionEntity?,
  topics: List<KnowledgeNodeEntity>,
  initialTopicId: String?,
  onDismiss: () -> Unit,
  onSave: (QuestionEntity, String?) -> Unit,
) {
  var questionText by remember(existing) { mutableStateOf(existing?.questionText.orEmpty()) }
  val initialOptions = existing?.options?.lines()?.filter { it.isNotBlank() }.orEmpty().ifEmpty { listOf("", "", "", "") }
  val options = remember(existing) { mutableStateListOf<String>().apply { addAll(initialOptions.take(6)) } }
  var correctIndex by remember(existing) {
    mutableStateOf((existing?.correctAnswer?.firstOrNull()?.uppercaseChar()?.code?.minus('A'.code) ?: 0).coerceIn(0, options.lastIndex))
  }
  var explanation by remember(existing) { mutableStateOf(existing?.explanation.orEmpty()) }
  var difficulty by remember(existing) { mutableStateOf(existing?.difficulty ?: "MEDIUM") }
  var selectedTopic by remember(existing?.id, initialTopicId) { mutableStateOf(initialTopicId) }
  val valid = questionText.isNotBlank() && options.size in 2..6 && options.all { it.isNotBlank() } &&
    options.distinctBy { it.trim().lowercase() }.size == options.size && correctIndex in options.indices

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(if (existing == null) "Add MCQ" else "Edit MCQ") },
    text = {
      LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { OutlinedTextField(questionText, { questionText = it }, label = { Text("Question") }, modifier = Modifier.fillMaxWidth()) }
        items(options.size) { index ->
          Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            TextButton(onClick = { correctIndex = index }) {
              Text(if (correctIndex == index) "✓ ${optionLetter(index)}" else optionLetter(index))
            }
            OutlinedTextField(
              options[index],
              { options[index] = it },
              label = { Text("Option ${index + 1}") },
              modifier = Modifier.weight(1f),
            )
            if (options.size > 2) {
              TextButton(onClick = {
                options.removeAt(index)
                correctIndex = correctIndex.coerceAtMost(options.lastIndex)
              }) { Text("−") }
            }
          }
        }
        if (options.size < 6) item { TextButton(onClick = { options.add("") }) { Text("+ Add option") } }
        item { OutlinedTextField(explanation, { explanation = it }, label = { Text("Explanation (optional)") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(difficulty, { difficulty = it.uppercase() }, label = { Text("Difficulty") }) }
        if (topics.isNotEmpty()) {
          item { Text("Topic (optional)", style = MaterialTheme.typography.titleSmall) }
          items(topics, key = { it.id }) { topic ->
            TextButton(onClick = { selectedTopic = if (selectedTopic == topic.id) null else topic.id }) {
              Text(if (selectedTopic == topic.id) "✓ ${topic.name}" else topic.name)
            }
          }
        }
      }
    },
    confirmButton = {
      TextButton(
        enabled = valid,
        onClick = {
          val now = System.currentTimeMillis()
          onSave(
            QuestionEntity(
              id = existing?.id ?: UUID.randomUUID().toString(),
              questionText = questionText.trim(),
              options = options.joinToString("\n") { it.trim() },
              correctAnswer = optionLetter(correctIndex),
              explanation = explanation.trim().ifBlank { null },
              source = existing?.source ?: "USER",
              difficulty = normalizeDifficulty(difficulty),
              createdAt = existing?.createdAt ?: now,
              updatedAt = now,
            ),
            selectedTopic,
          )
        },
      ) { Text("Save") }
    },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
  )
}

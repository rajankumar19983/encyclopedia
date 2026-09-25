package com.rajankumar.encyclopaedia.feature.questions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase
import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity
import com.rajankumar.encyclopaedia.feature.accessibility.asSpeakableExplanation
import com.rajankumar.encyclopaedia.feature.accessibility.asSpeakableQuestion
import com.rajankumar.encyclopaedia.feature.accessibility.rememberTextToSpeechController
import com.rajankumar.encyclopaedia.feature.revision.RevisionPracticeRequest
import com.rajankumar.encyclopaedia.feature.teacher.PublishTeacherContext
import com.rajankumar.encyclopaedia.feature.teacher.asTeacherContext
import java.util.UUID
import kotlinx.coroutines.launch

@Composable
fun PracticeScreen(
  onDone: () -> Unit,
  initialFilters: QuestionBankFilterState = QuestionBankFilterState(),
) {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val scope = rememberCoroutineScope()
  val speech = rememberTextToSpeechController()
  val revisionIds = remember { RevisionPracticeRequest.consume() }
  val allQuestions by dao.observeQuestions().collectAsStateWithLifecycle(emptyList())
  val topics by dao.observeAllNodes().collectAsStateWithLifecycle(emptyList())
  val questionTopics by dao.observeQuestionTopics().collectAsStateWithLifecycle(emptyList())
  val allAttempts by dao.observeAllAttempts().collectAsStateWithLifecycle(emptyList())
  var setupPreset by remember(initialFilters) { mutableStateOf(initialFilters) }
  var config by remember { mutableStateOf<PracticeSessionConfig?>(null) }
  var questions by remember { mutableStateOf<List<QuestionEntity>?>(null) }
  var sessionId by remember { mutableStateOf(newPracticeSessionId()) }
  var index by remember { mutableStateOf(0) }
  var selected by remember { mutableStateOf<String?>(null) }
  var submitted by remember { mutableStateOf(false) }
  var startedAt by remember { mutableLongStateOf(System.currentTimeMillis()) }
  var reviews by remember { mutableStateOf<List<PracticeAnswerReview>>(emptyList()) }

  LaunchedEffect(revisionIds) {
    if (revisionIds.isNotEmpty()) {
      config = PracticeSessionConfig(PracticeMode.MISTAKES, revisionIds.size)
      questions = dao.loadRevisionPracticeQuestions(revisionIds)
    }
  }

  if (config == null && revisionIds.isEmpty()) {
    PublishTeacherContext(null)
    PracticeSetup(
      initialFilters = setupPreset,
      questions = allQuestions,
      topics = topics,
      questionTopics = questionTopics,
      attempts = allAttempts,
      onStart = { chosen ->
        setupPreset = chosen.toQuestionBankFilterState()
        config = chosen
        questions = null
        scope.launch { questions = dao.loadPracticeQuestions(chosen) }
      },
      onDone = onDone,
    )
    return
  }

  val sessionQuestions = questions
  if (sessionQuestions == null) {
    PublishTeacherContext(null)
    Column(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
      Text("Practice", style = MaterialTheme.typography.headlineMedium)
      Text(if (revisionIds.isNotEmpty()) "Preparing revision practice…" else "Preparing ${config?.mode?.label?.lowercase() ?: ""} practice…")
      LinearProgressIndicator(Modifier.fillMaxWidth())
    }
    return
  }

  if (sessionQuestions.isEmpty()) {
    PublishTeacherContext(null)
    Column(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
      Text("Practice", style = MaterialTheme.typography.headlineMedium)
      Text(if (revisionIds.isNotEmpty()) "No valid revision questions are available." else config!!.emptyMessage())
      if (revisionIds.isEmpty()) {
        Button(onClick = { config = null; questions = null }) { Text("Adjust setup") }
      }
      TextButton(onClick = onDone) { Text("Back to Question Bank") }
    }
    return
  }

  if (index >= sessionQuestions.size) {
    PublishTeacherContext(null)
    val summary = reviews.summary()
    LazyColumn(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
      item { Text(summary.resultHeadline(), style = MaterialTheme.typography.headlineMedium) }
      item { Text(summary.resultDetail(), style = MaterialTheme.typography.titleLarge) }
      item { Text(practiceFeedback(summary.correct, summary.total), color = MaterialTheme.colorScheme.onSurfaceVariant) }
      val incorrect = reviews.incorrectOnly()
      if (incorrect.isNotEmpty()) {
        item { Text("Review mistakes", style = MaterialTheme.typography.titleLarge) }
        incorrect.forEach { review ->
          item(key = review.question.id) {
            Card(Modifier.fillMaxWidth()) {
              Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(review.question.questionText, style = MaterialTheme.typography.titleMedium)
                Text("Your answer: ${review.question.answerLabel(review.selectedAnswer)}")
                Text("Correct: ${review.question.answerLabel(review.question.correctAnswer)}", color = MaterialTheme.colorScheme.primary)
                review.question.explanation?.takeIf(String::isNotBlank)?.let { Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant) }
              }
            }
          }
        }
      }
      item {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          Button(
            onClick = {
              speech.stop()
              val incorrectIds = incorrect.map { it.question.id }.distinct()
              val retryConfig = (config ?: PracticeSessionConfig()).copy(
                mode = PracticeMode.MISTAKES,
                questionCount = incorrectIds.size.coerceAtLeast(1),
              )
              config = retryConfig
              questions = null
              index = 0
              selected = null
              submitted = false
              reviews = emptyList()
              sessionId = newPracticeSessionId()
              scope.launch {
                questions = dao.loadRevisionPracticeQuestions(incorrectIds)
                  .shuffled()
                  .take(retryConfig.safeQuestionCount)
              }
            },
            enabled = incorrect.isNotEmpty(),
          ) { Text("Practise these mistakes") }
          if (revisionIds.isEmpty()) {
            Button(onClick = {
              speech.stop()
              config = null
              questions = null
              index = 0
              reviews = emptyList()
              sessionId = newPracticeSessionId()
            }) { Text("New session") }
          }
        }
      }
      item { TextButton(onClick = { speech.stop(); onDone() }) { Text("Back to Question Bank") } }
    }
    return
  }

  val question = sessionQuestions[index]
  val options = question.optionList()
  val correctAnswer = question.correctAnswer.trim().uppercase()
  val progress = PracticeProgress(index + 1, sessionQuestions.size)
  val topicNamesById = remember(topics) { topics.associate { it.id to it.name } }
  PublishTeacherContext(question.asTeacherContext(options, answerRevealed = submitted))

  LazyColumn(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
    item {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
          Text(if (revisionIds.isNotEmpty()) "Revision Practice" else "Practice", style = MaterialTheme.typography.headlineMedium)
          Text(
            if (revisionIds.isNotEmpty()) "Recommended revision" else practiceSessionLabel(config!!, topicNamesById),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
        Text(progress.label)
      }
    }
    item { LinearProgressIndicator(progress = { progress.fraction }, modifier = Modifier.fillMaxWidth()) }
    item { Text("Score: ${reviews.count { it.wasCorrect }}", color = MaterialTheme.colorScheme.primary) }
    item {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(question.questionText, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
        Row {
          IconButton(onClick = { speech.speak(question.asSpeakableQuestion(options)) }) {
            Icon(Icons.Default.VolumeUp, "Read question aloud")
          }
          IconButton(onClick = speech::stop) {
            Icon(Icons.Default.Stop, "Stop reading")
          }
        }
      }
    }
    options.forEachIndexed { optionIndex, option ->
      val letter = optionLetter(optionIndex)
      item(key = "$index-$letter") {
        Card(Modifier.fillMaxWidth().clickable(enabled = !submitted) { selected = letter }) {
          Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = selected == letter, onClick = { if (!submitted) selected = letter }, enabled = !submitted)
            Text("$letter. $option", modifier = Modifier.padding(start = 8.dp))
          }
        }
      }
    }
    item {
      if (!submitted) {
        Button(onClick = {
          val answer = selected ?: return@Button
          val evaluation = question.evaluateAnswer(answer)
          val timeTaken = elapsedAnswerTime(startedAt)
          reviews = reviews + PracticeAnswerReview(question, answer, evaluation.isCorrect, timeTaken)
          submitted = true
          scope.launch {
            dao.insertAttempt(
              QuestionAttemptEntity(
                UUID.randomUUID().toString(),
                question.id,
                sessionId,
                evaluation.selected,
                evaluation.isCorrect,
                timeTaken,
              ),
            )
          }
        }, enabled = selected != null) { Text("Check Answer") }
      } else {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          val correct = selected == correctAnswer
          Text(
            if (correct) "Correct ✓" else "Incorrect. Correct answer: ${question.answerLabel(correctAnswer)}",
            color = if (correct) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleMedium,
          )
          question.explanation?.takeIf(String::isNotBlank)?.let {
            Card(Modifier.fillMaxWidth()) {
              Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                  Text("Explanation", style = MaterialTheme.typography.titleMedium)
                  IconButton(onClick = { question.asSpeakableExplanation()?.let(speech::speak) }) {
                    Icon(Icons.Default.VolumeUp, "Read explanation aloud")
                  }
                }
                Text(it)
              }
            }
          }
          Button(onClick = {
            speech.stop()
            index++
            selected = null
            submitted = false
            startedAt = System.currentTimeMillis()
          }) { Text(if (index == sessionQuestions.lastIndex) "Finish" else "Next Question") }
        }
      }
    }
    item { TextButton(onClick = { speech.stop(); onDone() }) { Text("End session") } }
  }
}

@Composable
private fun PracticeSetup(
  initialFilters: QuestionBankFilterState,
  questions: List<QuestionEntity>,
  topics: List<KnowledgeNodeEntity>,
  questionTopics: List<QuestionTopicEntity>,
  attempts: List<QuestionAttemptEntity>,
  onStart: (PracticeSessionConfig) -> Unit,
  onDone: () -> Unit,
) {
  var mode by remember { mutableStateOf(PracticeMode.RANDOM) }
  var count by remember { mutableStateOf(PracticeConfig.defaultSessionSize) }
  var query by remember(initialFilters) { mutableStateOf(initialFilters.query) }
  var source by remember(initialFilters) { mutableStateOf(initialFilters.source) }
  var difficulty by remember(initialFilters) { mutableStateOf(initialFilters.difficulty) }
  var topicId by remember(initialFilters) { mutableStateOf(initialFilters.topicId) }

  val config = PracticeSessionConfig(
    mode = mode,
    questionCount = count,
    query = query,
    source = source,
    difficulty = difficulty,
    topicId = topicId,
  )
  val matchingCount = remember(questions, questionTopics, attempts, config) {
    filterPracticeCandidates(
      candidates = questions.forPracticeMode(config.mode, attempts),
      questionTopics = questionTopics,
      config = config,
    ).size
  }
  val sourceOptions = remember(questions) { availableQuestionSources(questions) }

  LazyColumn(
    Modifier.fillMaxSize().padding(28.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item { Text("Practice", style = MaterialTheme.typography.headlineMedium) }
    item {
      Text(
        "Choose what you want to practise. Nothing is marked correct until you submit an answer.",
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
    item { Text("Mode", style = MaterialTheme.typography.titleMedium) }
    items(PracticeMode.entries, key = { it.name }) { option ->
      Card(Modifier.fillMaxWidth().clickable { mode = option }) {
        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
          RadioButton(selected = mode == option, onClick = { mode = option })
          Column(Modifier.padding(start = 8.dp)) {
            Text(option.label, style = MaterialTheme.typography.titleSmall)
            Text(option.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    }

    item {
      OutlinedTextField(
        value = query,
        onValueChange = { query = it },
        label = { Text("Search within practice questions") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
      )
    }
    item {
      QuestionFilterChoiceRow(
        title = "Source",
        choices = listOf(null to "All") + sourceOptions.map { value -> value to displayQuestionSource(value) },
        selected = source,
        onSelect = { source = it },
      )
    }
    item {
      QuestionFilterChoiceRow(
        title = "Difficulty",
        choices = listOf(null to "All") + questionDifficultyOptions.map { value ->
          value to value.lowercase().replaceFirstChar(Char::uppercase)
        },
        selected = difficulty,
        onSelect = { difficulty = it },
      )
    }
    if (topics.isNotEmpty()) {
      item {
        QuestionFilterChoiceRow(
          title = "Knowledge topic",
          choices = listOf(null to "All") + topics.map { topic -> topic.id to topic.name },
          selected = topicId,
          onSelect = { topicId = it },
        )
      }
    }
    if (config.hasFilters) {
      item {
        TextButton(onClick = {
          query = ""
          source = null
          difficulty = null
          topicId = null
        }) { Text("Clear practice filters") }
      }
    }

    item {
      Text(
        "$matchingCount practice-ready question${if (matchingCount == 1) "" else "s"} match this setup.",
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
    item { Text("Questions", style = MaterialTheme.typography.titleMedium) }
    item {
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        PracticeConfig.sessionSizes.forEach { size ->
          FilterChip(
            selected = count == size,
            onClick = { count = size },
            label = { Text(size.toString()) },
          )
        }
      }
    }
    item {
      Button(
        onClick = { onStart(config) },
        enabled = matchingCount > 0,
      ) { Text("Start practice") }
    }
    item { TextButton(onClick = onDone) { Text("Back to Question Bank") } }
  }
}

private fun practiceSessionLabel(
  config: PracticeSessionConfig,
  topicNamesById: Map<String, String>,
): String {
  val parts = mutableListOf(config.mode.label)
  config.source?.takeIf(String::isNotBlank)?.let { parts += displayQuestionSource(it) }
  config.difficulty?.takeIf(String::isNotBlank)?.let { parts += normalizeDifficulty(it) }
  config.topicId?.takeIf(String::isNotBlank)?.let { parts += topicNamesById[it] ?: "Selected topic" }
  config.query.trim().takeIf(String::isNotBlank)?.let { parts += "Search: ${it.take(24)}" }
  return parts.joinToString(" • ")
}

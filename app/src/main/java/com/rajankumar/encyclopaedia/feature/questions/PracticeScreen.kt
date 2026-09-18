package com.rajankumar.encyclopaedia.feature.questions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase
import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import java.util.UUID
import kotlinx.coroutines.launch

@Composable
fun PracticeScreen(onDone: () -> Unit) {
  val dao = EncyclopaediaDatabase.get(LocalContext.current).dao()
  val scope = rememberCoroutineScope()
  val sessionId = remember { UUID.randomUUID().toString() }
  var questions by remember { mutableStateOf<List<QuestionEntity>?>(null) }
  var index by remember { mutableStateOf(0) }
  var selected by remember { mutableStateOf<String?>(null) }
  var submitted by remember { mutableStateOf(false) }
  var startedAt by remember { mutableLongStateOf(System.currentTimeMillis()) }
  var reviews by remember { mutableStateOf<List<PracticeAnswerReview>>(emptyList()) }

  LaunchedEffect(Unit) {
    questions = dao.getRandomQuestions(PracticeConstants.maxSessionQuestions)
      .filter { it.validateForPractice().valid }
      .take(PracticeConfig.defaultSessionSize)
  }

  val sessionQuestions = questions
  if (sessionQuestions == null) {
    Column(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
      Text("Practice", style = MaterialTheme.typography.headlineMedium)
      Text("Preparing a valid practice session…")
      LinearProgressIndicator(Modifier.fillMaxWidth())
    }
    return
  }

  if (sessionQuestions.isEmpty()) {
    Column(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
      Text("Practice", style = MaterialTheme.typography.headlineMedium)
      Text("No practice-ready questions were found. Questions need printed question text, 2–6 options and a valid correct answer.")
      TextButton(onClick = onDone) { Text("Back to Question Bank") }
    }
    return
  }

  if (index >= sessionQuestions.size) {
    val summary = reviews.summary()
    LazyColumn(
      Modifier.fillMaxSize().padding(28.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item { Text("Session complete", style = MaterialTheme.typography.headlineMedium) }
      item { Text("${summary.correct} / ${summary.total} correct", style = MaterialTheme.typography.headlineLarge) }
      item { Text("${summary.accuracyPercent}% accuracy • ${formatPracticeDuration(summary.totalTimeMs)} total") }
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
                review.question.explanation?.takeIf(String::isNotBlank)?.let {
                  Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
              }
            }
          }
        }
      }
      item { Button(onClick = onDone) { Text("Back to Question Bank") } }
    }
    return
  }

  val question = sessionQuestions[index]
  val options = question.optionList()
  val correctAnswer = question.correctAnswer.trim().uppercase()
  val progress = PracticeProgress(index + 1, sessionQuestions.size)

  LazyColumn(
    Modifier.fillMaxSize().padding(28.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Practice", style = MaterialTheme.typography.headlineMedium)
        Text(progress.label)
      }
    }
    item { LinearProgressIndicator(progress = { progress.fraction }, modifier = Modifier.fillMaxWidth()) }
    item { Text("Score: ${reviews.count { it.wasCorrect }}", color = MaterialTheme.colorScheme.primary) }
    item { Text(question.questionText, style = MaterialTheme.typography.titleLarge) }

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
        Button(
          onClick = {
            val answer = selected ?: return@Button
            val evaluation = question.evaluateAnswer(answer)
            val timeTaken = (System.currentTimeMillis() - startedAt).coerceAtLeast(0)
            reviews = reviews + PracticeAnswerReview(question, answer, evaluation.isCorrect, timeTaken)
            submitted = true
            scope.launch {
              dao.insertAttempt(
                QuestionAttemptEntity(
                  id = UUID.randomUUID().toString(),
                  questionId = question.id,
                  sessionId = sessionId,
                  selectedAnswer = evaluation.selected,
                  isCorrect = evaluation.isCorrect,
                  timeTakenMs = timeTaken
                )
              )
            }
          },
          enabled = selected != null
        ) { Text("Check Answer") }
      } else {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          val correct = selected == correctAnswer
          Text(
            if (correct) "Correct ✓" else "Incorrect. Correct answer: ${question.answerLabel(correctAnswer)}",
            color = if (correct) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleMedium
          )
          question.explanation?.takeIf(String::isNotBlank)?.let {
            Card(Modifier.fillMaxWidth()) {
              Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Explanation", style = MaterialTheme.typography.titleMedium)
                Text(it)
              }
            }
          }
          Button(onClick = {
            index++
            selected = null
            submitted = false
            startedAt = System.currentTimeMillis()
          }) { Text(if (index == sessionQuestions.lastIndex) "Finish" else "Next Question") }
        }
      }
    }

    item { TextButton(onClick = onDone) { Text("End session") } }
  }
}

package com.rajankumar.encyclopaedia.feature.questions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
  var questions by remember { mutableStateOf<List<QuestionEntity>>(emptyList()) }
  var index by remember { mutableStateOf(0) }
  var selected by remember { mutableStateOf<String?>(null) }
  var submitted by remember { mutableStateOf(false) }
  var correctCount by remember { mutableStateOf(0) }
  var startedAt by remember { mutableLongStateOf(System.currentTimeMillis()) }

  LaunchedEffect(Unit) { questions = dao.getRandomQuestions(20) }

  if (questions.isEmpty()) {
    Column(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
      Text("Practice", style = MaterialTheme.typography.headlineMedium)
      Text("There are no questions to practise yet. Add or scan questions first.")
      TextButton(onClick = onDone) { Text("Back to Question Bank") }
    }
    return
  }

  if (index >= questions.size) {
    Column(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
      Text("Session complete", style = MaterialTheme.typography.headlineMedium)
      Text("$correctCount / ${questions.size} correct", style = MaterialTheme.typography.headlineLarge)
      Text("${((correctCount.toFloat() / questions.size) * 100).toInt()}% accuracy")
      Button(onClick = onDone) { Text("Back to Question Bank") }
    }
    return
  }

  val question = questions[index]
  val options = question.options.lines().map { it.trim() }.filter { it.isNotBlank() }
  val correctAnswer = question.correctAnswer.trim().uppercase()

  Column(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Text("Practice", style = MaterialTheme.typography.headlineMedium)
      Text("${index + 1} / ${questions.size}")
    }

    Text("Score: $correctCount", color = MaterialTheme.colorScheme.primary)
    Text(question.questionText, style = MaterialTheme.typography.titleLarge)

    options.forEachIndexed { optionIndex, option ->
      val letter = ('A'.code + optionIndex).toChar().toString()
      Card(
        Modifier.fillMaxWidth().clickable(enabled = !submitted) { selected = letter }
      ) {
        Row(
          Modifier.fillMaxWidth().padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          RadioButton(selected = selected == letter, onClick = { if (!submitted) selected = letter }, enabled = !submitted)
          Text("$letter. $option", modifier = Modifier.padding(start = 8.dp))
        }
      }
    }

    if (!submitted) {
      Button(
        onClick = {
          val answer = selected ?: return@Button
          val correct = answer == correctAnswer
          if (correct) correctCount++
          submitted = true
          scope.launch {
            dao.insertAttempt(
              QuestionAttemptEntity(
                id = UUID.randomUUID().toString(),
                questionId = question.id,
                sessionId = sessionId,
                selectedAnswer = answer,
                isCorrect = correct,
                timeTakenMs = (System.currentTimeMillis() - startedAt).coerceAtLeast(0)
              )
            )
          }
        },
        enabled = selected != null
      ) { Text("Check Answer") }
    } else {
      val correct = selected == correctAnswer
      Text(
        if (correct) "Correct ✓" else "Incorrect. Correct answer: $correctAnswer",
        color = if (correct) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.titleMedium
      )
      question.explanation?.takeIf { it.isNotBlank() }?.let {
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
      }) { Text(if (index == questions.lastIndex) "Finish" else "Next Question") }
    }

    TextButton(onClick = onDone) { Text("End session") }
  }
}

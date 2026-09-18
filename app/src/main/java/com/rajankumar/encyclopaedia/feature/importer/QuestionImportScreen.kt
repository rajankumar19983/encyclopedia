package com.rajankumar.encyclopaedia.feature.importer

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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

private data class ReviewDraft(
  val questionText: String,
  val options: List<String>,
  val answer: String,
  val warnings: List<String>,
  val source: String
)

@Composable
fun QuestionImportScreen(onDone: () -> Unit) {
  val context = LocalContext.current
  val dao = EncyclopaediaDatabase.get(context).dao()
  val scope = rememberCoroutineScope()
  val imageEngine = remember { MlKitOcrEngine() }
  val pdfEngine = remember { PdfOcrEngine() }
  var drafts by remember { mutableStateOf<List<ReviewDraft>>(emptyList()) }
  var rawText by remember { mutableStateOf("") }
  var status by remember { mutableStateOf("Choose an image or PDF containing printed MCQs.") }
  var busy by remember { mutableStateOf(false) }

  fun review(text: String, source: String) {
    rawText = text
    drafts = OcrQuestionParser.parse(text).map {
      ReviewDraft(it.questionText, it.options, it.correctAnswer.orEmpty(), it.warnings, source)
    }
    status = if (drafts.isEmpty()) {
      "No reliably structured numbered MCQs were found. Nothing has been saved."
    } else {
      "${drafts.size} draft questions found. Review every item before saving."
    }
  }

  val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
    if (uri != null) {
      busy = true
      status = "Reading printed text from image…"
      scope.launch {
        runCatching { imageEngine.recognize(context, uri) }
          .onSuccess { review(it, "SCAN") }
          .onFailure { status = "Image OCR failed: ${it.message ?: "unknown error"}" }
        busy = false
      }
    }
  }

  val pdfPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
    if (uri != null) {
      busy = true
      status = "Opening PDF…"
      scope.launch {
        runCatching {
          pdfEngine.recognize(context, uri) { page, total ->
            status = "Reading PDF page $page of $total…"
          }
        }.onSuccess { review(it, "PDF") }
          .onFailure { status = "PDF OCR failed: ${it.message ?: "unknown error"}" }
        busy = false
      }
    }
  }

  Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    Text("Import Questions", style = MaterialTheme.typography.headlineMedium)
    Text(
      "Printed text only. Images rendered from PDFs are temporary and OCR drafts are never saved automatically.",
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      Button(onClick = { imagePicker.launch("image/*") }, enabled = !busy) { Text("Choose Image") }
      Button(onClick = { pdfPicker.launch("application/pdf") }, enabled = !busy) { Text("Choose PDF") }
      TextButton(onClick = onDone) { Text("Back") }
    }
    Text(status)

    if (drafts.isEmpty() && rawText.isNotBlank()) {
      Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
          Text("OCR text for diagnosis", style = MaterialTheme.typography.titleMedium)
          Text(rawText.take(2500), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      itemsIndexed(drafts) { index, initial ->
        var question by remember(initial) { mutableStateOf(initial.questionText) }
        var optionsText by remember(initial) { mutableStateOf(initial.options.joinToString("\n")) }
        var answer by remember(initial) { mutableStateOf(initial.answer) }
        var saved by remember(initial) { mutableStateOf(false) }
        val options = optionsText.lines().map { it.trim() }.filter { it.isNotBlank() }.take(6)
        val validAnswer = answer.uppercase().singleOrNull()?.let {
          options.isNotEmpty() && it in 'A'..('A'.code + options.lastIndex).toChar()
        } == true
        val valid = question.isNotBlank() && options.size in 2..6 && validAnswer

        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Draft ${index + 1} • ${initial.source}", style = MaterialTheme.typography.titleMedium)
            initial.warnings.forEach { Text("⚠ $it", color = MaterialTheme.colorScheme.error) }
            OutlinedTextField(question, { question = it }, label = { Text("Question") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
              optionsText,
              { optionsText = it },
              label = { Text("Options — one per line (2–6)") },
              modifier = Modifier.fillMaxWidth(),
              minLines = 2
            )
            OutlinedTextField(answer, { answer = it.take(1).uppercase() }, label = { Text("Correct option A–F") })
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Button(
                enabled = valid && !saved,
                onClick = {
                  scope.launch {
                    dao.saveQuestion(
                      QuestionEntity(
                        id = UUID.randomUUID().toString(),
                        questionText = question.trim(),
                        options = options.joinToString("\n"),
                        correctAnswer = answer.uppercase(),
                        source = initial.source,
                        difficulty = "UNRATED"
                      ),
                      null
                    )
                    saved = true
                  }
                }
              ) { Text(if (saved) "Saved" else "Approve & Save") }
              TextButton(onClick = { drafts = drafts.filterIndexed { i, _ -> i != index } }) { Text("Reject") }
            }
            if (!valid && !saved) {
              Text(
                "Review required: question, 2–6 options and a valid correct option are mandatory.",
                style = MaterialTheme.typography.bodySmall
              )
            }
          }
        }
      }
    }
  }
}

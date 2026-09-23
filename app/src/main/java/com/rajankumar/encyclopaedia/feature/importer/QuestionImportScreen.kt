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

private data class ReviewDraft(val parsed: ParsedQuestionDraft, val source: String, val metadata: OcrSourceMetadata)

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
    val metadata = OcrSourceMetadataExtractor.extract(text)
    val parsed = OcrQuestionParser.parse(OcrLanguageFilter.removeDevanagariLines(text)).map { it.withQualityWarnings() }
    drafts = parsed.map { ReviewDraft(it, source, metadata) }
    status = importReviewStatus(parsed.reviewSummary())
  }

  val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
    if (uri != null) {
      busy = true
      status = "Reading printed text from image…"
      scope.launch {
        runCatching { imageEngine.recognize(context, uri) }.onSuccess { review(it, "SCAN") }
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
        runCatching { pdfEngine.recognize(context, uri) { page, total -> status = "Reading PDF page $page of $total…" } }
          .onSuccess { review(it, "PDF") }.onFailure { status = "PDF OCR failed: ${it.message ?: "unknown error"}" }
        busy = false
      }
    }
  }

  Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    Text("Import Questions", style = MaterialTheme.typography.headlineMedium)
    Text("Printed English text only. Source images are not stored and OCR drafts are never saved automatically.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      Button(onClick = { imagePicker.launch("image/*") }, enabled = !busy) { Text("Choose Image") }
      Button(onClick = { pdfPicker.launch("application/pdf") }, enabled = !busy) { Text("Choose PDF") }
      TextButton(onClick = onDone) { Text("Back") }
    }
    Text(status)
    if (drafts.isEmpty() && rawText.isNotBlank()) Card(Modifier.fillMaxWidth()) {
      Column(Modifier.padding(16.dp)) {
        Text("OCR text for diagnosis", style = MaterialTheme.typography.titleMedium)
        Text(OcrLanguageFilter.removeDevanagariLines(rawText).take(2500), color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      itemsIndexed(drafts) { index, initial ->
        var question by remember(initial) { mutableStateOf(initial.parsed.questionText) }
        var optionsText by remember(initial) { mutableStateOf(initial.parsed.options.joinToString("\n")) }
        var answer by remember(initial) { mutableStateOf(initial.parsed.correctAnswer.orEmpty()) }
        var saveState by remember(initial) { mutableStateOf("READY") }
        val editable = EditableImportDraft(question, optionsText.lines(), answer)
        val validation = editable.validateForSave()
        val options = editable.cleanedOptions
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Draft ${index + 1} • ${importSourceLabel(initial.source, initial.metadata)}", style = MaterialTheme.typography.titleMedium)
            Text(sourceMetadataSummary(initial.metadata), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            validation.issues.forEach { Text("⚠ $it", color = MaterialTheme.colorScheme.error) }
            OutlinedTextField(question, { question = it }, label = { Text("Question") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(optionsText, { optionsText = it }, label = { Text("Options — one per line (2 or more)") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
            editable.optionPreview().forEach { option -> Text("${option.label}. ${option.text}", style = MaterialTheme.typography.bodySmall) }
            OutlinedTextField(answer, { answer = it.take(2).uppercase() }, label = { Text("Correct option (A, B… or 1, 2…)") })
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Button(enabled = validation.canSave && saveState == "READY", onClick = {
                saveState = "SAVING"
                scope.launch {
                  val inserted = dao.saveImportedQuestionIfUnique(QuestionEntity(
                    id = UUID.randomUUID().toString(), questionText = question.trim(), options = options.joinToString("\n"),
                    correctAnswer = validation.normalizedAnswer.orEmpty(), explanation = initial.parsed.explanation,
                    source = initial.source, difficulty = "UNRATED"
                  ), null)
                  saveState = if (inserted) "SAVED" else "DUPLICATE"
                }
              }) { Text(when (saveState) { "SAVING" -> "Checking…"; "SAVED" -> "Saved"; "DUPLICATE" -> "Already exists"; else -> "Approve & Save" }) }
              TextButton(onClick = { drafts = drafts.filterIndexed { i, _ -> i != index } }) { Text("Reject") }
            }
            when {
              saveState == "DUPLICATE" -> Text("This question and its options already exist in the Question Bank, so another copy was not created.", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
              !validation.canSave && saveState != "SAVED" -> Text("Resolve every review warning before approving this question.", style = MaterialTheme.typography.bodySmall)
            }
          }
        }
      }
    }
  }
}

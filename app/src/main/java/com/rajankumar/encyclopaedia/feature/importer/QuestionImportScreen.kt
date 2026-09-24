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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
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
  val parsed: ParsedQuestionDraft,
  val source: String,
  val metadata: OcrSourceMetadata,
  val review: OcrDraftReviewState = OcrDraftReviewState(),
)

@Composable
fun QuestionImportScreen(onDone: () -> Unit) {
  val context = LocalContext.current
  val dao = EncyclopaediaDatabase.get(context).dao()
  val scope = rememberCoroutineScope()
  val documentImporter = remember(context.contentResolver) { OcrDocumentImporter(context.contentResolver) }
  val checklistItems = remember { ocrReviewChecklist() }
  var drafts by remember { mutableStateOf<List<ReviewDraft>>(emptyList()) }
  var rawText by remember { mutableStateOf("") }
  var preparation by remember { mutableStateOf<OcrImportPreparation?>(null) }
  var extractionNotices by remember { mutableStateOf<List<String>>(emptyList()) }
  var status by remember { mutableStateOf("Choose one or more images, or a PDF containing printed MCQs.") }
  var busy by remember { mutableStateOf(false) }
  var filter by remember { mutableStateOf(OcrReviewFilter.ALL) }

  fun review(document: OcrDocumentImport, source: String) {
    val text = document.extraction.combinedText
    rawText = text
    extractionNotices = document.allNotices()
    val prepared = prepareOcrImport(text)
    preparation = prepared
    drafts = prepared.drafts.map { ReviewDraft(it, source, prepared.metadata) }
    status = when {
      document.extraction.failedPages.isNotEmpty() -> "OCR completed with ${document.extraction.failedPages.size} source failure(s). Review warnings below."
      prepared.drafts.isEmpty() -> "OCR completed, but no reviewable MCQs were parsed."
      else -> prepared.report().message()
    }
  }

  val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
    if (uris.isNotEmpty()) {
      busy = true
      status = "Reading ${uris.size} image${if (uris.size == 1) "" else "s"}…"
      scope.launch {
        runCatching {
          documentImporter.importImages(uris) { progress ->
            status = "Reading image ${progress.completed} of ${progress.total}…"
          }
        }.onSuccess { review(it, "SCAN") }
          .onFailure { status = "Image OCR failed: ${it.message ?: "unknown error"}" }
        busy = false
      }
    }
  }
  val pdfPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
    if (uri != null) {
      busy = true
      status = "Reading PDF pages…"
      scope.launch {
        runCatching { documentImporter.importPdf(uri) }
          .onSuccess { review(it, "PDF") }
          .onFailure { status = "PDF OCR failed: ${it.message ?: "unknown error"}" }
        busy = false
      }
    }
  }

  val decisions = drafts.map { it.review.decision }
  val summary = buildOcrReviewUiSummary(decisions)
  Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    Text("Import Questions", style = MaterialTheme.typography.headlineMedium)
    Text("Printed English text only. Select multiple images when a question set spans several scans. Source images are not stored and OCR drafts are never saved automatically.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      Button(onClick = { imagePicker.launch("image/*") }, enabled = !busy) { Text("Choose Images") }
      Button(onClick = { pdfPicker.launch("application/pdf") }, enabled = !busy) { Text("Choose PDF") }
      TextButton(onClick = onDone) { Text("Back") }
    }
    Text(status)
    extractionNotices.forEach { Text("⚠ $it", color = MaterialTheme.colorScheme.error) }
    preparation?.let { prepared ->
      prepared.sanitized.summary().message()?.let { Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant) }
      prepared.diagnostics.message()?.let { Text("⚠ $it", color = MaterialTheme.colorScheme.error) }
      prepared.sanitized.removedLinePreviews().forEach { removed -> Text("Excluded: ${removed.text} — ${removed.reason}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
    if (drafts.isNotEmpty()) {
      Text(summary.headline, style = MaterialTheme.typography.titleMedium)
      Text(summary.detail, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        OcrReviewFilter.entries.forEach { choice ->
          FilterChip(selected = filter == choice, onClick = { filter = choice }, label = { Text("${choice.name.lowercase().replaceFirstChar { it.uppercase() }} (${choice.count(decisions)})") })
        }
      }
    }
    if (drafts.isEmpty() && rawText.isNotBlank()) Card(Modifier.fillMaxWidth()) {
      Column(Modifier.padding(16.dp)) {
        Text("OCR text for diagnosis", style = MaterialTheme.typography.titleMedium)
        Text(preparation?.sanitized?.text?.take(2500).orEmpty(), color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      itemsIndexed(drafts) { index, initial ->
        if (!filter.matches(initial.review.decision)) return@itemsIndexed
        var edit by remember(initial.parsed) { mutableStateOf(initial.parsed.toEditState()) }
        val validation = edit.validation()
        val gate = evaluateOcrApproval(validation, initial.review.checklist, checklistItems)
        val options = edit.editable().cleanedOptions
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Draft ${index + 1} • ${importSourceLabel(initial.source, initial.metadata)}", style = MaterialTheme.typography.titleMedium)
            Text(initial.review.decision.accessibilityLabel(index + 1), style = MaterialTheme.typography.bodySmall)
            Text(sourceMetadataSummary(initial.metadata), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            validation.issues.forEach { Text("⚠ $it", color = MaterialTheme.colorScheme.error) }
            OutlinedTextField(edit.question, { edit = edit.copy(question = it) }, label = { Text("Question") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(edit.optionsText, { edit = edit.copy(optionsText = it) }, label = { Text("Options — one per line (2 or more)") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
            edit.editable().optionPreview().forEach { option -> Text("${option.label}. ${option.text}", style = MaterialTheme.typography.bodySmall) }
            OutlinedTextField(edit.answer, { edit = edit.copy(answer = it.take(2).uppercase()) }, label = { Text("Correct option (A, B… or 1, 2…)") })
            Text("Manual verification", style = MaterialTheme.typography.titleSmall)
            checklistItems.forEachIndexed { checkIndex, item ->
              Row {
                Checkbox(
                  checked = checkIndex in initial.review.checklist.checked,
                  enabled = initial.review.decision == OcrReviewDecision.PENDING,
                  onCheckedChange = {
                    drafts = drafts.mapIndexed { i, draft -> if (i == index) draft.copy(review = draft.review.copy(checklist = draft.review.checklist.toggle(checkIndex))) else draft }
                  },
                )
                Text(item.label + if (item.required) " *" else "", modifier = Modifier.padding(top = 12.dp))
              }
            }
            val checklistProgress = initial.review.checklist.progress(checklistItems)
            Text("Required checks: ${checklistProgress.completed}/${checklistProgress.required}", style = MaterialTheme.typography.bodySmall)
            gate.reasons.forEach { Text("⚠ $it", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Button(enabled = gate.allowed && initial.review.saveStatus != OcrDraftSaveStatus.SAVING && initial.review.decision == OcrReviewDecision.PENDING, onClick = {
                drafts = drafts.mapIndexed { i, draft -> if (i == index) draft.copy(review = draft.review.copy(saveStatus = OcrDraftSaveStatus.SAVING)) else draft }
                scope.launch {
                  runCatching {
                    dao.saveImportedQuestionIfUnique(QuestionEntity(
                      id = UUID.randomUUID().toString(), questionText = edit.question.trim(), options = options.joinToString("\n"),
                      correctAnswer = validation.normalizedAnswer.orEmpty(), explanation = initial.parsed.explanation,
                      source = initial.source, difficulty = "UNRATED"
                    ), null)
                  }.onSuccess { inserted ->
                    drafts = drafts.mapIndexed { i, draft -> if (i == index) draft.copy(review = draft.review.copy(
                      decision = if (inserted) OcrReviewDecision.APPROVED else draft.review.decision,
                      saveStatus = if (inserted) OcrDraftSaveStatus.SAVED else OcrDraftSaveStatus.DUPLICATE,
                    )) else draft }
                  }.onFailure {
                    drafts = drafts.mapIndexed { i, draft -> if (i == index) draft.copy(review = draft.review.copy(saveStatus = OcrDraftSaveStatus.FAILED)) else draft }
                  }
                }
              }) { Text(initial.review.saveStatus.label()) }
              TextButton(enabled = initial.review.decision == OcrReviewDecision.PENDING, onClick = {
                drafts = drafts.mapIndexed { i, draft -> if (i == index) draft.copy(review = draft.review.copy(decision = OcrReviewDecision.REJECTED)) else draft }
              }) { Text(if (initial.review.decision == OcrReviewDecision.REJECTED) "Rejected" else "Reject") }
            }
            when {
              initial.review.decision == OcrReviewDecision.APPROVED -> Text("Approved and saved to the Question Bank.", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
              initial.review.decision == OcrReviewDecision.REJECTED -> Text("Rejected. This OCR draft was not saved.", style = MaterialTheme.typography.bodySmall)
              initial.review.saveStatus == OcrDraftSaveStatus.DUPLICATE -> Text("This question and its options already exist, so another copy was not created.", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
              initial.review.saveStatus == OcrDraftSaveStatus.FAILED -> Text("Saving failed. Your reviewed draft remains here so you can retry.", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
          }
        }
      }
    }
  }
}

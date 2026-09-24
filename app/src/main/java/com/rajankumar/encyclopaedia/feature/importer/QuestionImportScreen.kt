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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun QuestionImportScreen(onDone: () -> Unit) {
  val viewModel: ImportReviewViewModel = viewModel()
  val state = viewModel.uiState
  val checklistItems = remember { ocrReviewChecklist() }

  val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
    viewModel.importImages(uris)
  }
  val pdfPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
    if (uri != null) viewModel.importPdf(uri)
  }

  val decisions = state.drafts.map { it.review.decision }
  val summary = buildOcrReviewUiSummary(decisions)
  Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    Text("Import Questions", style = MaterialTheme.typography.headlineMedium)
    Text("Printed English text only. Select multiple images when a question set spans several scans. Source images are not stored and OCR drafts are never saved automatically.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      Button(onClick = { imagePicker.launch("image/*") }, enabled = !state.busy) { Text("Choose Images") }
      Button(onClick = { pdfPicker.launch("application/pdf") }, enabled = !state.busy) { Text("Choose PDF") }
      TextButton(onClick = onDone) { Text("Back") }
    }
    Text(state.status)
    state.extractionNotices.forEach { Text("⚠ $it", color = MaterialTheme.colorScheme.error) }
    state.preparation?.let { prepared ->
      prepared.sanitized.summary().message()?.let { Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant) }
      prepared.diagnostics.message()?.let { Text("⚠ $it", color = MaterialTheme.colorScheme.error) }
      prepared.sanitized.removedLinePreviews().forEach { removed -> Text("Excluded: ${removed.text} — ${removed.reason}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
    if (state.drafts.isNotEmpty()) {
      Text(summary.headline, style = MaterialTheme.typography.titleMedium)
      Text(summary.detail, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        OcrReviewFilter.entries.forEach { choice ->
          FilterChip(selected = state.filter == choice, onClick = { viewModel.setFilter(choice) }, label = { Text("${choice.name.lowercase().replaceFirstChar { it.uppercase() }} (${choice.count(decisions)})") })
        }
      }
    }
    if (state.drafts.isEmpty() && state.rawText.isNotBlank()) Card(Modifier.fillMaxWidth()) {
      Column(Modifier.padding(16.dp)) {
        Text("OCR text for diagnosis", style = MaterialTheme.typography.titleMedium)
        Text(state.preparation?.sanitized?.text?.take(2500).orEmpty(), color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      itemsIndexed(state.drafts, key = { index, _ -> index }) { index, initial ->
        if (!state.filter.matches(initial.review.decision)) return@itemsIndexed
        val edit = initial.edit
        val validation = edit.validation()
        val gate = evaluateOcrApproval(validation, initial.review.checklist, checklistItems)
        Card(Modifier.fillMaxWidth()) {
          Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Draft ${index + 1} • ${importSourceLabel(initial.source, initial.metadata)}", style = MaterialTheme.typography.titleMedium)
            Text(initial.review.decision.accessibilityLabel(index + 1), style = MaterialTheme.typography.bodySmall)
            Text(sourceMetadataSummary(initial.metadata), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            validation.issues.forEach { Text("⚠ $it", color = MaterialTheme.colorScheme.error) }
            OutlinedTextField(edit.question, { value ->
              viewModel.updateQuestion(index, value)
            }, label = { Text("Question") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(edit.optionsText, { value ->
              viewModel.updateOptions(index, value)
            }, label = { Text("Options — one per line (2 or more)") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
            edit.editable().optionPreview().forEach { option -> Text("${option.label}. ${option.text}", style = MaterialTheme.typography.bodySmall) }
            OutlinedTextField(edit.answer, { value ->
              viewModel.updateAnswer(index, value)
            }, label = { Text("Correct option (A, B… or 1, 2…)") })
            Text("Manual verification", style = MaterialTheme.typography.titleSmall)
            checklistItems.forEachIndexed { checkIndex, item ->
              Row {
                Checkbox(
                  checked = checkIndex in initial.review.checklist.checked,
                  enabled = initial.review.decision == OcrReviewDecision.PENDING,
                  onCheckedChange = {
                    viewModel.toggleChecklistItem(index, checkIndex)
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
                viewModel.save(index)
              }) { Text(initial.review.saveStatus.label()) }
              TextButton(enabled = initial.review.decision == OcrReviewDecision.PENDING, onClick = {
                viewModel.reject(index)
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

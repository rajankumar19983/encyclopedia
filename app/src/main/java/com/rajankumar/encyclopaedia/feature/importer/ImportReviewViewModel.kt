package com.rajankumar.encyclopaedia.feature.importer

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import java.util.UUID
import kotlinx.coroutines.launch

internal data class ReviewDraft(
  val parsed: ParsedQuestionDraft,
  val source: String,
  val metadata: OcrSourceMetadata,
  val review: OcrDraftReviewState = OcrDraftReviewState(),
  val edit: OcrDraftEditState = parsed.toEditState(),
  val editableSource: String = importSourceLabel(source, metadata),
)

internal fun List<ReviewDraft>.updateDraft(
  index: Int,
  transform: (ReviewDraft) -> ReviewDraft,
): List<ReviewDraft> = mapIndexed { draftIndex, draft ->
  if (draftIndex == index) transform(draft) else draft
}

internal fun ReviewDraft.toQuestionEntity(id: String): QuestionEntity {
  val validation = edit.validation()
  return QuestionEntity(
    id = id,
    questionText = edit.question.trim(),
    options = edit.editable().cleanedOptions.joinToString("\n"),
    correctAnswer = validation.normalizedAnswer.orEmpty(),
    explanation = parsed.explanation,
    source = editableSource.trim().ifBlank { source },
    difficulty = "UNRATED",
  )
}

internal data class ImportReviewUiState(
  val drafts: List<ReviewDraft> = emptyList(),
  val rawText: String = "",
  val preparation: OcrImportPreparation? = null,
  val extractionNotices: List<String> = emptyList(),
  val status: String = "Choose one or more images, or a PDF containing printed MCQs.",
  val busy: Boolean = false,
  val filter: OcrReviewFilter = OcrReviewFilter.ALL,
)

class ImportReviewViewModel(application: Application) : AndroidViewModel(application) {
  private val dao = EncyclopaediaDatabase.get(application).dao()
  private val documentImporter = OcrDocumentImporter(application.contentResolver)

  internal var uiState by mutableStateOf(ImportReviewUiState())
    private set

  fun importImages(uris: List<Uri>) {
    if (uris.isEmpty() || uiState.busy) return
    uiState = uiState.copy(
      busy = true,
      status = "Reading ${uris.size} image${if (uris.size == 1) "" else "s"}…",
    )
    viewModelScope.launch {
      runCatching {
        documentImporter.importImages(uris) { progress ->
          uiState = uiState.copy(status = "Reading image ${progress.completed} of ${progress.total}…")
        }
      }.onSuccess { review(it, "SCAN") }
        .onFailure { uiState = uiState.copy(status = "Image OCR failed: ${it.message ?: "unknown error"}") }
      uiState = uiState.copy(busy = false)
    }
  }

  fun importPdf(uri: Uri) {
    if (uiState.busy) return
    uiState = uiState.copy(busy = true, status = "Reading PDF pages…")
    viewModelScope.launch {
      runCatching { documentImporter.importPdf(uri) }
        .onSuccess { review(it, "PDF") }
        .onFailure { uiState = uiState.copy(status = "PDF OCR failed: ${it.message ?: "unknown error"}") }
      uiState = uiState.copy(busy = false)
    }
  }

  fun setFilter(filter: OcrReviewFilter) {
    uiState = uiState.copy(filter = filter)
  }

  fun updateQuestion(index: Int, value: String) = updateDraft(index) {
    it.copy(edit = it.edit.copy(question = value))
  }

  fun updateOptions(index: Int, value: String) = updateDraft(index) {
    it.copy(edit = it.edit.copy(optionsText = value))
  }

  fun updateAnswer(index: Int, value: String) = updateDraft(index) {
    it.copy(edit = it.edit.copy(answer = value.take(2).uppercase()))
  }

  fun updateSource(index: Int, value: String) = updateDraft(index) {
    it.copy(editableSource = value)
  }

  fun toggleChecklistItem(index: Int, checkIndex: Int) = updateDraft(index) {
    it.copy(review = it.review.copy(checklist = it.review.checklist.toggle(checkIndex)))
  }

  fun reject(index: Int) = updateDraft(index) {
    it.copy(review = it.review.copy(decision = OcrReviewDecision.REJECTED))
  }

  fun save(index: Int) {
    val draft = uiState.drafts.getOrNull(index) ?: return
    if (draft.review.decision != OcrReviewDecision.PENDING || draft.review.saveStatus == OcrDraftSaveStatus.SAVING) return
    val validation = draft.edit.validation()
    val gate = evaluateOcrApproval(validation, draft.review.checklist, ocrReviewChecklist())
    if (!gate.allowed) return
    updateDraft(index) { it.copy(review = it.review.copy(saveStatus = OcrDraftSaveStatus.SAVING)) }
    viewModelScope.launch {
      runCatching {
        dao.saveImportedQuestionIfUnique(
          draft.toQuestionEntity(UUID.randomUUID().toString()),
          null,
        )
      }.onSuccess { inserted ->
        updateDraft(index) { current ->
          current.copy(review = current.review.copy(
            decision = if (inserted) OcrReviewDecision.APPROVED else current.review.decision,
            saveStatus = if (inserted) OcrDraftSaveStatus.SAVED else OcrDraftSaveStatus.DUPLICATE,
          ))
        }
      }.onFailure {
        updateDraft(index) { current ->
          current.copy(review = current.review.copy(saveStatus = OcrDraftSaveStatus.FAILED))
        }
      }
    }
  }

  private fun review(document: OcrDocumentImport, source: String) {
    val text = document.extraction.combinedText
    val prepared = prepareOcrImport(text)
    val status = when {
      document.extraction.failedPages.isNotEmpty() -> "OCR completed with ${document.extraction.failedPages.size} source failure(s). Review warnings below."
      prepared.drafts.isEmpty() -> "OCR completed, but no reviewable MCQs were parsed."
      else -> prepared.report().message()
    }
    uiState = uiState.copy(
      drafts = prepared.drafts.map { ReviewDraft(it, source, prepared.metadata) },
      rawText = text,
      preparation = prepared,
      extractionNotices = document.allNotices(),
      status = status,
    )
  }

  private fun updateDraft(index: Int, transform: (ReviewDraft) -> ReviewDraft) {
    uiState = uiState.copy(drafts = uiState.drafts.updateDraft(index, transform))
  }
}

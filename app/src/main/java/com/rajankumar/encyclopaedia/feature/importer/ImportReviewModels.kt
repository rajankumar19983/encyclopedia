package com.rajankumar.encyclopaedia.feature.importer

enum class ImportReadiness { READY_FOR_REVIEW, NEEDS_ATTENTION }

data class ImportOptionPreview(val label: String, val text: String)

data class ImportReviewItem(
  val id: String,
  val draft: EditableImportDraft,
  val reviewed: Boolean = false,
)

data class ImportReviewQueue(val items: List<ImportReviewItem>) {
  val remaining: Int get() = items.count { !it.reviewed }
  val complete: Boolean get() = items.isNotEmpty() && remaining == 0

  fun markReviewed(id: String): ImportReviewQueue = copy(
    items = items.map { if (it.id == id) it.copy(reviewed = true) else it },
  )
}

data class ImportReviewProgress(
  val total: Int,
  val saved: Int,
  val rejected: Int,
  val remaining: Int,
)

data class ImportApprovalResult(
  val approved: List<EditableImportDraft>,
  val rejectedIds: List<String>,
) {
  val canPersist: Boolean get() = approved.isNotEmpty() && rejectedIds.isEmpty()
}

fun importedAnswerIndex(value: String, optionCount: Int): Int? {
  val clean = value.trim().uppercase()
  val index = clean.toIntOrNull()?.minus(1)
    ?: clean.singleOrNull()?.takeIf { it in 'A'..'Z' }?.let { it.code - 'A'.code }
  return index?.takeIf { it in 0 until optionCount }
}

fun normalizedImportedAnswer(value: String, optionCount: Int): String? =
  importedAnswerIndex(value, optionCount)?.let { ('A'.code + it).toChar().toString() }

fun importDraftId(question: String, index: Int): String {
  val stem = question.trim().lowercase()
    .replace(Regex("[^a-z0-9]+"), "-")
    .trim('-')
    .take(32)
    .ifBlank { "question" }
  return "$stem-${index + 1}"
}

fun EditableImportDraft.optionPreview(): List<ImportOptionPreview> = cleanedOptions.mapIndexed { index, text ->
  ImportOptionPreview(ocrOptionLabel(index), text)
}

fun ParsedQuestionDraft.importReadiness(): ImportReadiness =
  if (importPolicy().canConfirm) ImportReadiness.READY_FOR_REVIEW else ImportReadiness.NEEDS_ATTENTION

fun List<EditableImportDraft>.toReviewItems(): List<ImportReviewItem> =
  mapIndexed { index, draft -> ImportReviewItem(importDraftId(draft.question, index), draft) }

fun importReviewProgress(total: Int, saved: Int, rejected: Int): ImportReviewProgress {
  val safeTotal = total.coerceAtLeast(0)
  val safeSaved = saved.coerceIn(0, safeTotal)
  val safeRejected = rejected.coerceIn(0, safeTotal - safeSaved)
  return ImportReviewProgress(safeTotal, safeSaved, safeRejected, safeTotal - safeSaved - safeRejected)
}

fun ImportReviewQueue.reviewIfValid(id: String): ImportReviewQueue = copy(
  items = items.map { item ->
    if (item.id != id) item
    else item.copy(reviewed = item.draft.validateForSave().canSave && item.draft.hasOnlyEnglishOcrContent())
  },
)

fun ImportReviewQueue.updateDraft(id: String, draft: EditableImportDraft): ImportReviewQueue = copy(
  items = items.map { item ->
    if (item.id == id) item.copy(draft = draft.englishOnly(), reviewed = false) else item
  },
)

fun ImportReviewQueue.approvalResult(): ImportApprovalResult {
  val approved = items.filter { it.reviewed && it.draft.validateForSave().canSave }.map { it.draft }
  val rejected = items.filterNot { it.reviewed && it.draft.validateForSave().canSave }.map { it.id }
  return ImportApprovalResult(approved, rejected)
}

fun importReviewStatus(summary: OcrReviewSummary): String = when {
  summary.total == 0 -> "No reliably structured English MCQs were found. Nothing has been saved."
  summary.needsAttention > 0 -> "${summary.total} drafts found • ${summary.needsAttention} need attention before approval."
  else -> "${summary.total} drafts found and ready for review. Nothing is saved until you approve it."
}

fun importSourceLabel(source: String, metadata: OcrSourceMetadata): String =
  listOfNotNull(source.takeIf(String::isNotBlank), metadata.examName, metadata.year?.toString()).joinToString(" • ")

fun sourceMetadataSummary(metadata: OcrSourceMetadata): String = when {
  metadata.examName != null && metadata.year != null -> "${metadata.examName} • ${metadata.year}"
  metadata.examName != null -> metadata.examName
  metadata.year != null -> metadata.year.toString()
  else -> "Source metadata not detected"
}

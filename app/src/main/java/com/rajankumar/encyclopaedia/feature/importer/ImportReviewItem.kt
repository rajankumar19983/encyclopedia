package com.rajankumar.encyclopaedia.feature.importer

data class ImportReviewItem(
  val id: String,
  val draft: EditableImportDraft,
  val reviewed: Boolean = false,
)

fun List<EditableImportDraft>.toReviewItems(): List<ImportReviewItem> =
  mapIndexed { index, draft -> ImportReviewItem(importDraftId(draft.question, index), draft) }

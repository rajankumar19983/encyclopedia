package com.rajankumar.encyclopaedia.feature.importer

fun OcrImportSession.toEditableReviewItems(): List<ImportReviewItem> = drafts.mapIndexed { index, draft ->
  ImportReviewItem(
    id = importDraftId(draft.questionText, index),
    draft = EditableImportDraft(
      question = draft.questionText,
      options = draft.options,
      answer = draft.correctAnswer.orEmpty()
    )
  )
}

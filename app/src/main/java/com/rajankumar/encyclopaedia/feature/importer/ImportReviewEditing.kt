package com.rajankumar.encyclopaedia.feature.importer

fun ImportReviewQueue.updateDraft(id: String, draft: EditableImportDraft): ImportReviewQueue = copy(
  items = items.map { item ->
    if (item.id == id) item.copy(draft = draft.englishOnly(), reviewed = false) else item
  }
)

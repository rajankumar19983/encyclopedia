package com.rajankumar.encyclopaedia.feature.importer

fun ImportReviewQueue.reviewIfValidAndUnique(id: String): ImportReviewQueue {
  val duplicateIds = duplicateItemIds()
  return copy(
    items = items.map { item ->
      if (item.id != id) item
      else item.copy(
        reviewed = item.id !in duplicateIds &&
          item.draft.validateForSave().canSave &&
          item.draft.hasOnlyEnglishOcrContent()
      )
    }
  )
}

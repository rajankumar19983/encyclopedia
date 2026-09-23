package com.rajankumar.encyclopaedia.feature.importer

fun ImportReviewQueue.reviewIfValid(id: String): ImportReviewQueue = copy(
  items = items.map { item ->
    if (item.id != id) item
    else item.copy(reviewed = item.draft.validateForSave().canSave && item.draft.hasOnlyEnglishOcrContent())
  }
)

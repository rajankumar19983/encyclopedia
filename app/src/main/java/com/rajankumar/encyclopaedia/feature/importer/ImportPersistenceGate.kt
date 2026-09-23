package com.rajankumar.encyclopaedia.feature.importer

fun ImportReviewQueue.canPersistAll(): Boolean = complete && items.all { item ->
  item.reviewed && item.draft.hasOnlyEnglishOcrContent() && item.draft.validateForSave().canSave
}

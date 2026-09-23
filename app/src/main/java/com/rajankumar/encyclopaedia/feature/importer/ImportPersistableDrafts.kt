package com.rajankumar.encyclopaedia.feature.importer

fun ImportReviewQueue.persistableDrafts(): List<EditableImportDraft> =
  if (canPersistAll()) items.map { it.draft } else emptyList()

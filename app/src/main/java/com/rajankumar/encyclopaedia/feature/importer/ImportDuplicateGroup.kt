package com.rajankumar.encyclopaedia.feature.importer

data class ImportDuplicateGroup(
  val fingerprint: String,
  val itemIds: List<String>
)

fun ImportReviewQueue.duplicateGroups(): List<ImportDuplicateGroup> = items
  .groupBy { it.draft.importFingerprint() }
  .filterKeys { it.isNotBlank() }
  .filterValues { it.size > 1 }
  .map { (fingerprint, matches) -> ImportDuplicateGroup(fingerprint, matches.map { it.id }) }

fun ImportReviewQueue.duplicateItemIds(): Set<String> = duplicateGroups()
  .flatMap { it.itemIds }
  .toSet()

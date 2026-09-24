package com.rajankumar.encyclopaedia.feature.importer

data class ImportDuplicateAudit(
  val withinBatchGroups: List<ImportDuplicateGroup>,
  val existingMatches: List<ExistingDuplicateMatch>
) {
  val hasDuplicates: Boolean get() = withinBatchGroups.isNotEmpty() || existingMatches.isNotEmpty()
  val blockedItemIds: Set<String> get() = buildSet {
    withinBatchGroups.forEach { addAll(it.itemIds) }
    existingMatches.forEach { add(it.importItemId) }
  }
}

fun ImportReviewQueue.auditDuplicates(existing: List<ExistingQuestionFingerprint> = emptyList()): ImportDuplicateAudit =
  ImportDuplicateAudit(duplicateGroups(), findExistingDuplicates(existing))

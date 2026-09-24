package com.rajankumar.encyclopaedia.feature.importer

private val duplicateWhitespace = Regex("\\s+")
private val duplicatePunctuation = Regex("[^a-z0-9 ]")

data class ImportDuplicateGroup(
  val fingerprint: String,
  val itemIds: List<String>,
)

data class ImportDuplicateAudit(
  val withinBatchGroups: List<ImportDuplicateGroup>,
  val existingMatches: List<ExistingDuplicateMatch>,
) {
  val hasDuplicates: Boolean get() = withinBatchGroups.isNotEmpty() || existingMatches.isNotEmpty()
  val blockedItemIds: Set<String> get() = buildSet {
    withinBatchGroups.forEach { addAll(it.itemIds) }
    existingMatches.forEach { add(it.importItemId) }
  }
}

data class ImportDuplicateNotice(
  val title: String,
  val message: String,
)

fun normalizeImportFingerprintText(value: String): String = value
  .lowercase()
  .replace(duplicatePunctuation, " ")
  .replace(duplicateWhitespace, " ")
  .trim()

fun EditableImportDraft.importFingerprint(): String = buildString {
  append(normalizeImportFingerprintText(question))
  cleanedOptions.forEach { option ->
    append('|')
    append(normalizeImportFingerprintText(option))
  }
}

fun ImportReviewQueue.duplicateGroups(): List<ImportDuplicateGroup> = items
  .groupBy { it.draft.importFingerprint() }
  .filterKeys { it.isNotBlank() }
  .filterValues { it.size > 1 }
  .map { (fingerprint, matches) -> ImportDuplicateGroup(fingerprint, matches.map { it.id }) }

fun ImportReviewQueue.duplicateItemIds(): Set<String> = duplicateGroups()
  .flatMap { it.itemIds }
  .toSet()

fun ImportReviewQueue.auditDuplicates(
  existing: List<ExistingQuestionFingerprint> = emptyList(),
): ImportDuplicateAudit = ImportDuplicateAudit(duplicateGroups(), findExistingDuplicates(existing))

fun ImportDuplicateAudit.notices(): List<ImportDuplicateNotice> = buildList {
  if (withinBatchGroups.isNotEmpty()) {
    add(ImportDuplicateNotice(
      "Duplicate questions in import",
      "${withinBatchGroups.size} duplicate group${if (withinBatchGroups.size == 1) "" else "s"} must be resolved before saving.",
    ))
  }
  if (existingMatches.isNotEmpty()) {
    val count = existingMatches.map { it.importItemId }.distinct().size
    add(ImportDuplicateNotice(
      "Questions already exist",
      "$count imported question${if (count == 1) "" else "s"} match existing study-bank content.",
    ))
  }
}

fun ImportReviewQueue.reviewIfValidAndUnique(id: String): ImportReviewQueue {
  val duplicateIds = duplicateItemIds()
  return copy(
    items = items.map { item ->
      if (item.id != id) item
      else item.copy(
        reviewed = item.id !in duplicateIds &&
          item.draft.validateForSave().canSave &&
          item.draft.hasOnlyEnglishOcrContent(),
      )
    },
  )
}

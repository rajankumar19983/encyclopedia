package com.rajankumar.encyclopaedia.feature.importer

data class ImportPersistencePlan(
  val drafts: List<EditableImportDraft>,
  val blockedItemIds: Set<String>,
  val ready: Boolean
)

fun ImportReviewQueue.persistencePlan(existing: List<ExistingQuestionFingerprint> = emptyList()): ImportPersistencePlan {
  val duplicates = auditDuplicates(existing)
  val validAndReviewed = items.all { item ->
    item.reviewed && item.draft.hasOnlyEnglishOcrContent() && item.draft.validateForSave().canSave
  }
  val ready = items.isNotEmpty() && validAndReviewed && !duplicates.hasDuplicates
  return ImportPersistencePlan(
    drafts = if (ready) items.map { it.draft } else emptyList(),
    blockedItemIds = duplicates.blockedItemIds,
    ready = ready
  )
}

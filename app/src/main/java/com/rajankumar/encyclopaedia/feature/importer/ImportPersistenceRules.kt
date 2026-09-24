package com.rajankumar.encyclopaedia.feature.importer

data class ImportPersistencePlan(
  val drafts: List<EditableImportDraft>,
  val blockedItemIds: Set<String>,
  val ready: Boolean,
)

data class ImportSaveSelection(
  val selectedIds: Set<String> = emptySet(),
) {
  fun toggle(id: String): ImportSaveSelection = copy(
    selectedIds = if (id in selectedIds) selectedIds - id else selectedIds + id,
  )

  fun contains(id: String): Boolean = id in selectedIds
}

data class ImportSavePlan(
  val ready: List<ImportReviewItem>,
  val blocked: List<ImportReviewItem>,
)

fun ImportReviewQueue.canPersistAll(): Boolean = complete && items.all { item ->
  item.reviewed && item.draft.hasOnlyEnglishOcrContent() && item.draft.validateForSave().canSave
}

fun ImportReviewQueue.persistableDrafts(): List<EditableImportDraft> =
  if (canPersistAll()) items.map { it.draft } else emptyList()

fun ImportReviewQueue.persistencePlan(
  existing: List<ExistingQuestionFingerprint> = emptyList(),
): ImportPersistencePlan {
  val duplicates = auditDuplicates(existing)
  val validAndReviewed = items.all { item ->
    item.reviewed && item.draft.hasOnlyEnglishOcrContent() && item.draft.validateForSave().canSave
  }
  val ready = items.isNotEmpty() && validAndReviewed && !duplicates.hasDuplicates
  return ImportPersistencePlan(
    drafts = if (ready) items.map { it.draft } else emptyList(),
    blockedItemIds = duplicates.blockedItemIds,
    ready = ready,
  )
}

fun ImportReviewQueue.canPersistWithoutDuplicates(
  existing: List<ExistingQuestionFingerprint> = emptyList(),
): Boolean = persistencePlan(existing).ready

fun ImportReviewQueue.persistableUniqueDrafts(
  existing: List<ExistingQuestionFingerprint> = emptyList(),
): List<EditableImportDraft> = persistencePlan(existing).drafts

fun buildImportSavePlan(
  items: List<ImportReviewItem>,
  selection: ImportSaveSelection,
): ImportSavePlan {
  val selected = items.filter { selection.contains(it.id) }
  return ImportSavePlan(
    ready = selected.filter { it.draft.isValid() },
    blocked = selected.filterNot { it.draft.isValid() },
  )
}

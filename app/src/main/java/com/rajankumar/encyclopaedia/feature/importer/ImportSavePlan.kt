package com.rajankumar.encyclopaedia.feature.importer

data class ImportSavePlan(
  val ready: List<ImportReviewItem>,
  val blocked: List<ImportReviewItem>,
)

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

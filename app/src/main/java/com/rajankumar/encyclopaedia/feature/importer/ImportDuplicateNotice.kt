package com.rajankumar.encyclopaedia.feature.importer

data class ImportDuplicateNotice(
  val title: String,
  val message: String
)

fun ImportDuplicateAudit.notices(): List<ImportDuplicateNotice> = buildList {
  if (withinBatchGroups.isNotEmpty()) add(
    ImportDuplicateNotice(
      "Duplicate questions in import",
      "${withinBatchGroups.size} duplicate group${if (withinBatchGroups.size == 1) "" else "s"} must be resolved before saving."
    )
  )
  if (existingMatches.isNotEmpty()) add(
    ImportDuplicateNotice(
      "Questions already exist",
      "${existingMatches.map { it.importItemId }.distinct().size} imported question${if (existingMatches.map { it.importItemId }.distinct().size == 1) "" else "s"} match existing study-bank content."
    )
  )
}

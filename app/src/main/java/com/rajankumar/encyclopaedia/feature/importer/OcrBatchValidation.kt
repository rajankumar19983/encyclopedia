package com.rajankumar.encyclopaedia.feature.importer

data class OcrBatchValidation(
  val total: Int,
  val clean: Int,
  val flagged: Int,
  val duplicateGroups: Int,
)

fun validateOcrBatch(drafts: List<ParsedQuestionDraft>): OcrBatchValidation {
  val clean = drafts.count { validateOcrDraft(it).clean }
  return OcrBatchValidation(
    total = drafts.size,
    clean = clean,
    flagged = drafts.size - clean,
    duplicateGroups = duplicateOcrDraftGroups(drafts).size,
  )
}

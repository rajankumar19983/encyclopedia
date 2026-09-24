package com.rajankumar.encyclopaedia.feature.importer

data class ExistingQuestionFingerprint(
  val id: String,
  val question: String,
  val options: List<String>
) {
  val fingerprint: String get() = EditableImportDraft(question, options, "").importFingerprint()
}

data class ExistingDuplicateMatch(
  val importItemId: String,
  val existingQuestionId: String
)

fun ImportReviewQueue.findExistingDuplicates(existing: List<ExistingQuestionFingerprint>): List<ExistingDuplicateMatch> {
  val existingByFingerprint = existing.groupBy { it.fingerprint }
  return items.flatMap { item ->
    existingByFingerprint[item.draft.importFingerprint()].orEmpty().map { match ->
      ExistingDuplicateMatch(item.id, match.id)
    }
  }
}

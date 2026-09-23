package com.rajankumar.encyclopaedia.feature.importer

fun duplicateOcrWarning(drafts: List<ParsedQuestionDraft>): String? {
  val groups = duplicateOcrDraftGroups(drafts)
  if (groups.isEmpty()) return null
  val positions = groups.joinToString("; ") { group -> group.indexes.joinToString(", ") { (it + 1).toString() } }
  return "Possible duplicate OCR drafts detected at review positions: $positions. Verify before saving."
}

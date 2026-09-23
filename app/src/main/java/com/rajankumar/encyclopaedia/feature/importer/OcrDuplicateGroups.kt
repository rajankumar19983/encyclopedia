package com.rajankumar.encyclopaedia.feature.importer

data class OcrDuplicateGroup(val fingerprint: String, val indexes: List<Int>)

fun duplicateOcrDraftGroups(drafts: List<ParsedQuestionDraft>): List<OcrDuplicateGroup> = drafts
  .mapIndexed { index, draft -> draft.fingerprint() to index }
  .groupBy({ it.first }, { it.second })
  .filterValues { it.size > 1 }
  .map { (fingerprint, indexes) -> OcrDuplicateGroup(fingerprint, indexes) }
  .sortedBy { it.indexes.first() }

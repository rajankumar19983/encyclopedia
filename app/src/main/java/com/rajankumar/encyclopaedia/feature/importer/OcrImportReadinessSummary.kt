package com.rajankumar.encyclopaedia.feature.importer

data class OcrImportReadinessSummary(
  val total: Int,
  val ready: Int,
  val needsAttention: Int,
)

fun summarizeImportReadiness(drafts: List<ParsedQuestionDraft>): OcrImportReadinessSummary {
  val ready = drafts.count { it.importReadiness() == ImportReadiness.READY_FOR_REVIEW }
  return OcrImportReadinessSummary(drafts.size, ready, drafts.size - ready)
}

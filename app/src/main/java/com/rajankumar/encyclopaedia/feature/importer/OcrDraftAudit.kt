package com.rajankumar.encyclopaedia.feature.importer

data class OcrDraftAudit(
  val optionCountQuality: OcrOptionCountQuality,
  val answerConflict: OcrAnswerConflict,
  val warnings: List<String>,
)

fun auditOcrDraft(draft: ParsedQuestionDraft, sourceLines: List<String> = emptyList()): OcrDraftAudit {
  val conflict = detectOcrAnswerConflict(sourceLines)
  return OcrDraftAudit(
    optionCountQuality = classifyOcrOptionCount(draft.options.size),
    answerConflict = conflict,
    warnings = (draft.warnings + listOfNotNull(conflict.warning())).distinct(),
  )
}

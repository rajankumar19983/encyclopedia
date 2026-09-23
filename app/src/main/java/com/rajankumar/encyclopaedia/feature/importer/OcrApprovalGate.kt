package com.rajankumar.encyclopaedia.feature.importer

data class OcrApprovalGate(val allowed: Boolean, val reasons: List<String>)

fun evaluateOcrApproval(
  validation: EditableImportValidation,
  checklistState: OcrReviewChecklistState,
  checklist: List<OcrReviewChecklistItem> = ocrReviewChecklist(),
): OcrApprovalGate {
  val reasons = buildList {
    if (!validation.canSave) addAll(validation.issues)
    if (!checklistState.requiredComplete(checklist)) add("Complete every required review check before approval.")
  }.distinct()
  return OcrApprovalGate(reasons.isEmpty(), reasons)
}

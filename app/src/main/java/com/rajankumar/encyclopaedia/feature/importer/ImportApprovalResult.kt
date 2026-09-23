package com.rajankumar.encyclopaedia.feature.importer

data class ImportApprovalResult(
  val approved: List<EditableImportDraft>,
  val rejectedIds: List<String>
) {
  val canPersist: Boolean get() = approved.isNotEmpty() && rejectedIds.isEmpty()
}

fun ImportReviewQueue.approvalResult(): ImportApprovalResult {
  val approved = items.filter { it.reviewed && it.draft.validateForSave().canSave }.map { it.draft }
  val rejected = items.filterNot { it.reviewed && it.draft.validateForSave().canSave }.map { it.id }
  return ImportApprovalResult(approved, rejected)
}

package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreGate(
  val canProceed: Boolean,
  val needsReview: Boolean
)

fun BackupInspection.restoreGate(): BackupRestoreGate = BackupRestoreGate(
  canProceed = restoreReviewModel().restoreEnabled,
  needsReview = restoreReadiness() == BackupRestoreReadiness.REVIEW_WARNINGS
)

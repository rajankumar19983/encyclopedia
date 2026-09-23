package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreReviewAcknowledgement(
  val warningReviewRequired: Boolean,
  val acknowledged: Boolean
) {
  val canConfirm: Boolean get() = !warningReviewRequired || acknowledged
}

fun BackupInspection.restoreAcknowledgement(acknowledged: Boolean): BackupRestoreReviewAcknowledgement =
  BackupRestoreReviewAcknowledgement(
    warningReviewRequired = restoreReadiness() == BackupRestoreReadiness.REVIEW_WARNINGS,
    acknowledged = acknowledged
  )

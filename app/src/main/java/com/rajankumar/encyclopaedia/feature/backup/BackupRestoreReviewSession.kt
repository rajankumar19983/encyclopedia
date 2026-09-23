package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreReviewSession(
  val outcome: BackupRestoreReviewOutcome,
  val warningsAcknowledged: Boolean
) {
  val canExecuteRestore: Boolean
    get() = outcome.canRestore

  val acknowledgementRequired: Boolean
    get() = outcome.requiresAcknowledgement
}

fun BackupInspection.restoreReviewSession(
  warningsAcknowledged: Boolean = false
): BackupRestoreReviewSession = BackupRestoreReviewSession(
  outcome = restoreReviewOutcome(warningsAcknowledged),
  warningsAcknowledged = warningsAcknowledged
)

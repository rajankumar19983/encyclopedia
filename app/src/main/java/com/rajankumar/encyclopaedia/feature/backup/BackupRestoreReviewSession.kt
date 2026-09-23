package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreReviewSession(
  val outcome: BackupRestoreReviewOutcome,
  val warningsAcknowledged: Boolean
) {
  val canExecuteRestore: Boolean
    get() = outcome.restoreEnabled && (!outcome.requiresWarningAcknowledgement || warningsAcknowledged)

  val acknowledgementRequired: Boolean
    get() = outcome.requiresWarningAcknowledgement && !warningsAcknowledged
}

fun BackupInspection.restoreReviewSession(
  warningsAcknowledged: Boolean = false
): BackupRestoreReviewSession = BackupRestoreReviewSession(
  outcome = restoreReviewOutcome(),
  warningsAcknowledged = warningsAcknowledged
)

package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreSafetyState(
  val canRestore: Boolean,
  val acknowledgementRequired: Boolean,
  val blockingIssues: Int,
  val warnings: Int
)

fun BackupInspection.restoreSafetyState(warningsAcknowledged: Boolean = false): BackupRestoreSafetyState {
  val outcome = restoreReviewOutcome(warningsAcknowledged)
  return BackupRestoreSafetyState(outcome.canRestore, outcome.requiresAcknowledgement, outcome.blockingIssues, outcome.warnings)
}

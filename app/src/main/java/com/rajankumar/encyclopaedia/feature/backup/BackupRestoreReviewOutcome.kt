package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreReviewOutcome(
  val canRestore: Boolean,
  val requiresAcknowledgement: Boolean,
  val headline: String,
  val actionLabel: String,
  val blockingIssues: Int,
  val warnings: Int
)

fun BackupInspection.restoreReviewOutcome(
  warningsAcknowledged: Boolean
): BackupRestoreReviewOutcome {
  val model = restoreReviewModel()
  val execution = restoreExecutionGate(warningsAcknowledged)
  val readiness = restoreReadiness()
  return BackupRestoreReviewOutcome(
    canRestore = execution.canExecute,
    requiresAcknowledgement = readiness == BackupRestoreReadiness.REVIEW_WARNINGS && !warningsAcknowledged,
    headline = model.reviewHeadline(),
    actionLabel = readiness.actionLabel(),
    blockingIssues = model.blockingIssueCount,
    warnings = model.warningCount
  )
}

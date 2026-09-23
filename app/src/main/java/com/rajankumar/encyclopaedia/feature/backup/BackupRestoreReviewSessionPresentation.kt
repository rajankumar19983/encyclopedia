package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreReviewSessionPresentation(
  val headline: String,
  val description: String,
  val badges: List<BackupRestoreReviewBadge>,
  val actionLabel: String,
  val actionEnabled: Boolean,
  val acknowledgementRequired: Boolean
)

fun BackupInspection.restoreReviewSessionPresentation(
  warningsAcknowledged: Boolean = false
): BackupRestoreReviewSessionPresentation {
  val session = restoreReviewSession(warningsAcknowledged)
  val presentation = restoreReviewPresentation()
  return BackupRestoreReviewSessionPresentation(
    headline = presentation.headline,
    description = presentation.description,
    badges = presentation.badges,
    actionLabel = presentation.actionLabel,
    actionEnabled = session.canExecuteRestore,
    acknowledgementRequired = session.acknowledgementRequired
  )
}

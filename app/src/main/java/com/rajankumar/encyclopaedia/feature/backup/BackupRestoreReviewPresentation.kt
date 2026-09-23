package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreReviewPresentation(
  val headline: String,
  val description: String,
  val badges: List<BackupRestoreReviewBadge>,
  val actionLabel: String,
  val actionEnabled: Boolean
)

fun BackupInspection.restoreReviewPresentation(): BackupRestoreReviewPresentation {
  val model = restoreReviewModel()
  val readiness = restoreReadiness()
  return BackupRestoreReviewPresentation(
    headline = model.reviewHeadline(),
    description = model.reviewDescription(),
    badges = model.reviewBadges(),
    actionLabel = readiness.actionLabel(),
    actionEnabled = model.restoreEnabled
  )
}

package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreReviewPlan(
  val readiness: BackupRestoreReadiness,
  val risk: BackupRestoreRisk,
  val actionLabel: String,
  val restoreEnabled: Boolean,
  val sections: List<BackupRestoreReviewSection>
)

fun BackupInspection.restoreReviewPlan(): BackupRestoreReviewPlan {
  val readiness = restoreReadiness()
  val model = restoreReviewModel()
  return BackupRestoreReviewPlan(
    readiness = readiness,
    risk = restoreRisk(),
    actionLabel = readiness.actionLabel(),
    restoreEnabled = model.restoreEnabled,
    sections = model.sections.orderedForRestoreReview()
  )
}

package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreReviewAnalytics(
  val readiness: String,
  val issueCount: Int,
  val blockingIssueCount: Int,
  val warningCount: Int
)

fun BackupInspection.restoreReviewAnalytics(): BackupRestoreReviewAnalytics {
  val model = restoreReviewModel()
  return BackupRestoreReviewAnalytics(
    readiness = restoreReadiness().name,
    issueCount = model.issueCount,
    blockingIssueCount = model.blockingIssueCount,
    warningCount = model.warningCount
  )
}

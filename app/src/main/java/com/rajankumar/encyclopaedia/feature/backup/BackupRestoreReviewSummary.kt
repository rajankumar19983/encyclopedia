package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreReviewSummary(
  val sectionCount: Int,
  val issueCount: Int,
  val blockingIssueCount: Int,
  val warningCount: Int
)

fun BackupInspection.restoreReviewSummary(): BackupRestoreReviewSummary {
  val model = restoreReviewModel()
  return BackupRestoreReviewSummary(
    sectionCount = model.sections.size,
    issueCount = model.issueCount,
    blockingIssueCount = model.blockingIssueCount,
    warningCount = model.warningCount
  )
}

package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreIssueCounts(val errors: Int, val warnings: Int) {
  val total: Int get() = errors + warnings
}

fun BackupInspection.restoreIssueCounts(): BackupRestoreIssueCounts {
  val model = restoreReviewModel()
  return BackupRestoreIssueCounts(model.blockingIssueCount, model.warningCount)
}

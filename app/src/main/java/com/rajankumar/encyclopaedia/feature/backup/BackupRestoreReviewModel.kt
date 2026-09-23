package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreReviewModel(
  val title: String,
  val message: String,
  val restoreEnabled: Boolean,
  val sections: List<BackupRestoreReviewSection>
) {
  val issueCount: Int get() = sections.sumOf { it.errorCount + it.warningCount }
  val blockingIssueCount: Int get() = sections.sumOf { it.errorCount }
  val warningCount: Int get() = sections.sumOf { it.warningCount }
}

fun BackupInspection.restoreReviewModel(): BackupRestoreReviewModel {
  val review = restoreReview()
  return BackupRestoreReviewModel(
    title = review.title,
    message = review.message,
    restoreEnabled = review.restoreEnabled,
    sections = restoreReviewSections()
  )
}

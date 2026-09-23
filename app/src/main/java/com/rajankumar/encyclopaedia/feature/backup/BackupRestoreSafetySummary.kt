package com.rajankumar.encyclopaedia.feature.backup

fun BackupInspection.restoreSafetySummary(): String {
  val model = restoreReviewModel()
  return when {
    model.blockingIssueCount > 0 -> "${model.blockingIssueCount} blocking issue${if (model.blockingIssueCount == 1) "" else "s"}"
    model.warningCount > 0 -> if (model.warningCount == 1) "1 warning requires review" else "${model.warningCount} warnings require review"
    else -> "No restore safety issues"
  }
}

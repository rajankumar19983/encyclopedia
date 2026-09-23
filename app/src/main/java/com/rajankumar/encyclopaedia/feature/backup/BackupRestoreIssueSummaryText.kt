package com.rajankumar.encyclopaedia.feature.backup

fun BackupRestoreReviewModel.issueSummaryText(): String = when {
  issueCount == 0 -> "No issues found"
  blockingIssueCount == 0 -> "$warningCount warning${if (warningCount == 1) "" else "s"}"
  warningCount == 0 -> "$blockingIssueCount blocking issue${if (blockingIssueCount == 1) "" else "s"}"
  else -> "$blockingIssueCount blocking, $warningCount warning${if (warningCount == 1) "" else "s"}"
}

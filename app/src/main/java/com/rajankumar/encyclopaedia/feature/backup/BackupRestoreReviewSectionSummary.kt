package com.rajankumar.encyclopaedia.feature.backup

fun BackupRestoreReviewSection.summaryLabel(): String = buildString {
  append(group.label())
  append(": ")
  append(issueCountLabel())
  if (errorCount > 0) append(", $errorCount blocking")
  if (warningCount > 0) append(", $warningCount warning${if (warningCount == 1) "" else "s"}")
}

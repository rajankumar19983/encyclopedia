package com.rajankumar.encyclopaedia.feature.backup

fun BackupRestoreReviewSection.issueCountLabel(): String {
  val total = errorCount + warningCount
  return "$total issue${if (total == 1) "" else "s"}"
}

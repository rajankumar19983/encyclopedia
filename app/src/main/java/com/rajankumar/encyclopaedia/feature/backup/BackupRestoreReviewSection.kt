package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegritySeverity

data class BackupRestoreReviewSection(
  val group: BackupRestoreIssueGroup,
  val errorCount: Int,
  val warningCount: Int,
  val messages: List<String>
) {
  val hasBlockingIssues: Boolean get() = errorCount > 0
}

fun BackupInspection.restoreReviewSections(): List<BackupRestoreReviewSection> =
  restoreIssueGroups().map { summary ->
    BackupRestoreReviewSection(
      group = summary.group,
      errorCount = summary.issues.count { it.severity == IntegritySeverity.ERROR },
      warningCount = summary.issues.count { it.severity == IntegritySeverity.WARNING },
      messages = summary.issues.map { it.message }
    )
  }

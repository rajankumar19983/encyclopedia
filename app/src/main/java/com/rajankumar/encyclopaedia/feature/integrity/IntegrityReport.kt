package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.feature.backup.BackupSnapshot

data class IntegrityReport(
  val issues: Set<IntegrityIssue>,
  val recordCount: Int
) {
  val valid: Boolean get() = issues.isEmpty()
  val messages: List<String> get() = issues.map { it.message() }
  val details: List<IntegrityIssueDetail> get() = issues.map { it.detail() }
  val issueCount: Int get() = issues.size
  val errorCount: Int get() = details.count { it.severity == IntegritySeverity.ERROR }
  val warningCount: Int get() = details.count { it.severity == IntegritySeverity.WARNING }
  val hasBlockingIssues: Boolean get() = errorCount > 0
}

fun BackupSnapshot.integrityReport(): IntegrityReport = IntegrityReport(
  issues = integrityIssues(),
  recordCount = recordCount
)

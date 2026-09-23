package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssueDetail

data class BackupRestoreIssueSummary(
  val errors: List<IntegrityIssueDetail>,
  val warnings: List<IntegrityIssueDetail>
) {
  val errorCount: Int get() = errors.size
  val warningCount: Int get() = warnings.size
  val hasErrors: Boolean get() = errors.isNotEmpty()
  val hasWarnings: Boolean get() = warnings.isNotEmpty()
}

fun BackupInspection.restoreIssueSummary(): BackupRestoreIssueSummary {
  val details = integrity?.details.orEmpty()
  return BackupRestoreIssueSummary(
    errors = details.filter { it.severity.name == "ERROR" },
    warnings = details.filter { it.severity.name == "WARNING" }
  )
}

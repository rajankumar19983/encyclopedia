package com.rajankumar.encyclopaedia.feature.integrity

data class IntegrityCheckPresentation(
  val title: String,
  val summary: String,
  val status: String,
  val blockingIssues: Int,
  val warnings: Int,
  val recordsChecked: Int,
  val details: List<IntegrityIssueDetail>
)

fun IntegrityReport.toCheckPresentation(): IntegrityCheckPresentation = IntegrityCheckPresentation(
  title = when {
    hasBlockingIssues -> "Data integrity issues found"
    warningCount > 0 -> "Data integrity warnings found"
    else -> "Data integrity check passed"
  },
  summary = when {
    hasBlockingIssues -> "$errorCount blocking issue${if (errorCount == 1) "" else "s"} must be resolved before this data is safe to restore."
    warningCount > 0 -> "$warningCount warning${if (warningCount == 1) "" else "s"} found. Your data remains usable, but review the details."
    else -> "No structural or relationship problems were found in the current study data."
  },
  status = when {
    hasBlockingIssues -> "Action required"
    warningCount > 0 -> "Review recommended"
    else -> "Healthy"
  },
  blockingIssues = errorCount,
  warnings = warningCount,
  recordsChecked = recordCount,
  details = details.sortedWith(compareBy<IntegrityIssueDetail> { it.severity != IntegritySeverity.ERROR }.thenBy { it.issue.code() })
)

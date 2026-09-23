package com.rajankumar.encyclopaedia.feature.integrity

fun IntegrityReport.summaryText(): String {
  val overview = overview()
  return when (overview.status) {
    IntegrityStatus.HEALTHY -> "${overview.recordCount} records checked. No integrity issues found."
    IntegrityStatus.WARNING -> "${overview.recordCount} records checked. ${overview.warningCount} warning(s) found."
    IntegrityStatus.BLOCKED -> "${overview.recordCount} records checked. ${overview.errorCount} blocking issue(s) and ${overview.warningCount} warning(s) found."
  }
}

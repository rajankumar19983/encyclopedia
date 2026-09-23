package com.rajankumar.encyclopaedia.feature.integrity

fun IntegrityReport.sortedDetails(): List<IntegrityIssueDetail> = details.sortedWith(
  compareBy<IntegrityIssueDetail> { if (it.severity == IntegritySeverity.ERROR) 0 else 1 }
    .thenBy { it.issue.code() }
)

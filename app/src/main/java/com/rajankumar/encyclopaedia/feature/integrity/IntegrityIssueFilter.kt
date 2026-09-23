package com.rajankumar.encyclopaedia.feature.integrity

fun IntegrityReport.issueDetails(severity: IntegritySeverity): List<IntegrityIssueDetail> =
  details.filter { it.severity == severity }

package com.rajankumar.encyclopaedia.feature.integrity

fun IntegrityReport.primaryIssue(): IntegrityIssueDetail? = details.firstOrNull { it.severity == IntegritySeverity.ERROR }
  ?: details.firstOrNull()

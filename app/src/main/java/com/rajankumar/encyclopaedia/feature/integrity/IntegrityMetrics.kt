package com.rajankumar.encyclopaedia.feature.integrity

data class IntegrityMetrics(
  val records: Int,
  val issues: Int,
  val errors: Int,
  val warnings: Int
)

fun IntegrityReport.metrics(): IntegrityMetrics = IntegrityMetrics(
  records = recordCount,
  issues = issueCount,
  errors = errorCount,
  warnings = warningCount
)

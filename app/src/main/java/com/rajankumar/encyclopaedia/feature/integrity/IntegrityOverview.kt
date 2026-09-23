package com.rajankumar.encyclopaedia.feature.integrity

data class IntegrityOverview(
  val status: IntegrityStatus,
  val label: String,
  val recordCount: Int,
  val errorCount: Int,
  val warningCount: Int
)

fun IntegrityReport.overview(): IntegrityOverview = IntegrityOverview(
  status = status,
  label = status.label(),
  recordCount = recordCount,
  errorCount = errorCount,
  warningCount = warningCount
)

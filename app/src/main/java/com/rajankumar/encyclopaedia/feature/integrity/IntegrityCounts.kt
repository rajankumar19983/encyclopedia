package com.rajankumar.encyclopaedia.feature.integrity

data class IntegrityCounts(
  val total: Int,
  val blocking: Int,
  val warnings: Int
)

fun IntegrityReport.counts(): IntegrityCounts = IntegrityCounts(
  total = issueCount,
  blocking = errorCount,
  warnings = warningCount
)

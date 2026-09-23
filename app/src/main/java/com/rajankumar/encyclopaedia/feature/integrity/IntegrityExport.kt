package com.rajankumar.encyclopaedia.feature.integrity

data class IntegrityExportItem(
  val code: String,
  val severity: String,
  val message: String
)

fun IntegrityReport.exportItems(): List<IntegrityExportItem> = details.map { detail ->
  IntegrityExportItem(
    code = detail.issue.code(),
    severity = detail.severity.name.lowercase(),
    message = detail.message
  )
}

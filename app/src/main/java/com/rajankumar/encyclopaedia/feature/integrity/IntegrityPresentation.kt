package com.rajankumar.encyclopaedia.feature.integrity

data class IntegrityPresentation(
  val headline: String,
  val summary: String,
  val primaryMessage: String?
)

fun IntegrityReport.presentation(): IntegrityPresentation = IntegrityPresentation(
  headline = status.label(),
  summary = summaryText(),
  primaryMessage = primaryIssue()?.message
)

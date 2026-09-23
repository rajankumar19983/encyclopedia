package com.rajankumar.encyclopaedia.feature.integrity

data class IntegrityIssueDetail(
  val issue: IntegrityIssue,
  val severity: IntegritySeverity,
  val message: String
)

fun IntegrityIssue.detail(): IntegrityIssueDetail = IntegrityIssueDetail(
  issue = this,
  severity = severity(),
  message = message()
)

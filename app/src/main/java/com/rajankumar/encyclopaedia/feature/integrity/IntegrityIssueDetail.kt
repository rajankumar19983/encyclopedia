package com.rajankumar.encyclopaedia.feature.integrity

data class IntegrityIssueDetail(
  val issue: IntegrityIssue,
  val message: String
)

fun IntegrityIssue.detail(): IntegrityIssueDetail = IntegrityIssueDetail(
  issue = this,
  message = message()
)

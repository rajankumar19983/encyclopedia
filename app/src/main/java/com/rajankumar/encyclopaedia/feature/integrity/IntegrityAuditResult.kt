package com.rajankumar.encyclopaedia.feature.integrity

data class IntegrityAuditResult(
  val report: IntegrityReport,
  val decision: RestoreIntegrityDecision,
  val presentation: IntegrityPresentation
)

fun IntegrityReport.auditResult(): IntegrityAuditResult = IntegrityAuditResult(
  report = this,
  decision = restoreDecision(),
  presentation = presentation()
)

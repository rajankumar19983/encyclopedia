package com.rajankumar.encyclopaedia.feature.integrity

data class RestoreIntegrityDecision(
  val allowed: Boolean,
  val warnings: List<IntegrityIssueDetail>,
  val blockers: List<IntegrityIssueDetail>
)

fun IntegrityReport.restoreDecision(): RestoreIntegrityDecision {
  val blockers = details.filter { it.severity == IntegritySeverity.ERROR }
  val warnings = details.filter { it.severity == IntegritySeverity.WARNING }
  return RestoreIntegrityDecision(
    allowed = blockers.isEmpty(),
    warnings = warnings,
    blockers = blockers
  )
}

package com.rajankumar.encyclopaedia.feature.integrity

enum class IntegrityStatus {
  HEALTHY,
  WARNING,
  BLOCKED
}

val IntegrityReport.status: IntegrityStatus
  get() = when {
    hasBlockingIssues -> IntegrityStatus.BLOCKED
    warningCount > 0 -> IntegrityStatus.WARNING
    else -> IntegrityStatus.HEALTHY
  }

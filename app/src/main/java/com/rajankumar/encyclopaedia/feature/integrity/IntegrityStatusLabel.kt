package com.rajankumar.encyclopaedia.feature.integrity

fun IntegrityStatus.label(): String = when (this) {
  IntegrityStatus.HEALTHY -> "Backup is healthy"
  IntegrityStatus.WARNING -> "Backup has warnings"
  IntegrityStatus.BLOCKED -> "Backup cannot be safely restored"
}

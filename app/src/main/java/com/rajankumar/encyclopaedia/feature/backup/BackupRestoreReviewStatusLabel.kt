package com.rajankumar.encyclopaedia.feature.backup

fun BackupRestoreReviewSessionPresentation.reviewStatusLabel(): String = when {
  actionEnabled -> "Ready to restore"
  acknowledgementRequired -> "Review required"
  else -> "Restore blocked"
}

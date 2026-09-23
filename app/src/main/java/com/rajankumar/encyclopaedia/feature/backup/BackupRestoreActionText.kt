package com.rajankumar.encyclopaedia.feature.backup

fun BackupRestoreReviewSessionPresentation.restoreActionText(): String = when {
  actionEnabled -> "Create safety backup & restore"
  acknowledgementRequired -> "Review warnings to continue"
  else -> actionLabel
}

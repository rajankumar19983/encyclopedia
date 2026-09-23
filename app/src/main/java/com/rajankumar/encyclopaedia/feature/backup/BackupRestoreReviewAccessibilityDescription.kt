package com.rajankumar.encyclopaedia.feature.backup

fun BackupRestoreReviewSessionPresentation.accessibilityDescription(): String = buildString {
  append(headline)
  append(". ")
  append(description)
  if (acknowledgementRequired) append(" Warning acknowledgement is required before restore.")
  if (!actionEnabled && !acknowledgementRequired) append(" Restore is unavailable.")
}

package com.rajankumar.encyclopaedia.feature.backup

fun BackupRestoreReviewPresentation.accessibilitySummary(): String = buildString {
  append(headline)
  append(". ")
  append(description)
  if (badges.isNotEmpty()) {
    append(" ")
    append(badges.joinToString(". ") { "${it.label}: ${it.count}" })
    append(".")
  }
}

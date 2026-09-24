package com.rajankumar.encyclopaedia.feature.revision

fun RevisionSessionProgress.label(): String = when {
  safeTotal == 0 -> "No revision session"
  percent >= 100 -> "Revision session complete"
  else -> "$safeCompleted of $safeTotal revised • $percent%"
}

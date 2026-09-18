package com.rajankumar.encyclopaedia.feature.revision

enum class RevisionReason(val label: String) {
  INCORRECT("Answered incorrectly"),
  REPEATED_MISTAKE("Repeated mistake"),
  SLOW_ANSWER("Slow answer"),
  LOW_ACCURACY("Low accuracy"),
  MANUAL("Added for revision")
}

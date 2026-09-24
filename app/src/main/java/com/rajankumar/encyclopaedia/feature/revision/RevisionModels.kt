package com.rajankumar.encyclopaedia.feature.revision

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

enum class RevisionPriority(val label: String) {
  URGENT("Urgent"), HIGH("High"), NORMAL("Normal"), LOW("Low")
}

enum class RevisionReason(val label: String) {
  INCORRECT("Answered incorrectly"),
  REPEATED_MISTAKE("Repeated mistake"),
  SLOW_ANSWER("Slow answer"),
  LOW_ACCURACY("Low accuracy"),
  MANUAL("Added for revision")
}

data class RevisionItem(
  val question: QuestionEntity,
  val mistakes: Int,
  val attempts: Int,
  val priority: RevisionPriority,
  val reasons: Set<RevisionReason>
) {
  val mistakeRatePercent: Int get() = revisionMistakeRatePercent(mistakes, attempts)
  val accuracyPercent: Int get() = revisionAccuracyPercent((attempts - mistakes).coerceAtLeast(0), attempts)
  val reasonSummary: String get() = reasons.revisionReasonSummary()
}

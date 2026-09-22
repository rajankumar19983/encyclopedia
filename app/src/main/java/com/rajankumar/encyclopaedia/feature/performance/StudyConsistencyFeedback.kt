package com.rajankumar.encyclopaedia.feature.performance

data class StudyConsistencyFeedback(val headline: String, val detail: String)

fun studyConsistencyFeedback(consistency: StudyConsistency): StudyConsistencyFeedback = when {
  consistency.activeDays == 0 -> StudyConsistencyFeedback("Start your study streak", "Complete a practice session to record your first active day.")
  consistency.currentStreak >= 7 -> StudyConsistencyFeedback("Strong ${consistency.currentStreak}-day streak", "Consistency is becoming a habit. Keep the streak useful by revising weak topics too.")
  consistency.currentStreak >= 2 -> StudyConsistencyFeedback("${consistency.currentStreak}-day study streak", "Keep showing up daily to build a longer learning rhythm.")
  consistency.currentStreak == 1 -> StudyConsistencyFeedback("Studied today", "Return tomorrow to turn today's work into a streak.")
  else -> StudyConsistencyFeedback("Restart your streak", "Your longest streak is ${consistency.longestStreak} days. A short session today starts a new one.")
}

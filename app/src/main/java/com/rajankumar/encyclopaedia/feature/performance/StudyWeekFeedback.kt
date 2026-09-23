package com.rajankumar.encyclopaedia.feature.performance

data class StudyWeekFeedback(
  val headline: String,
  val detail: String,
)

fun WeeklyStudySummary.feedback(): StudyWeekFeedback = when {
  attempts == 0 -> StudyWeekFeedback("Start this week's practice", "No question attempts are recorded in the last seven days.")
  activeDays >= 5 -> StudyWeekFeedback("Strong study rhythm", "You studied on $activeDays of the last 7 days with $accuracyPercent% accuracy.")
  activeDays >= 3 -> StudyWeekFeedback("Building consistency", "You studied on $activeDays of the last 7 days. One more active day will strengthen the routine.")
  else -> StudyWeekFeedback("Study more regularly", "You studied on $activeDays of the last 7 days. Spread practice across more days instead of relying on a single session.")
}

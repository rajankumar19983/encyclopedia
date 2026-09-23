package com.rajankumar.encyclopaedia.feature.performance

data class StudyPace(
  val attemptsPerActiveDay: Int,
  val averageTimePerActiveDayMs: Long,
)

fun WeeklyStudySummary.studyPace(): StudyPace = StudyPace(
  attemptsPerActiveDay = if (activeDays == 0) 0 else attempts / activeDays,
  averageTimePerActiveDayMs = if (activeDays == 0) 0 else totalTimeMs / activeDays,
)

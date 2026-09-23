package com.rajankumar.encyclopaedia.feature.home

enum class HomeStudyStatus { EMPTY, STARTING, NEEDS_REVIEW, BUILDING_COVERAGE, ESTABLISHED }

fun homeStudyStatus(questionCount: Int, attemptCount: Int, accuracyPercent: Int, coveragePercent: Int): HomeStudyStatus = when {
  questionCount == 0 -> HomeStudyStatus.EMPTY
  attemptCount == 0 -> HomeStudyStatus.STARTING
  accuracyPercent < 60 -> HomeStudyStatus.NEEDS_REVIEW
  coveragePercent < 100 -> HomeStudyStatus.BUILDING_COVERAGE
  else -> HomeStudyStatus.ESTABLISHED
}

package com.rajankumar.encyclopaedia.feature.questions

fun recommendPracticeSize(questionCount: Int, previousAccuracy: Int?): Int {
  if (questionCount <= 0) return 0
  val desired = when {
    previousAccuracy == null -> 10
    previousAccuracy < 50 -> 10
    previousAccuracy < 75 -> 20
    else -> 30
  }
  return minOf(questionCount, desired, PracticeConstants.maxSessionQuestions)
}

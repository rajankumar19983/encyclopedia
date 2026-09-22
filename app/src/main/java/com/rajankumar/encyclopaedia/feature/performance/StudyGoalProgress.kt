package com.rajankumar.encyclopaedia.feature.performance

data class StudyGoalProgress(val completed: Int, val target: Int) {
  val percent: Int get() = if (target <= 0) 0 else (completed.coerceAtLeast(0) * 100 / target).coerceAtMost(100)
  val remaining: Int get() = (target - completed).coerceAtLeast(0)
  val isComplete: Boolean get() = target > 0 && completed >= target
}

fun studyGoalProgress(volume: StudyVolume, targetAttempts: Int = 20): StudyGoalProgress =
  StudyGoalProgress(volume.attempts, targetAttempts.coerceAtLeast(1))

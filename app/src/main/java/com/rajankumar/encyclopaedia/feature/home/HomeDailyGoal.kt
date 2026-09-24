package com.rajankumar.encyclopaedia.feature.home

data class HomeDailyGoal(val completed: Int, val target: Int) {
  val safeTarget: Int get() = target.coerceAtLeast(1)
  val safeCompleted: Int get() = completed.coerceIn(0, safeTarget)
  val percent: Int get() = safeCompleted * 100 / safeTarget
}

package com.rajankumar.encyclopaedia.feature.home

data class HomeTaskSummary(val pending: Int, val completed: Int) {
  val total: Int get() = pending.coerceAtLeast(0) + completed.coerceAtLeast(0)
  val progress: HomePlanProgress get() = HomePlanProgress(completed, total)
}

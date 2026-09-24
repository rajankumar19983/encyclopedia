package com.rajankumar.encyclopaedia.feature.home

data class HomeTodaySummary(val completedTasks: Int, val totalTasks: Int, val revisionCount: Int) {
  fun label(): String = "${homePlannerCountLabel(completedTasks, totalTasks)} • ${homeRevisionCountLabel(revisionCount)}"
}

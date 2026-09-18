package com.rajankumar.encyclopaedia.feature.revision

enum class RevisionHealth(val label: String) { CLEAR("Clear"), LIGHT("Light"), BUILDING("Building"), HEAVY("Heavy") }

fun List<RevisionItem>.revisionHealth(): RevisionHealth = when {
  isEmpty() -> RevisionHealth.CLEAR
  count { it.priority == RevisionPriority.URGENT } >= 5 -> RevisionHealth.HEAVY
  size >= 15 -> RevisionHealth.BUILDING
  else -> RevisionHealth.LIGHT
}

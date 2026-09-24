package com.rajankumar.encyclopaedia.feature.revision

enum class RevisionHealth(val label: String) { CLEAR("Clear"), LIGHT("Light"), BUILDING("Building"), HEAVY("Heavy") }

fun List<RevisionItem>.revisionHealth(): RevisionHealth = when {
  isEmpty() -> RevisionHealth.CLEAR
  count { it.priority == RevisionPriority.URGENT } >= 5 -> RevisionHealth.HEAVY
  size >= 15 -> RevisionHealth.BUILDING
  else -> RevisionHealth.LIGHT
}

fun RevisionHealth.guidance(): String = when (this) {
  RevisionHealth.CLEAR -> "Keep practising to maintain recall."
  RevisionHealth.LIGHT -> "A short revision session should keep the queue manageable."
  RevisionHealth.BUILDING -> "Prioritise revision before adding too much new material."
  RevisionHealth.HEAVY -> "Work through urgent questions first and use shorter focused sessions."
}

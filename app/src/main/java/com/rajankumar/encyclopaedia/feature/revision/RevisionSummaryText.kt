package com.rajankumar.encyclopaedia.feature.revision

fun RevisionStats.summaryText(): String = when {
  total == 0 -> "Revision queue is clear"
  urgent > 0 -> "$total to revise • $urgent urgent"
  high > 0 -> "$total to revise • $high high priority"
  else -> "$total question${if (total == 1) "" else "s"} to revise"
}

fun RevisionStats.accessibilitySummary(): String = when {
  total == 0 -> "Revision queue is clear."
  else -> "$total questions need revision, including $urgent urgent and $high high priority."
}

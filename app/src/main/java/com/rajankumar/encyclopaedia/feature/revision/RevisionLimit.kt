package com.rajankumar.encyclopaedia.feature.revision

fun recommendedRevisionLimit(queueSize: Int): Int = when {
  queueSize <= 0 -> 0
  queueSize <= 10 -> queueSize
  queueSize <= 25 -> 10
  else -> RevisionConstants.defaultSessionSize
}

fun revisionSessionLimitLabel(queueSize: Int): String {
  val limit = recommendedRevisionLimit(queueSize)
  return if (limit == 0) "No session ready" else "$limit question${if (limit == 1) "" else "s"} recommended"
}

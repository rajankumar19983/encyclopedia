package com.rajankumar.encyclopaedia.feature.revision

data class RevisionSessionPlan(val queueSize: Int, val sessionSize: Int) {
  val deferred: Int get() = (queueSize - sessionSize).coerceAtLeast(0)
  val summary: String get() = if (sessionSize == 0) "No revision due" else "Revise $sessionSize question${if (sessionSize == 1) "" else "s"}"
}

fun revisionSessionPlan(queueSize: Int, maxSession: Int = 20): RevisionSessionPlan {
  val safeQueue = queueSize.coerceAtLeast(0)
  return RevisionSessionPlan(safeQueue, suggestedRevisionSessionSize(safeQueue, maxSession))
}

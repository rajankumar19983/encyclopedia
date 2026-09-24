package com.rajankumar.encyclopaedia.feature.revision

fun suggestedRevisionSessionSize(queueSize: Int, maxSession: Int = 20): Int {
  val safeQueue = queueSize.coerceAtLeast(0)
  val safeMax = maxSession.coerceAtLeast(1)
  return safeQueue.coerceAtMost(safeMax)
}

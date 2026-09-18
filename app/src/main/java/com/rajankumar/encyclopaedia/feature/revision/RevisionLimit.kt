package com.rajankumar.encyclopaedia.feature.revision

fun recommendedRevisionLimit(queueSize: Int): Int = when {
  queueSize <= 0 -> 0
  queueSize <= 10 -> queueSize
  queueSize <= 25 -> 10
  else -> 20
}

package com.rajankumar.encyclopaedia.feature.revision

fun revisionAttemptLabel(count: Int): String = when (val safe = count.coerceAtLeast(0)) {
  0 -> "Not attempted yet"
  1 -> "1 revision attempt"
  else -> "$safe revision attempts"
}

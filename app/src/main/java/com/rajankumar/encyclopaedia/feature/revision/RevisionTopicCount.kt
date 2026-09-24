package com.rajankumar.encyclopaedia.feature.revision

fun revisionTopicCountLabel(count: Int): String = when (val safe = count.coerceAtLeast(0)) {
  0 -> "No weak topics"
  1 -> "1 topic needs revision"
  else -> "$safe topics need revision"
}

package com.rajankumar.encyclopaedia.feature.revision

fun revisionDueLabel(count: Int): String = when (val safe = count.coerceAtLeast(0)) {
  0 -> "Nothing due for revision"
  1 -> "1 question due"
  else -> "$safe questions due"
}

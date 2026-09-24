package com.rajankumar.encyclopaedia.feature.backup

fun backupItemCountLabel(count: Int): String = when (val safe = count.coerceAtLeast(0)) {
  0 -> "No items"
  1 -> "1 item"
  else -> "$safe items"
}

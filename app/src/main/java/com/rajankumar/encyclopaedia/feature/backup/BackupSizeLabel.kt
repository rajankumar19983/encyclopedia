package com.rajankumar.encyclopaedia.feature.backup

fun backupSizeLabel(bytes: Long): String {
  val safe = bytes.coerceAtLeast(0)
  return when {
    safe < 1024 -> "$safe B"
    safe < 1024 * 1024 -> "${safe / 1024} KB"
    else -> "${safe / (1024 * 1024)} MB"
  }
}

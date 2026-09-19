package com.rajankumar.encyclopaedia.feature.backup

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val BACKUP_FILE_PREFIX = "encyclopaedia-backup"
private const val BACKUP_FILE_EXTENSION = "json"

fun backupFileName(createdAt: Long): String {
  val formatter = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US).apply {
    timeZone = TimeZone.getTimeZone("UTC")
  }
  return "$BACKUP_FILE_PREFIX-${formatter.format(Date(createdAt))}.$BACKUP_FILE_EXTENSION"
}

fun isSupportedBackupFileName(name: String): Boolean =
  name.startsWith("$BACKUP_FILE_PREFIX-") && name.endsWith(".$BACKUP_FILE_EXTENSION")

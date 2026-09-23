package com.rajankumar.encyclopaedia.feature.backup

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val BACKUP_FILE_PREFIX = "encyclopaedia_backup"
private const val BACKUP_FILE_EXTENSION = "encbackup"

fun backupFileName(
  createdAt: Long,
  type: BackupType = BackupType.MANUAL,
  formatVersion: Int = BACKUP_FORMAT_VERSION
): String {
  val formatter = SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US).apply {
    timeZone = TimeZone.getTimeZone("UTC")
  }
  val typePart = type.name.lowercase(Locale.US)
  return "${BACKUP_FILE_PREFIX}_${formatter.format(Date(createdAt))}_${typePart}_v$formatVersion.$BACKUP_FILE_EXTENSION"
}

fun isSupportedBackupFileName(name: String): Boolean =
  BACKUP_NAME_PATTERN.matches(name)

private val BACKUP_NAME_PATTERN = Regex(
  "^${BACKUP_FILE_PREFIX}_\\d{4}-\\d{2}-\\d{2}_\\d{6}_(automatic|manual)_v\\d+\\.${BACKUP_FILE_EXTENSION}$"
)

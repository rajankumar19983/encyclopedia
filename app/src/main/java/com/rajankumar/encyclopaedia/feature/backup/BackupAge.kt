package com.rajankumar.encyclopaedia.feature.backup

private const val DAY_MS = 86_400_000L

enum class BackupAge { TODAY, RECENT, STALE }

fun backupAge(createdAt: Long, now: Long): BackupAge {
  val age = (now - createdAt).coerceAtLeast(0)
  return when {
    age < DAY_MS -> BackupAge.TODAY
    age <= 7 * DAY_MS -> BackupAge.RECENT
    else -> BackupAge.STALE
  }
}

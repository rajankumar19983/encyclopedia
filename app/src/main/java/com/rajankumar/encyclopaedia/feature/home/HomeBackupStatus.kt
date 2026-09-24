package com.rajankumar.encyclopaedia.feature.home

fun homeBackupStatusLabel(daysSinceBackup: Int?): String = when {
  daysSinceBackup == null -> "No backup yet"
  daysSinceBackup <= 0 -> "Backup current"
  daysSinceBackup <= 7 -> "Backup ${daysSinceBackup}d ago"
  else -> "Backup recommended"
}

package com.rajankumar.encyclopaedia.feature.backup

fun backupScreenAccessibility(health: BackupHealth): String {
  val status = backupScreenStatus(health)
  return "Backup and restore. ${status.headline}. ${status.detail}"
}

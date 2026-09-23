package com.rajankumar.encyclopaedia.feature.backup

fun BackupHealth.message(): String = when (this) {
  BackupHealth.HEALTHY -> "Your recent backup provides a recovery point for local study data."
  BackupHealth.NEEDS_BACKUP -> "Your latest backup is over a week old. Create a fresh backup before major changes."
  BackupHealth.NO_BACKUP -> "No recovery point exists yet. Create your first backup to protect local study data."
}

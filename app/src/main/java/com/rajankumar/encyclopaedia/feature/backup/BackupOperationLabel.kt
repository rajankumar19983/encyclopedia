package com.rajankumar.encyclopaedia.feature.backup

enum class BackupOperation { IDLE, CREATING, RESTORING }

fun BackupOperation.label(): String = when (this) {
  BackupOperation.IDLE -> "Ready"
  BackupOperation.CREATING -> "Creating backup…"
  BackupOperation.RESTORING -> "Restoring backup…"
}

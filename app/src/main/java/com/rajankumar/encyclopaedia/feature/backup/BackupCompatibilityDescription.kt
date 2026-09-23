package com.rajankumar.encyclopaedia.feature.backup

fun BackupCompatibility.description(): String = when (this) {
  BackupCompatibility.SUPPORTED -> "This backup format is supported."
  BackupCompatibility.TOO_OLD -> "This backup was created with an older unsupported format."
  BackupCompatibility.TOO_NEW -> "This backup was created by a newer app version."
}

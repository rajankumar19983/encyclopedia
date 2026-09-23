package com.rajankumar.encyclopaedia.feature.backup

fun BackupPreflight.summary(): String = when (compatibility) {
  BackupCompatibility.SUPPORTED -> "$recordCount records are ready for integrity inspection."
  BackupCompatibility.TOO_OLD -> "Restore is blocked because this backup format is too old."
  BackupCompatibility.TOO_NEW -> "Restore is blocked until the app supports this newer backup format."
}

package com.rajankumar.encyclopaedia.feature.backup

enum class BackupResult { SUCCESS, FAILED, CANCELLED }

fun BackupResult.label(): String = when (this) { BackupResult.SUCCESS -> "Backup complete"; BackupResult.FAILED -> "Backup failed"; BackupResult.CANCELLED -> "Backup cancelled" }

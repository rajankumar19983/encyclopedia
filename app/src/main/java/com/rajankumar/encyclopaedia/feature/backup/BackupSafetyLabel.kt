package com.rajankumar.encyclopaedia.feature.backup

fun BackupSafetyState.label(): String = when (this) { BackupSafetyState.SAFE -> "Backup protected"; BackupSafetyState.DUE -> "Backup recommended"; BackupSafetyState.OVERDUE -> "Backup needed" }

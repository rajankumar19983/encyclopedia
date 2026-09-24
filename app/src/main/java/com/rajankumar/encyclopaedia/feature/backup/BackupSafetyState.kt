package com.rajankumar.encyclopaedia.feature.backup

enum class BackupSafetyState { SAFE, DUE, OVERDUE }

fun BackupAge.safetyState(): BackupSafetyState = when (this) { BackupAge.TODAY -> BackupSafetyState.SAFE; BackupAge.RECENT -> BackupSafetyState.DUE; BackupAge.STALE -> BackupSafetyState.OVERDUE }

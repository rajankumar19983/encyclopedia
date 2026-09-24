package com.rajankumar.encyclopaedia.feature.backup

fun BackupProgress.label(): String = if (safeTotal == 0) "Preparing backup" else "$safeCompleted of $safeTotal items • $percent%"

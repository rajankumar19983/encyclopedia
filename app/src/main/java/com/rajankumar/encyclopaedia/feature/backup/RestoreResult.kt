package com.rajankumar.encyclopaedia.feature.backup

enum class RestoreResult { SUCCESS, FAILED, CANCELLED }

fun RestoreResult.label(): String = when (this) { RestoreResult.SUCCESS -> "Restore complete"; RestoreResult.FAILED -> "Restore failed"; RestoreResult.CANCELLED -> "Restore cancelled" }

package com.rajankumar.encyclopaedia.feature.performance

fun revisionProgress(completed: Int, total: Int): Float = if (total <= 0) 0f else (completed.coerceIn(0, total) / total.toFloat()).coerceIn(0f, 1f)

package com.rajankumar.encyclopaedia.feature.notebook

fun normalizeNotebookPressure(value: Float): Float = if (value.isFinite()) value.coerceIn(0f, 1f) else 0f

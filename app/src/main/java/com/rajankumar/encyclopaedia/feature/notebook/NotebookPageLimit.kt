package com.rajankumar.encyclopaedia.feature.notebook

const val NOTEBOOK_STROKE_WARNING_THRESHOLD = 5_000

fun notebookStrokeWarning(count: Int): String? = if (count.coerceAtLeast(0) >= NOTEBOOK_STROKE_WARNING_THRESHOLD) "This page is very detailed. Consider starting a new page for smoother drawing." else null

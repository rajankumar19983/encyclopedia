package com.rajankumar.encyclopaedia.feature.notebook

internal const val NOTEBOOK_MIN_PEN_WIDTH = 1f
internal const val NOTEBOOK_MAX_PEN_WIDTH = 24f
internal fun clampNotebookPenWidth(width: Float) = width.coerceIn(NOTEBOOK_MIN_PEN_WIDTH, NOTEBOOK_MAX_PEN_WIDTH)

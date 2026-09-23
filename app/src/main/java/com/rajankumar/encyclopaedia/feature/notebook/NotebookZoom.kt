package com.rajankumar.encyclopaedia.feature.notebook

internal const val NOTEBOOK_MIN_ZOOM = 0.25f
internal const val NOTEBOOK_MAX_ZOOM = 4f
internal fun clampNotebookZoom(scale: Float) = scale.coerceIn(NOTEBOOK_MIN_ZOOM, NOTEBOOK_MAX_ZOOM)

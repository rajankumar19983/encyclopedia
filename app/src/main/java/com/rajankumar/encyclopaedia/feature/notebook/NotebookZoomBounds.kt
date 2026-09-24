package com.rajankumar.encyclopaedia.feature.notebook

const val NOTEBOOK_MIN_ZOOM = 0.5f
const val NOTEBOOK_MAX_ZOOM = 4f

fun clampNotebookZoom(value: Float): Float = if (value.isFinite()) value.coerceIn(NOTEBOOK_MIN_ZOOM, NOTEBOOK_MAX_ZOOM) else 1f

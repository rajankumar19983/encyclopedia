package com.rajankumar.encyclopaedia.feature.notebook

const val NOTEBOOK_MIN_STROKE_WIDTH = 1f
const val NOTEBOOK_MAX_STROKE_WIDTH = 32f

fun sanitizeStrokeWidth(value: Float): Float = if (value.isFinite()) value.coerceIn(NOTEBOOK_MIN_STROKE_WIDTH, NOTEBOOK_MAX_STROKE_WIDTH) else 4f

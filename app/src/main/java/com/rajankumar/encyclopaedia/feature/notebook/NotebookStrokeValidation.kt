package com.rajankumar.encyclopaedia.feature.notebook

internal const val NOTEBOOK_MIN_STROKE_WIDTH = .25f
internal const val NOTEBOOK_MAX_STROKE_WIDTH = 128f

internal fun isValidStrokeWidth(width: Float): Boolean =
  width.isFinite() && width in NOTEBOOK_MIN_STROKE_WIDTH..NOTEBOOK_MAX_STROKE_WIDTH

internal fun isSupportedStrokeTool(tool: String): Boolean =
  tool.trim().uppercase() in setOf("PEN", "HIGHLIGHTER", "ERASER")

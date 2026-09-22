package com.rajankumar.encyclopaedia.feature.notebook

import androidx.compose.ui.geometry.Offset

internal data class NotebookSelectionBounds(
  val left: Float,
  val top: Float,
  val right: Float,
  val bottom: Float
) {
  val center: Offset get() = Offset((left + right) / 2f, (top + bottom) / 2f)
  val width: Float get() = right - left
  val height: Float get() = bottom - top
}

internal fun selectionBounds(
  strokes: List<CanvasStroke>,
  padding: Float = 20f
): NotebookSelectionBounds? {
  val points = strokes.flatMap { it.points }
  if (points.isEmpty()) return null
  return NotebookSelectionBounds(
    left = points.minOf { it.x } - padding,
    top = points.minOf { it.y } - padding,
    right = points.maxOf { it.x } + padding,
    bottom = points.maxOf { it.y } + padding
  )
}

internal fun scaleSelectionPoint(point: Offset, center: Offset, scale: Float): Offset =
  Offset(
    center.x + (point.x - center.x) * scale,
    center.y + (point.y - center.y) * scale
  )

package com.rajankumar.encyclopaedia.feature.notebook

import androidx.compose.ui.geometry.Offset
import kotlin.math.hypot

internal const val NOTEBOOK_SELECTION_PADDING = 20f
internal const val NOTEBOOK_SELECTION_HANDLE_RADIUS = 36f
internal const val NOTEBOOK_SELECTION_MIN_SCALE = .25f
internal const val NOTEBOOK_SELECTION_MAX_SCALE = 4f

internal data class NotebookSelectionBounds(
  val left: Float,
  val top: Float,
  val right: Float,
  val bottom: Float
) {
  val center: Offset get() = Offset((left + right) / 2f, (top + bottom) / 2f)
  val width: Float get() = right - left
  val height: Float get() = bottom - top
  val resizeHandle: Offset get() = Offset(right, bottom)

  fun contains(point: Offset): Boolean = point.x in left..right && point.y in top..bottom
}

internal fun selectionBounds(
  strokes: List<CanvasStroke>,
  padding: Float = NOTEBOOK_SELECTION_PADDING
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

internal fun isSelectionResizeHandleHit(
  point: Offset,
  bounds: NotebookSelectionBounds,
  radius: Float = NOTEBOOK_SELECTION_HANDLE_RADIUS
): Boolean = hypot(
  point.x - bounds.resizeHandle.x,
  point.y - bounds.resizeHandle.y
) <= radius

internal fun selectionScale(
  dragStart: Offset,
  current: Offset,
  center: Offset,
  minScale: Float = NOTEBOOK_SELECTION_MIN_SCALE,
  maxScale: Float = NOTEBOOK_SELECTION_MAX_SCALE
): Float {
  val startDistance = hypot(dragStart.x - center.x, dragStart.y - center.y).coerceAtLeast(1f)
  return (hypot(current.x - center.x, current.y - center.y) / startDistance)
    .coerceIn(minScale, maxScale)
}

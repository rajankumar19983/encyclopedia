package com.rajankumar.encyclopaedia.feature.notebook

import androidx.compose.ui.geometry.Offset

internal data class NotebookWorldBounds(
  val left: Float,
  val top: Float,
  val right: Float,
  val bottom: Float,
) {
  fun expanded(padding: Float): NotebookWorldBounds = NotebookWorldBounds(
    left = left - padding,
    top = top - padding,
    right = right + padding,
    bottom = bottom + padding,
  )
}

internal fun NotebookViewport.visibleWorldBounds(
  screenWidth: Float,
  screenHeight: Float,
): NotebookWorldBounds {
  val topLeft = screenToWorld(Offset.Zero)
  val bottomRight = screenToWorld(Offset(screenWidth, screenHeight))
  return NotebookWorldBounds(
    left = minOf(topLeft.x, bottomRight.x),
    top = minOf(topLeft.y, bottomRight.y),
    right = maxOf(topLeft.x, bottomRight.x),
    bottom = maxOf(topLeft.y, bottomRight.y),
  )
}

internal fun CanvasStroke.intersects(bounds: NotebookWorldBounds): Boolean {
  if (points.isEmpty()) return false
  val padding = width / 2f
  val strokeLeft = points.minOf { it.x } - padding
  val strokeTop = points.minOf { it.y } - padding
  val strokeRight = points.maxOf { it.x } + padding
  val strokeBottom = points.maxOf { it.y } + padding
  return strokeRight >= bounds.left &&
    strokeLeft <= bounds.right &&
    strokeBottom >= bounds.top &&
    strokeTop <= bounds.bottom
}

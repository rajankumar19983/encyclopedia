package com.rajankumar.encyclopaedia.feature.notebook

import androidx.compose.ui.geometry.Offset

internal data class NotebookViewport(
  val scale: Float = 1f,
  val offset: Offset = Offset.Zero,
) {
  fun screenToWorld(point: Offset): Offset = (point - offset) / scale

  fun worldToScreen(point: Offset): Offset = point * scale + offset

  fun panBy(delta: Offset): NotebookViewport = copy(offset = offset + delta)

  fun zoomAt(
    screenFocus: Offset,
    zoomFactor: Float,
    minScale: Float = MIN_SCALE,
    maxScale: Float = MAX_SCALE,
  ): NotebookViewport {
    val nextScale = (scale * zoomFactor).coerceIn(minScale, maxScale)
    if (nextScale == scale) return this
    val worldFocus = screenToWorld(screenFocus)
    return copy(
      scale = nextScale,
      offset = screenFocus - worldFocus * nextScale,
    )
  }

  companion object {
    const val MIN_SCALE = 0.5f
    const val MAX_SCALE = 4f
  }
}

package com.rajankumar.encyclopaedia.feature.notebook

import android.view.MotionEvent

internal object NotebookInputPolicy {
  fun isStylus(toolType: Int): Boolean =
    toolType == MotionEvent.TOOL_TYPE_STYLUS || toolType == MotionEvent.TOOL_TYPE_ERASER

  fun canStartStroke(palmRejection: Boolean, toolType: Int): Boolean =
    !palmRejection || isStylus(toolType)

  fun shouldStartViewportGesture(
    palmRejection: Boolean,
    pointerToolTypes: List<Int>,
  ): Boolean {
    if (pointerToolTypes.size < 2) return false
    if (!palmRejection) return true
    return pointerToolTypes.none(::isStylus)
  }
}

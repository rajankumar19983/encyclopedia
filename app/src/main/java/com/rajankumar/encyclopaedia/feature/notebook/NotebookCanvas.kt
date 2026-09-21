package com.rajankumar.encyclopaedia.feature.notebook

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter

internal data class CanvasStroke(
  val points: List<Offset>,
  val width: Float
)

@Composable
internal fun NotebookCanvas(
  strokes: List<CanvasStroke>,
  palmRejection: Boolean,
  onStrokeFinished: (CanvasStroke) -> Unit,
  modifier: Modifier = Modifier
) {
  var activePoints by remember { mutableStateOf<List<Offset>>(emptyList()) }
  var activeWidth by remember { mutableStateOf(4f) }
  var acceptingPointer by remember { mutableStateOf(false) }

  Canvas(
    modifier = modifier
      .background(Color.White)
      .pointerInteropFilter { event ->
        val actionIndex = event.actionIndex.coerceAtLeast(0)
        val tool = runCatching { event.getToolType(actionIndex) }.getOrDefault(MotionEvent.TOOL_TYPE_UNKNOWN)
        val isStylus = tool == MotionEvent.TOOL_TYPE_STYLUS || tool == MotionEvent.TOOL_TYPE_ERASER
        when (event.actionMasked) {
          MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
            acceptingPointer = !palmRejection || isStylus
            if (acceptingPointer) {
              activePoints = listOf(Offset(event.getX(actionIndex), event.getY(actionIndex)))
              val pressure = event.getPressure(actionIndex).coerceIn(0.1f, 1f)
              activeWidth = 2.5f + pressure * 4.5f
            }
          }
          MotionEvent.ACTION_MOVE -> if (acceptingPointer) {
            val index = (0 until event.pointerCount).firstOrNull {
              val t = event.getToolType(it)
              !palmRejection || t == MotionEvent.TOOL_TYPE_STYLUS || t == MotionEvent.TOOL_TYPE_ERASER
            } ?: -1
            if (index >= 0) activePoints = activePoints + Offset(event.getX(index), event.getY(index))
          }
          MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> if (acceptingPointer) {
            val finished = activePoints
            if (finished.isNotEmpty()) onStrokeFinished(CanvasStroke(finished, activeWidth))
            activePoints = emptyList()
            acceptingPointer = false
          }
          MotionEvent.ACTION_CANCEL -> {
            activePoints = emptyList()
            acceptingPointer = false
          }
        }
        acceptingPointer
      }
  ) {
    fun drawStroke(stroke: CanvasStroke) {
      if (stroke.points.isEmpty()) return
      if (stroke.points.size == 1) {
        drawCircle(Color.Black, stroke.width / 2f, stroke.points.first())
        return
      }
      val path = Path().apply {
        moveTo(stroke.points.first().x, stroke.points.first().y)
        stroke.points.drop(1).forEach { lineTo(it.x, it.y) }
      }
      drawPath(path, Color.Black, style = Stroke(width = stroke.width, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
    strokes.forEach(::drawStroke)
    drawStroke(CanvasStroke(activePoints, activeWidth))
  }
}

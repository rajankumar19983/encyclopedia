package com.rajankumar.encyclopaedia.feature.notebook

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.input.pointer.pointerInteropFilter

internal enum class NotebookTool { PEN, ERASER }

internal data class CanvasStroke(
  val points: List<Offset>,
  val width: Float,
  val tool: NotebookTool = NotebookTool.PEN
)

@Composable
internal fun NotebookCanvas(
  strokes: List<CanvasStroke>,
  palmRejection: Boolean,
  selectedTool: NotebookTool,
  onStrokeFinished: (CanvasStroke) -> Unit,
  onEraseAt: (Offset) -> Unit,
  modifier: Modifier = Modifier
) {
  var activePoints by remember { mutableStateOf<List<Offset>>(emptyList()) }
  var activeWidth by remember { mutableStateOf(4f) }
  var acceptingPointer by remember { mutableStateOf(false) }
  var eventTool by remember { mutableStateOf(selectedTool) }

  Canvas(
    modifier = modifier.background(Color.White).pointerInteropFilter { event ->
      val actionIndex = event.actionIndex.coerceAtLeast(0)
      val hardwareTool = runCatching { event.getToolType(actionIndex) }.getOrDefault(MotionEvent.TOOL_TYPE_UNKNOWN)
      val isStylus = hardwareTool == MotionEvent.TOOL_TYPE_STYLUS || hardwareTool == MotionEvent.TOOL_TYPE_ERASER
      when (event.actionMasked) {
        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
          acceptingPointer = !palmRejection || isStylus
          if (acceptingPointer) {
            eventTool = if (hardwareTool == MotionEvent.TOOL_TYPE_ERASER) NotebookTool.ERASER else selectedTool
            val point = Offset(event.getX(actionIndex), event.getY(actionIndex))
            if (eventTool == NotebookTool.ERASER) onEraseAt(point) else {
              activePoints = listOf(point)
              val pressure = event.getPressure(actionIndex).coerceIn(0.1f, 1f)
              activeWidth = 2.5f + pressure * 4.5f
            }
          }
        }
        MotionEvent.ACTION_MOVE -> if (acceptingPointer) {
          val index = (0 until event.pointerCount).firstOrNull {
            val t = event.getToolType(it)
            !palmRejection || t == MotionEvent.TOOL_TYPE_STYLUS || t == MotionEvent.TOOL_TYPE_ERASER
          } ?: -1
          if (index >= 0) {
            val point = Offset(event.getX(index), event.getY(index))
            if (eventTool == NotebookTool.ERASER) onEraseAt(point) else activePoints = activePoints + point
          }
        }
        MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> if (acceptingPointer) {
          if (eventTool == NotebookTool.PEN && activePoints.isNotEmpty()) onStrokeFinished(CanvasStroke(activePoints, activeWidth))
          activePoints = emptyList(); acceptingPointer = false
        }
        MotionEvent.ACTION_CANCEL -> { activePoints = emptyList(); acceptingPointer = false }
      }
      acceptingPointer
    }
  ) {
    fun drawStroke(stroke: CanvasStroke) {
      if (stroke.points.isEmpty() || stroke.tool != NotebookTool.PEN) return
      if (stroke.points.size == 1) { drawCircle(Color.Black, stroke.width / 2f, stroke.points.first()); return }
      val path = Path().apply { moveTo(stroke.points.first().x, stroke.points.first().y); stroke.points.drop(1).forEach { lineTo(it.x, it.y) } }
      drawPath(path, Color.Black, style = Stroke(width = stroke.width, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
    strokes.forEach(::drawStroke)
    drawStroke(CanvasStroke(activePoints, activeWidth))
  }
}

internal fun CanvasStroke.isNear(point: Offset, radius: Float = 28f): Boolean = points.any { sample ->
  val dx = sample.x - point.x
  val dy = sample.y - point.y
  dx * dx + dy * dy <= radius * radius
}

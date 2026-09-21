package com.rajankumar.encyclopaedia.feature.notebook

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
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
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

internal enum class NotebookTool { PEN, ERASER }
internal enum class NotebookBackground { PLAIN, LINED, GRID }

internal data class CanvasStroke(
  val points: List<Offset>,
  val width: Float,
  val tool: NotebookTool = NotebookTool.PEN,
  val colorArgb: Long = 0xFF111111
)

@Composable
internal fun NotebookCanvas(
  strokes: List<CanvasStroke>,
  palmRejection: Boolean,
  selectedTool: NotebookTool,
  penWidth: Float,
  penColorArgb: Long,
  background: NotebookBackground,
  onStrokeFinished: (CanvasStroke) -> Unit,
  onEraseAt: (Offset) -> Unit,
  modifier: Modifier = Modifier
) {
  var activePoints by remember { mutableStateOf<List<Offset>>(emptyList()) }
  var activeWidth by remember { mutableStateOf(penWidth) }
  var acceptingPointer by remember { mutableStateOf(false) }
  var activePointerId by remember { mutableStateOf(-1) }
  var eventTool by remember { mutableStateOf(selectedTool) }

  fun appendPoint(point: Offset) {
    val last = activePoints.lastOrNull()
    if (last == null || abs(last.x - point.x) >= 0.5f || abs(last.y - point.y) >= 0.5f) activePoints = activePoints + point
  }

  fun consumeHistoricalPoints(event: MotionEvent, pointerIndex: Int) {
    for (historyIndex in 0 until event.historySize) appendPoint(Offset(event.getHistoricalX(pointerIndex, historyIndex), event.getHistoricalY(pointerIndex, historyIndex)))
  }

  Canvas(modifier = modifier.pointerInteropFilter { event ->
    val actionIndex = event.actionIndex.coerceAtLeast(0)
    val hardwareTool = runCatching { event.getToolType(actionIndex) }.getOrDefault(MotionEvent.TOOL_TYPE_UNKNOWN)
    val isStylus = hardwareTool == MotionEvent.TOOL_TYPE_STYLUS || hardwareTool == MotionEvent.TOOL_TYPE_ERASER
    when (event.actionMasked) {
      MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> if (!acceptingPointer && (!palmRejection || isStylus)) {
        acceptingPointer = true
        activePointerId = event.getPointerId(actionIndex)
        eventTool = if (hardwareTool == MotionEvent.TOOL_TYPE_ERASER) NotebookTool.ERASER else selectedTool
        val point = Offset(event.getX(actionIndex), event.getY(actionIndex))
        if (eventTool == NotebookTool.ERASER) onEraseAt(point) else {
          activePoints = listOf(point)
          val pressure = event.getPressure(actionIndex).coerceIn(.1f, 1f)
          activeWidth = penWidth * (.65f + pressure * .7f)
        }
      }
      MotionEvent.ACTION_MOVE -> if (acceptingPointer) {
        val index = event.findPointerIndex(activePointerId)
        if (index >= 0) {
          if (eventTool == NotebookTool.ERASER) {
            for (historyIndex in 0 until event.historySize) onEraseAt(Offset(event.getHistoricalX(index, historyIndex), event.getHistoricalY(index, historyIndex)))
            onEraseAt(Offset(event.getX(index), event.getY(index)))
          } else {
            consumeHistoricalPoints(event, index)
            appendPoint(Offset(event.getX(index), event.getY(index)))
          }
        }
      }
      MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> if (acceptingPointer && event.getPointerId(actionIndex) == activePointerId) {
        if (eventTool == NotebookTool.PEN && activePoints.isNotEmpty()) onStrokeFinished(CanvasStroke(activePoints, activeWidth, colorArgb = penColorArgb))
        activePoints = emptyList(); acceptingPointer = false; activePointerId = -1
      }
      MotionEvent.ACTION_CANCEL -> { activePoints = emptyList(); acceptingPointer = false; activePointerId = -1 }
    }
    acceptingPointer
  }) {
    drawRect(Color.White)
    val guide = Color(0xFFE1E5EA); val spacing = 48f
    if (background == NotebookBackground.LINED || background == NotebookBackground.GRID) {
      var y = spacing
      while (y < size.height) { drawLine(guide, Offset(0f, y), Offset(size.width, y), 1f); y += spacing }
    }
    if (background == NotebookBackground.GRID) {
      var x = spacing
      while (x < size.width) { drawLine(guide, Offset(x, 0f), Offset(x, size.height), 1f); x += spacing }
    }
    fun drawStroke(s: CanvasStroke) {
      if (s.points.isEmpty() || s.tool != NotebookTool.PEN) return
      val color = Color(s.colorArgb.toULong())
      if (s.points.size == 1) { drawCircle(color, s.width / 2f, s.points.first()); return }
      val path = Path().apply {
        moveTo(s.points.first().x, s.points.first().y)
        if (s.points.size == 2) lineTo(s.points[1].x, s.points[1].y) else {
          for (i in 1 until s.points.lastIndex) {
            val current = s.points[i]; val next = s.points[i + 1]
            quadraticBezierTo(current.x, current.y, (current.x + next.x) / 2f, (current.y + next.y) / 2f)
          }
          lineTo(s.points.last().x, s.points.last().y)
        }
      }
      drawPath(path, color, style = Stroke(width = s.width, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
    strokes.forEach(::drawStroke)
    drawStroke(CanvasStroke(activePoints, activeWidth, colorArgb = penColorArgb))
  }
}

internal fun CanvasStroke.isNear(point: Offset, radius: Float = 28f): Boolean {
  if (points.isEmpty()) return false
  val hitRadius = radius + width / 2f
  val radiusSquared = hitRadius * hitRadius
  if (points.size == 1) return distanceSquared(points.first(), point) <= radiusSquared
  return points.zipWithNext().any { (start, end) -> pointToSegmentDistanceSquared(point, start, end) <= radiusSquared }
}

private fun distanceSquared(a: Offset, b: Offset): Float {
  val dx = a.x - b.x; val dy = a.y - b.y
  return dx * dx + dy * dy
}

private fun pointToSegmentDistanceSquared(point: Offset, start: Offset, end: Offset): Float {
  val dx = end.x - start.x; val dy = end.y - start.y
  val lengthSquared = dx * dx + dy * dy
  if (lengthSquared <= 0.0001f) return distanceSquared(point, start)
  val projection = ((point.x - start.x) * dx + (point.y - start.y) * dy) / lengthSquared
  val t = max(0f, min(1f, projection))
  val closest = Offset(start.x + t * dx, start.y + t * dy)
  return distanceSquared(point, closest)
}

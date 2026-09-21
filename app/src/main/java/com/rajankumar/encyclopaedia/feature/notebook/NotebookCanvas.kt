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
  var eventTool by remember { mutableStateOf(selectedTool) }

  Canvas(modifier = modifier.pointerInteropFilter { event ->
    val actionIndex=event.actionIndex.coerceAtLeast(0); val hardwareTool=runCatching{event.getToolType(actionIndex)}.getOrDefault(MotionEvent.TOOL_TYPE_UNKNOWN); val isStylus=hardwareTool==MotionEvent.TOOL_TYPE_STYLUS||hardwareTool==MotionEvent.TOOL_TYPE_ERASER
    when(event.actionMasked){
      MotionEvent.ACTION_DOWN,MotionEvent.ACTION_POINTER_DOWN->{acceptingPointer=!palmRejection||isStylus;if(acceptingPointer){eventTool=if(hardwareTool==MotionEvent.TOOL_TYPE_ERASER)NotebookTool.ERASER else selectedTool;val point=Offset(event.getX(actionIndex),event.getY(actionIndex));if(eventTool==NotebookTool.ERASER)onEraseAt(point)else{activePoints=listOf(point);val pressure=event.getPressure(actionIndex).coerceIn(.1f,1f);activeWidth=penWidth*(.65f+pressure*.7f)}}}
      MotionEvent.ACTION_MOVE->if(acceptingPointer){val index=(0 until event.pointerCount).firstOrNull{val t=event.getToolType(it);!palmRejection||t==MotionEvent.TOOL_TYPE_STYLUS||t==MotionEvent.TOOL_TYPE_ERASER}?:-1;if(index>=0){val p=Offset(event.getX(index),event.getY(index));if(eventTool==NotebookTool.ERASER)onEraseAt(p)else activePoints=activePoints+p}}
      MotionEvent.ACTION_UP,MotionEvent.ACTION_POINTER_UP->if(acceptingPointer){if(eventTool==NotebookTool.PEN&&activePoints.isNotEmpty())onStrokeFinished(CanvasStroke(activePoints,activeWidth,colorArgb=penColorArgb));activePoints=emptyList();acceptingPointer=false}
      MotionEvent.ACTION_CANCEL->{activePoints=emptyList();acceptingPointer=false}
    };acceptingPointer
  }) {
    drawRect(Color.White)
    val guide=Color(0xFFE1E5EA); val spacing=48f
    if(background==NotebookBackground.LINED||background==NotebookBackground.GRID){var y=spacing;while(y<size.height){drawLine(guide,Offset(0f,y),Offset(size.width,y),1f);y+=spacing}}
    if(background==NotebookBackground.GRID){var x=spacing;while(x<size.width){drawLine(guide,Offset(x,0f),Offset(x,size.height),1f);x+=spacing}}
    fun drawStroke(s:CanvasStroke){if(s.points.isEmpty()||s.tool!=NotebookTool.PEN)return;val color=Color(s.colorArgb.toULong());if(s.points.size==1){drawCircle(color,s.width/2f,s.points.first());return};val path=Path().apply{moveTo(s.points.first().x,s.points.first().y);s.points.drop(1).forEach{lineTo(it.x,it.y)}};drawPath(path,color,style=Stroke(width=s.width,cap=StrokeCap.Round,join=StrokeJoin.Round))}
    strokes.forEach(::drawStroke);drawStroke(CanvasStroke(activePoints,activeWidth,colorArgb=penColorArgb))
  }
}

internal fun CanvasStroke.isNear(point:Offset,radius:Float=28f):Boolean=points.any{val dx=it.x-point.x;val dy=it.y-point.y;dx*dx+dy*dy<=radius*radius}

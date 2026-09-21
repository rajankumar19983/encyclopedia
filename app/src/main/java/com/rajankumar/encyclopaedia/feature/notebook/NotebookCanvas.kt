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

internal enum class NotebookTool { PEN, HIGHLIGHTER, ERASER }
internal enum class NotebookBackground { PLAIN, LINED, GRID }

internal data class CanvasStroke(
  val points: List<Offset>,
  val width: Float,
  val tool: NotebookTool = NotebookTool.PEN,
  val colorArgb: Long = 0xFF111111
)

@Composable
internal fun NotebookCanvas(
  strokes: List<CanvasStroke>, palmRejection: Boolean, selectedTool: NotebookTool,
  penWidth: Float, penColorArgb: Long, background: NotebookBackground,
  onStrokeFinished: (CanvasStroke) -> Unit, onEraseAt: (Offset) -> Unit,
  modifier: Modifier = Modifier
) {
  var activePoints by remember { mutableStateOf<List<Offset>>(emptyList()) }
  var activeWidth by remember { mutableStateOf(penWidth) }
  var acceptingPointer by remember { mutableStateOf(false) }
  var activePointerId by remember { mutableStateOf(-1) }
  var eventTool by remember { mutableStateOf(selectedTool) }

  fun appendPoint(point: Offset) { val last=activePoints.lastOrNull();if(last==null||abs(last.x-point.x)>=.5f||abs(last.y-point.y)>=.5f)activePoints=activePoints+point }
  fun historical(event:MotionEvent,index:Int){for(h in 0 until event.historySize)appendPoint(Offset(event.getHistoricalX(index,h),event.getHistoricalY(index,h)))}

  Canvas(modifier=modifier.pointerInteropFilter{event->
    val ai=event.actionIndex.coerceAtLeast(0);val hardware=runCatching{event.getToolType(ai)}.getOrDefault(MotionEvent.TOOL_TYPE_UNKNOWN);val stylus=hardware==MotionEvent.TOOL_TYPE_STYLUS||hardware==MotionEvent.TOOL_TYPE_ERASER
    when(event.actionMasked){
      MotionEvent.ACTION_DOWN,MotionEvent.ACTION_POINTER_DOWN->if(!acceptingPointer&&(!palmRejection||stylus)){acceptingPointer=true;activePointerId=event.getPointerId(ai);eventTool=if(hardware==MotionEvent.TOOL_TYPE_ERASER)NotebookTool.ERASER else selectedTool;val p=Offset(event.getX(ai),event.getY(ai));if(eventTool==NotebookTool.ERASER)onEraseAt(p)else{activePoints=listOf(p);val pressure=event.getPressure(ai).coerceIn(.1f,1f);activeWidth=if(eventTool==NotebookTool.HIGHLIGHTER)penWidth*4f else penWidth*(.65f+pressure*.7f)}}
      MotionEvent.ACTION_MOVE->if(acceptingPointer){val i=event.findPointerIndex(activePointerId);if(i>=0){if(eventTool==NotebookTool.ERASER){for(h in 0 until event.historySize)onEraseAt(Offset(event.getHistoricalX(i,h),event.getHistoricalY(i,h)));onEraseAt(Offset(event.getX(i),event.getY(i)))}else{historical(event,i);appendPoint(Offset(event.getX(i),event.getY(i)))}}}
      MotionEvent.ACTION_UP,MotionEvent.ACTION_POINTER_UP->if(acceptingPointer&&event.getPointerId(ai)==activePointerId){if(eventTool!=NotebookTool.ERASER&&activePoints.isNotEmpty())onStrokeFinished(CanvasStroke(activePoints,activeWidth,eventTool,penColorArgb));activePoints=emptyList();acceptingPointer=false;activePointerId=-1}
      MotionEvent.ACTION_CANCEL->{activePoints=emptyList();acceptingPointer=false;activePointerId=-1}
    };acceptingPointer
  }){
    drawRect(Color.White);val guide=Color(0xFFE1E5EA);val spacing=48f
    if(background==NotebookBackground.LINED||background==NotebookBackground.GRID){var y=spacing;while(y<size.height){drawLine(guide,Offset(0f,y),Offset(size.width,y),1f);y+=spacing}}
    if(background==NotebookBackground.GRID){var x=spacing;while(x<size.width){drawLine(guide,Offset(x,0f),Offset(x,size.height),1f);x+=spacing}}
    fun drawStroke(s:CanvasStroke){if(s.points.isEmpty()||s.tool==NotebookTool.ERASER)return;val base=Color(s.colorArgb.toULong());val color=if(s.tool==NotebookTool.HIGHLIGHTER)base.copy(alpha=.32f)else base;if(s.points.size==1){drawCircle(color,s.width/2f,s.points.first());return};val path=Path().apply{moveTo(s.points.first().x,s.points.first().y);if(s.points.size==2)lineTo(s.points[1].x,s.points[1].y)else{for(i in 1 until s.points.lastIndex){val c=s.points[i];val n=s.points[i+1];quadraticBezierTo(c.x,c.y,(c.x+n.x)/2f,(c.y+n.y)/2f)};lineTo(s.points.last().x,s.points.last().y)}};drawPath(path,color,style=Stroke(width=s.width,cap=StrokeCap.Round,join=StrokeJoin.Round))}
    strokes.forEach(::drawStroke);if(eventTool!=NotebookTool.ERASER)drawStroke(CanvasStroke(activePoints,activeWidth,eventTool,penColorArgb))
  }
}

internal fun CanvasStroke.isNear(point:Offset,radius:Float=28f):Boolean{if(points.isEmpty())return false;val r=radius+width/2f;val r2=r*r;if(points.size==1)return distanceSquared(points.first(),point)<=r2;return points.zipWithNext().any{(a,b)->pointToSegmentDistanceSquared(point,a,b)<=r2}}
private fun distanceSquared(a:Offset,b:Offset):Float{val dx=a.x-b.x;val dy=a.y-b.y;return dx*dx+dy*dy}
private fun pointToSegmentDistanceSquared(p:Offset,a:Offset,b:Offset):Float{val dx=b.x-a.x;val dy=b.y-a.y;val len=dx*dx+dy*dy;if(len<=.0001f)return distanceSquared(p,a);val projection=((p.x-a.x)*dx+(p.y-a.y)*dy)/len;val t=max(0f,min(1f,projection));return distanceSquared(p,Offset(a.x+t*dx,a.y+t*dy))}

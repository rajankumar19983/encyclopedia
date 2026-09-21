package com.rajankumar.encyclopaedia.feature.notebook

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import kotlin.math.*

internal enum class NotebookTool { PEN, HIGHLIGHTER, ERASER, LASSO }
internal enum class NotebookBackground { PLAIN, LINED, GRID }
internal data class CanvasStroke(val points:List<Offset>,val width:Float,val tool:NotebookTool=NotebookTool.PEN,val colorArgb:Long=0xFF111111)

@Composable internal fun NotebookCanvas(strokes:List<CanvasStroke>,palmRejection:Boolean,selectedTool:NotebookTool,penWidth:Float,penColorArgb:Long,background:NotebookBackground,onStrokeFinished:(CanvasStroke)->Unit,onEraseAt:(Offset)->Unit,onLassoFinished:(List<Offset>)->Unit={},modifier:Modifier=Modifier){
  var activePoints by remember{mutableStateOf<List<Offset>>(emptyList())};var activeWidth by remember{mutableStateOf(penWidth)};var accepting by remember{mutableStateOf(false)};var pointerId by remember{mutableStateOf(-1)};var eventTool by remember{mutableStateOf(selectedTool)}
  fun append(p:Offset){val l=activePoints.lastOrNull();if(l==null||abs(l.x-p.x)>=.5f||abs(l.y-p.y)>=.5f)activePoints=activePoints+p}
  fun history(e:MotionEvent,i:Int){for(h in 0 until e.historySize)append(Offset(e.getHistoricalX(i,h),e.getHistoricalY(i,h)))}
  Canvas(modifier.pointerInteropFilter{e->val ai=e.actionIndex.coerceAtLeast(0);val hw=runCatching{e.getToolType(ai)}.getOrDefault(MotionEvent.TOOL_TYPE_UNKNOWN);val stylus=hw==MotionEvent.TOOL_TYPE_STYLUS||hw==MotionEvent.TOOL_TYPE_ERASER;when(e.actionMasked){MotionEvent.ACTION_DOWN,MotionEvent.ACTION_POINTER_DOWN->if(!accepting&&(!palmRejection||stylus)){accepting=true;pointerId=e.getPointerId(ai);eventTool=if(hw==MotionEvent.TOOL_TYPE_ERASER)NotebookTool.ERASER else selectedTool;val p=Offset(e.getX(ai),e.getY(ai));if(eventTool==NotebookTool.ERASER)onEraseAt(p)else{activePoints=listOf(p);val pressure=e.getPressure(ai).coerceIn(.1f,1f);activeWidth=if(eventTool==NotebookTool.HIGHLIGHTER)penWidth*4f else penWidth*(.65f+pressure*.7f)}};MotionEvent.ACTION_MOVE->if(accepting){val i=e.findPointerIndex(pointerId);if(i>=0){if(eventTool==NotebookTool.ERASER){for(h in 0 until e.historySize)onEraseAt(Offset(e.getHistoricalX(i,h),e.getHistoricalY(i,h)));onEraseAt(Offset(e.getX(i),e.getY(i)))}else{history(e,i);append(Offset(e.getX(i),e.getY(i)))}}};MotionEvent.ACTION_UP,MotionEvent.ACTION_POINTER_UP->if(accepting&&e.getPointerId(ai)==pointerId){when{eventTool==NotebookTool.LASSO&&activePoints.size>=3->onLassoFinished(activePoints);eventTool!=NotebookTool.ERASER&&activePoints.isNotEmpty()->onStrokeFinished(CanvasStroke(activePoints,activeWidth,eventTool,penColorArgb))};activePoints=emptyList();accepting=false;pointerId=-1};MotionEvent.ACTION_CANCEL->{activePoints=emptyList();accepting=false;pointerId=-1}};accepting}){
    drawRect(Color.White);val guide=Color(0xFFE1E5EA);val spacing=48f;if(background==NotebookBackground.LINED||background==NotebookBackground.GRID){var y=spacing;while(y<size.height){drawLine(guide,Offset(0f,y),Offset(size.width,y),1f);y+=spacing}};if(background==NotebookBackground.GRID){var x=spacing;while(x<size.width){drawLine(guide,Offset(x,0f),Offset(x,size.height),1f);x+=spacing}}
    fun drawS(s:CanvasStroke){if(s.points.isEmpty()||s.tool==NotebookTool.ERASER||s.tool==NotebookTool.LASSO)return;val base=Color(s.colorArgb.toULong());val color=if(s.tool==NotebookTool.HIGHLIGHTER)base.copy(alpha=.32f)else base;if(s.points.size==1){drawCircle(color,s.width/2,s.points.first());return};val p=Path().apply{moveTo(s.points.first().x,s.points.first().y);if(s.points.size==2)lineTo(s.points[1].x,s.points[1].y)else{for(i in 1 until s.points.lastIndex){val c=s.points[i];val n=s.points[i+1];quadraticBezierTo(c.x,c.y,(c.x+n.x)/2,(c.y+n.y)/2)};lineTo(s.points.last().x,s.points.last().y)}};drawPath(p,color,style=Stroke(s.width,cap=StrokeCap.Round,join=StrokeJoin.Round))}
    strokes.forEach(::drawS);if(eventTool==NotebookTool.LASSO&&activePoints.size>1){val p=Path().apply{moveTo(activePoints.first().x,activePoints.first().y);activePoints.drop(1).forEach{lineTo(it.x,it.y)}};drawPath(p,Color(0xFF2563EB),style=Stroke(2f,pathEffect=PathEffect.dashPathEffect(floatArrayOf(10f,8f))))}else if(eventTool!=NotebookTool.ERASER)drawS(CanvasStroke(activePoints,activeWidth,eventTool,penColorArgb))
  }
}
internal fun CanvasStroke.isNear(point:Offset,radius:Float=28f):Boolean{if(points.isEmpty())return false;val r=radius+width/2;val r2=r*r;if(points.size==1)return dist2(points.first(),point)<=r2;return points.zipWithNext().any{(a,b)->segDist2(point,a,b)<=r2}}
internal fun CanvasStroke.isInsidePolygon(polygon:List<Offset>):Boolean{if(points.isEmpty()||polygon.size<3)return false;val samples=if(points.size<=8)points else points.filterIndexed{i,_->i%(points.size/8).coerceAtLeast(1)==0};return samples.any{pointInPolygon(it,polygon)}}
private fun pointInPolygon(p:Offset,poly:List<Offset>):Boolean{var inside=false;var j=poly.lastIndex;for(i in poly.indices){val a=poly[i];val b=poly[j];if((a.y>p.y)!=(b.y>p.y)&&p.x<(b.x-a.x)*(p.y-a.y)/(b.y-a.y)+a.x)inside=!inside;j=i};return inside}
private fun dist2(a:Offset,b:Offset):Float{val x=a.x-b.x;val y=a.y-b.y;return x*x+y*y}
private fun segDist2(p:Offset,a:Offset,b:Offset):Float{val x=b.x-a.x;val y=b.y-a.y;val l=x*x+y*y;if(l<=.0001f)return dist2(p,a);val t=max(0f,min(1f,((p.x-a.x)*x+(p.y-a.y)*y)/l));return dist2(p,Offset(a.x+t*x,a.y+t*y))}

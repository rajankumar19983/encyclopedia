package com.rajankumar.encyclopaedia.feature.notebook

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookCanvasGeometryTest {
  @Test fun isNear_detectsPointNearLongSegment(){val s=CanvasStroke(listOf(Offset(0f,0f),Offset(100f,0f)),4f);assertTrue(s.isNear(Offset(50f,10f),10f))}
  @Test fun isNear_rejectsPointOutsideStrokeRadius(){val s=CanvasStroke(listOf(Offset(0f,0f),Offset(100f,0f)),4f);assertFalse(s.isNear(Offset(50f,20f),10f))}
  @Test fun isNear_accountsForStrokeWidth(){val s=CanvasStroke(listOf(Offset(0f,0f),Offset(100f,0f)),20f);assertTrue(s.isNear(Offset(50f,15f),5f))}
  @Test fun isNear_handlesSinglePointStroke(){val s=CanvasStroke(listOf(Offset(20f,20f)),6f);assertTrue(s.isNear(Offset(24f,20f),2f));assertFalse(s.isNear(Offset(30f,20f),2f))}
  @Test fun isInsidePolygon_selectsStrokeWithPointInside(){val s=CanvasStroke(listOf(Offset(0f,50f),Offset(50f,50f),Offset(100f,50f)),4f);assertTrue(s.isInsidePolygon(square()))}
  @Test fun isInsidePolygon_selectsSparseStrokeCrossingBoundary(){val s=CanvasStroke(listOf(Offset(0f,50f),Offset(100f,50f)),4f);assertTrue(s.isInsidePolygon(square()))}
  @Test fun isInsidePolygon_selectsDiagonalBoundaryCrossing(){val s=CanvasStroke(listOf(Offset(0f,0f),Offset(100f,100f)),4f);assertTrue(s.isInsidePolygon(square()))}
  @Test fun isInsidePolygon_rejectsStrokeOutsideLasso(){val s=CanvasStroke(listOf(Offset(100f,100f),Offset(140f,140f)),4f);assertFalse(s.isInsidePolygon(square()))}
  @Test fun isInsidePolygon_requiresValidLasso(){val s=CanvasStroke(listOf(Offset(10f,10f),Offset(20f,20f)),4f);assertFalse(s.isInsidePolygon(listOf(Offset(0f,0f),Offset(30f,30f))))}
  @Test fun isInsidePolygon_handlesSinglePointStroke(){val inside=CanvasStroke(listOf(Offset(50f,50f)),4f);val outside=CanvasStroke(listOf(Offset(5f,5f)),4f);assertTrue(inside.isInsidePolygon(square()));assertFalse(outside.isInsidePolygon(square()))}
  private fun square()=listOf(Offset(20f,20f),Offset(80f,20f),Offset(80f,80f),Offset(20f,80f))
}

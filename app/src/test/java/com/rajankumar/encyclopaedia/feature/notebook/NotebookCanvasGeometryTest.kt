package com.rajankumar.encyclopaedia.feature.notebook

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookCanvasGeometryTest {
  @Test
  fun isNear_detectsPointNearLongSegment() {
    val stroke = CanvasStroke(
      points = listOf(Offset(0f, 0f), Offset(100f, 0f)),
      width = 4f
    )

    assertTrue(stroke.isNear(Offset(50f, 10f), radius = 10f))
  }

  @Test
  fun isNear_rejectsPointOutsideStrokeRadius() {
    val stroke = CanvasStroke(
      points = listOf(Offset(0f, 0f), Offset(100f, 0f)),
      width = 4f
    )

    assertFalse(stroke.isNear(Offset(50f, 20f), radius = 10f))
  }

  @Test
  fun isNear_accountsForStrokeWidth() {
    val stroke = CanvasStroke(
      points = listOf(Offset(0f, 0f), Offset(100f, 0f)),
      width = 20f
    )

    assertTrue(stroke.isNear(Offset(50f, 15f), radius = 5f))
  }

  @Test
  fun isNear_handlesSinglePointStroke() {
    val stroke = CanvasStroke(
      points = listOf(Offset(20f, 20f)),
      width = 6f
    )

    assertTrue(stroke.isNear(Offset(24f, 20f), radius = 2f))
    assertFalse(stroke.isNear(Offset(30f, 20f), radius = 2f))
  }

  @Test
  fun isInsidePolygon_selectsStrokeCrossingLassoInterior() {
    val stroke = CanvasStroke(
      points = listOf(
        Offset(0f, 50f),
        Offset(25f, 50f),
        Offset(50f, 50f),
        Offset(75f, 50f),
        Offset(100f, 50f)
      ),
      width = 4f
    )
    val lasso = listOf(
      Offset(20f, 20f),
      Offset(80f, 20f),
      Offset(80f, 80f),
      Offset(20f, 80f)
    )

    assertTrue(stroke.isInsidePolygon(lasso))
  }

  @Test
  fun isInsidePolygon_rejectsStrokeOutsideLasso() {
    val stroke = CanvasStroke(
      points = listOf(Offset(100f, 100f), Offset(140f, 140f)),
      width = 4f
    )
    val lasso = listOf(
      Offset(0f, 0f),
      Offset(50f, 0f),
      Offset(50f, 50f),
      Offset(0f, 50f)
    )

    assertFalse(stroke.isInsidePolygon(lasso))
  }

  @Test
  fun isInsidePolygon_requiresValidLasso() {
    val stroke = CanvasStroke(
      points = listOf(Offset(10f, 10f), Offset(20f, 20f)),
      width = 4f
    )

    assertFalse(stroke.isInsidePolygon(listOf(Offset(0f, 0f), Offset(30f, 30f))))
  }
}

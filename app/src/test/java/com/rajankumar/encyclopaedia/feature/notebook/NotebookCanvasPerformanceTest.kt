package com.rajankumar.encyclopaedia.feature.notebook

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookCanvasPerformanceTest {
  @Test
  fun visibleBoundsRespectPanAndZoom() {
    val viewport = NotebookViewport(
      scale = 2f,
      offset = Offset(-200f, -100f),
    )

    val bounds = viewport.visibleWorldBounds(
      screenWidth = 800f,
      screenHeight = 600f,
    )

    assertEquals(100f, bounds.left, 0.0001f)
    assertEquals(50f, bounds.top, 0.0001f)
    assertEquals(500f, bounds.right, 0.0001f)
    assertEquals(350f, bounds.bottom, 0.0001f)
  }

  @Test
  fun offscreenStrokeIsCulled() {
    val visible = NotebookWorldBounds(0f, 0f, 500f, 500f)
    val stroke = CanvasStroke(
      points = listOf(Offset(900f, 900f), Offset(950f, 950f)),
      width = 4f,
    )

    assertFalse(stroke.intersects(visible))
  }

  @Test
  fun crossingStrokeRemainsVisibleEvenWhenEndpointsAreOutside() {
    val visible = NotebookWorldBounds(100f, 100f, 300f, 300f)
    val stroke = CanvasStroke(
      points = listOf(Offset(50f, 200f), Offset(350f, 200f)),
      width = 4f,
    )

    assertTrue(stroke.intersects(visible))
  }

  @Test
  fun strokeWidthKeepsEdgeStrokeVisible() {
    val visible = NotebookWorldBounds(100f, 100f, 300f, 300f)
    val stroke = CanvasStroke(
      points = listOf(Offset(305f, 150f), Offset(305f, 250f)),
      width = 12f,
    )

    assertTrue(stroke.intersects(visible))
  }

  @Test
  fun emptyStrokeNeverIntersectsViewport() {
    assertFalse(
      CanvasStroke(emptyList(), width = 4f).intersects(
        NotebookWorldBounds(0f, 0f, 100f, 100f),
      )
    )
  }
}

package com.rajankumar.encyclopaedia.feature.notebook

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookViewportTest {
  @Test
  fun screenAndWorldCoordinatesRoundTrip() {
    val viewport = NotebookViewport(scale = 2f, offset = Offset(30f, -12f))
    val world = Offset(80f, 45f)

    val screen = viewport.worldToScreen(world)

    assertOffsetEquals(world, viewport.screenToWorld(screen))
  }

  @Test
  fun panMovesViewportWithoutChangingScale() {
    val viewport = NotebookViewport(scale = 1.5f, offset = Offset(10f, 20f))

    val moved = viewport.panBy(Offset(-4f, 7f))

    assertEquals(1.5f, moved.scale, 0.0001f)
    assertOffsetEquals(Offset(6f, 27f), moved.offset)
  }

  @Test
  fun zoomKeepsFocusedWorldPointUnderFinger() {
    val viewport = NotebookViewport(scale = 1f, offset = Offset(25f, 40f))
    val focus = Offset(200f, 300f)
    val worldBefore = viewport.screenToWorld(focus)

    val zoomed = viewport.zoomAt(focus, 2f)

    assertEquals(2f, zoomed.scale, 0.0001f)
    assertOffsetEquals(focus, zoomed.worldToScreen(worldBefore))
  }

  @Test
  fun zoomIsClampedToSupportedRange() {
    val focus = Offset(100f, 100f)

    val tooSmall = NotebookViewport().zoomAt(focus, 0.01f)
    val tooLarge = NotebookViewport().zoomAt(focus, 100f)

    assertEquals(NotebookViewport.MIN_SCALE, tooSmall.scale, 0.0001f)
    assertEquals(NotebookViewport.MAX_SCALE, tooLarge.scale, 0.0001f)
  }

  @Test
  fun drawingCoordinatesStayInWorldSpaceAfterPanAndZoom() {
    val viewport = NotebookViewport(scale = 2.5f, offset = Offset(-120f, 75f))
    val storedPoint = Offset(320f, 180f)

    val touchPoint = viewport.worldToScreen(storedPoint)

    assertOffsetEquals(storedPoint, viewport.screenToWorld(touchPoint))
  }

  @Test
  fun screenDragBecomesScaleAdjustedWorldDrag() {
    val viewport = NotebookViewport(scale = 2f, offset = Offset(50f, -30f))
    val startScreen = Offset(210f, 170f)
    val endScreen = startScreen + Offset(80f, -40f)

    val worldDelta = viewport.screenToWorld(endScreen) - viewport.screenToWorld(startScreen)

    assertOffsetEquals(Offset(40f, -20f), worldDelta)
  }

  @Test
  fun panAfterZoomPreservesWorldCoordinateMapping() {
    val original = NotebookViewport(scale = 1.25f, offset = Offset(20f, 35f))
    val focus = Offset(240f, 160f)
    val worldAtFocus = original.screenToWorld(focus)
    val gestureDelta = Offset(35f, -18f)

    val moved = original.zoomAt(focus, 1.6f).panBy(gestureDelta)

    assertOffsetEquals(focus + gestureDelta, moved.worldToScreen(worldAtFocus))
  }

  private fun assertOffsetEquals(expected: Offset, actual: Offset) {
    assertEquals(expected.x, actual.x, 0.0001f)
    assertEquals(expected.y, actual.y, 0.0001f)
  }
}

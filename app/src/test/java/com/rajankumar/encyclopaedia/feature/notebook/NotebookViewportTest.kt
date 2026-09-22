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

  private fun assertOffsetEquals(expected: Offset, actual: Offset) {
    assertEquals(expected.x, actual.x, 0.0001f)
    assertEquals(expected.y, actual.y, 0.0001f)
  }
}

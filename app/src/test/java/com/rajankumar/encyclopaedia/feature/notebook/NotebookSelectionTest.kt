package com.rajankumar.encyclopaedia.feature.notebook

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookSelectionTest {
  @Test fun emptySelection_hasNoBounds() {
    assertNull(selectionBounds(emptyList()))
  }

  @Test fun boundsIncludeConfiguredPadding() {
    val strokes = listOf(
      CanvasStroke(listOf(Offset(10f, 20f), Offset(50f, 80f)), 4f),
      CanvasStroke(listOf(Offset(30f, 5f)), 2f)
    )
    assertEquals(
      NotebookSelectionBounds(-2f, -7f, 62f, 92f),
      selectionBounds(strokes, padding = 12f)
    )
  }

  @Test fun boundsCenter_isCalculatedFromExtents() {
    val bounds = NotebookSelectionBounds(10f, 20f, 50f, 80f)
    assertEquals(Offset(30f, 50f), bounds.center)
    assertEquals(40f, bounds.width)
    assertEquals(60f, bounds.height)
    assertEquals(Offset(50f, 80f), bounds.resizeHandle)
  }

  @Test fun boundsContainEdgesButRejectOutsidePoints() {
    val bounds = NotebookSelectionBounds(10f, 20f, 50f, 80f)
    assertTrue(bounds.contains(Offset(10f, 20f)))
    assertTrue(bounds.contains(Offset(50f, 80f)))
    assertFalse(bounds.contains(Offset(50.1f, 80f)))
  }

  @Test fun resizeHandleUsesHitRadius() {
    val bounds = NotebookSelectionBounds(0f, 0f, 100f, 100f)
    assertTrue(isSelectionResizeHandleHit(Offset(120f, 100f), bounds, radius = 20f))
    assertFalse(isSelectionResizeHandleHit(Offset(121f, 100f), bounds, radius = 20f))
  }

  @Test fun translatePointAppliesDelta() {
    assertEquals(Offset(17f, 4f), translateSelectionPoint(Offset(10f, 10f), Offset(7f, -6f)))
  }

  @Test fun duplicatePointUsesDefaultOffset() {
    assertEquals(Offset(38f, 48f), duplicateSelectionPoint(Offset(10f, 20f)))
  }

  @Test fun duplicatePointSupportsCustomOffset() {
    assertEquals(Offset(15f, 25f), duplicateSelectionPoint(Offset(10f, 20f), 5f))
  }

  @Test fun scalePointKeepsCenterFixed() {
    val center = Offset(20f, 20f)
    assertEquals(center, scaleSelectionPoint(center, center, 3f))
  }

  @Test fun scalePointExpandsAroundCenter() {
    assertEquals(
      Offset(30f, 10f),
      scaleSelectionPoint(Offset(20f, 15f), Offset(10f, 20f), 2f)
    )
  }

  @Test fun selectionScaleUsesRelativeDistance() {
    assertEquals(
      2f,
      selectionScale(Offset(20f, 10f), Offset(30f, 10f), Offset(10f, 10f)),
      .0001f
    )
  }

  @Test fun selectionScaleIsClamped() {
    val center = Offset.Zero
    assertEquals(.25f, selectionScale(Offset(10f, 0f), Offset.Zero, center), .0001f)
    assertEquals(4f, selectionScale(Offset(1f, 0f), Offset(100f, 0f), center), .0001f)
  }
}

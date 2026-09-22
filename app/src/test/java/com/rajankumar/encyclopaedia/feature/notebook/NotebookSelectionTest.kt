package com.rajankumar.encyclopaedia.feature.notebook

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
}

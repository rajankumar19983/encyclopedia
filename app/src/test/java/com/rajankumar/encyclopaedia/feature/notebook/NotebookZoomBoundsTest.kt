package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookZoomBoundsTest {
  @Test fun clampsZoomRange() {
    assertEquals(0.25f, clampNotebookZoom(0.1f))
    assertEquals(4f, clampNotebookZoom(8f))
    assertEquals(2f, clampNotebookZoom(2f))
  }
}

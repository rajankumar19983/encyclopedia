package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookZoomTest {
  @Test fun zoomIsClamped() {
    assertEquals(0.25f, clampNotebookZoom(0.1f))
    assertEquals(2f, clampNotebookZoom(2f))
    assertEquals(4f, clampNotebookZoom(8f))
  }
}

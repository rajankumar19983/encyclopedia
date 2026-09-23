package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookCanvasA11yTest {
  @Test fun descriptionIncludesToolCountAndPalmRejection() {
    val text = notebookCanvasDescription(NotebookTool.PEN, 2, true)
    assertTrue(text.contains("Pen selected"))
    assertTrue(text.contains("2 strokes"))
    assertTrue(text.contains("Palm rejection on"))
  }
}

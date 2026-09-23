package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookPenWidthTest {
  @Test fun widthIsClampedToUsableRange() {
    assertEquals(1f, clampNotebookPenWidth(0f))
    assertEquals(8f, clampNotebookPenWidth(8f))
    assertEquals(24f, clampNotebookPenWidth(40f))
  }
}

package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookToolLabelTest {
  @Test fun everyToolHasReadableLabel() {
    assertEquals(listOf("Pen", "Highlighter", "Eraser", "Lasso"), NotebookTool.entries.map { it.label() })
  }
}

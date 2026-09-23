package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookSelectionSummaryTest {
  @Test fun pluralizesSelectionCount() {
    assertEquals("No strokes selected", notebookSelectionSummary(0))
    assertEquals("1 stroke selected", notebookSelectionSummary(1))
    assertEquals("3 strokes selected", notebookSelectionSummary(3))
  }
}

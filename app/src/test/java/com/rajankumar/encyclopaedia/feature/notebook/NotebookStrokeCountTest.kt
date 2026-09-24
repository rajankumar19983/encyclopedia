package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookStrokeCountTest {
  @Test fun labelsStrokeCounts() { assertEquals("Blank page", notebookStrokeCountLabel(0)); assertEquals("1 stroke", notebookStrokeCountLabel(1)); assertEquals("4 strokes", notebookStrokeCountLabel(4)) }
}

package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookSelectionCountTest {
  @Test fun labelsSelection() { assertEquals("Nothing selected", notebookSelectionCountLabel(0)); assertEquals("1 stroke selected", notebookSelectionCountLabel(1)); assertEquals("4 strokes selected", notebookSelectionCountLabel(4)) }
}

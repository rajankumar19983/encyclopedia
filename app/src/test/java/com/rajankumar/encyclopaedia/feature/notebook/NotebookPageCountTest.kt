package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookPageCountTest {
  @Test fun labelsPageCounts() { assertEquals("No pages", notebookPageCountLabel(0)); assertEquals("1 page", notebookPageCountLabel(1)); assertEquals("8 pages", notebookPageCountLabel(8)) }
}

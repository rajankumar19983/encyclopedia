package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookModifiedLabelTest {
  @Test fun formatsRecentAges() { assertEquals("Just now", notebookModifiedLabel(0)); assertEquals("12 min ago", notebookModifiedLabel(12)); assertEquals("2 hr ago", notebookModifiedLabel(120)); assertEquals("2 d ago", notebookModifiedLabel(2880)) }
}

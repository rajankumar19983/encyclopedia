package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookBackgroundTest {
  @Test fun parsesBackgroundIgnoringCaseAndWhitespace() =
    assertEquals(NotebookBackground.GRID, parseNotebookBackground(" grid "))

  @Test fun acceptsAllSupportedBackgrounds() {
    listOf("PLAIN", "RULED", "GRID", "DOTS").forEach { assertTrue(isSupportedNotebookBackground(it)) }
  }

  @Test fun rejectsUnknownBackground() = assertFalse(isSupportedNotebookBackground("paper"))
}

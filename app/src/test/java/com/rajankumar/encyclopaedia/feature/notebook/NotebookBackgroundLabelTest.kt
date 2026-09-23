package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookBackgroundLabelTest {
  @Test fun backgroundsHaveReadableLabels() {
    assertEquals(listOf("Plain", "Lined", "Grid"), NotebookBackground.entries.map { it.label() })
  }
}

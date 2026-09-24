package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookPressureTest {
  @Test fun pressureIsBounded() { assertEquals(0f, normalizeNotebookPressure(-1f)); assertEquals(1f, normalizeNotebookPressure(2f)); assertEquals(0.5f, normalizeNotebookPressure(0.5f)) }
  @Test fun invalidPressureBecomesZero() = assertEquals(0f, normalizeNotebookPressure(Float.NaN))
}

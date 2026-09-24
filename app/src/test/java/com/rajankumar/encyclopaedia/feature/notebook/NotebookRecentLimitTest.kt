package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookRecentLimitTest {
  @Test fun clampsRecentLimit() { assertEquals(1, notebookRecentLimit(0)); assertEquals(6, notebookRecentLimit(6)); assertEquals(20, notebookRecentLimit(50)) }
}

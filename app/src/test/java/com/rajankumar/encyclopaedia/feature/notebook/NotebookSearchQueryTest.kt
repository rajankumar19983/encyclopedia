package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookSearchQueryTest {
  @Test fun collapsesWhitespace() = assertEquals("operating systems", normalizeNotebookSearchQuery(" operating   systems "))
  @Test fun detectsUsefulQuery() { assertFalse(hasNotebookSearchQuery("  ")); assertTrue(hasNotebookSearchQuery("dbms")) }
}

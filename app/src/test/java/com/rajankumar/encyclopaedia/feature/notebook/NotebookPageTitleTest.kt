package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class NotebookPageTitleTest {
  @Test fun normalizesWhitespace() = assertEquals("DBMS Normalization", normalizeNotebookTitle("  DBMS   Normalization "))
  @Test fun blankTitleIsInvalid() = assertFalse(isValidNotebookTitle("   "))
}

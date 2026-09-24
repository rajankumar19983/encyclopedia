package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookDuplicateTitleTest {
  @Test fun addsCopySuffix() = assertEquals("Networks copy", notebookDuplicateTitle(" Networks "))
  @Test fun blankTitleUsesFallback() = assertEquals("Untitled page copy", notebookDuplicateTitle(""))
}

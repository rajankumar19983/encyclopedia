package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookExportNameTest {
  @Test fun sanitizesExportName() = assertEquals("Operating-Systems-Notes", notebookExportName(" Operating Systems Notes "))
  @Test fun blankTitleUsesFallback() = assertEquals("notebook", notebookExportName("  "))
}

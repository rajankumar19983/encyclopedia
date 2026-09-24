package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookTitlePreviewTest {
  @Test fun blankTitleUsesFallback() = assertEquals("Untitled page", notebookTitlePreview(""))
  @Test fun longTitleIsShortened() { val result = notebookTitlePreview("A very long operating systems notebook title for competitive exam revision"); assertTrue(result.length <= NOTEBOOK_TITLE_PREVIEW_LENGTH); assertTrue(result.endsWith("…")) }
}

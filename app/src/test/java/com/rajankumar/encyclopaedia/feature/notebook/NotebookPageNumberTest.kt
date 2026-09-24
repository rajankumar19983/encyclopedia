package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookPageNumberTest {
  @Test fun formatsPagePosition() = assertEquals("Page 2 of 5", notebookPageNumberLabel(1, 5))
  @Test fun clampsInvalidPosition() = assertEquals("Page 1 of 1", notebookPageNumberLabel(-2, 0))
}

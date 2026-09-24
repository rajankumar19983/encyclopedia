package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookPageHealthTest {
  @Test fun classifiesPageDetail() { assertEquals(NotebookPageHealth.BLANK, notebookPageHealth(0)); assertEquals(NotebookPageHealth.LIGHT, notebookPageHealth(20)); assertEquals(NotebookPageHealth.DETAILED, notebookPageHealth(2_000)); assertEquals(NotebookPageHealth.VERY_DETAILED, notebookPageHealth(5_000)) }
}

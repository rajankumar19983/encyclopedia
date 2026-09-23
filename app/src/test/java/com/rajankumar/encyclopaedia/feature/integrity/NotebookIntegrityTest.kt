package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.data.local.*
import org.junit.Assert.*
import org.junit.Test

class NotebookIntegrityTest {
  @Test fun validPagePasses() = assertTrue(NotebookPageEntity("p", "Notes").hasValidNotebookPageFields())
  @Test fun invalidPageSizeFails() = assertFalse(NotebookPageEntity("p", "Notes", pageWidth = 0f).hasValidNotebookPageFields())
  @Test fun validLayerPasses() = assertTrue(NotebookLayerEntity("l", "p", "Ink").hasValidNotebookLayerFields())
  @Test fun blankStrokePointsFail() = assertFalse(NotebookStrokeEntity("s", "l", "").hasValidNotebookStrokeFields())
}

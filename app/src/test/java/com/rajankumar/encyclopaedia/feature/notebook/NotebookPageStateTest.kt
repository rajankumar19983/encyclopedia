package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookPageStateTest {
  @Test fun pageSummaryCombinesUsefulState() = assertEquals("OS Notes • 3 strokes • Saved", NotebookPageState(" OS  Notes ", 3, NotebookAutosaveStatus.SAVED).summary)
  @Test fun blankPageUsesUntitledLabel() = assertEquals("Untitled page • Blank page • Unsaved changes", NotebookPageState("", 0, NotebookAutosaveStatus.UNSAVED).summary)
}

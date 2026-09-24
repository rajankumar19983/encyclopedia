package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookAutosaveTest {
  @Test fun labelsAutosaveStates() { assertEquals("Saved", NotebookAutosaveStatus.SAVED.label()); assertEquals("Saving…", NotebookAutosaveStatus.SAVING.label()); assertEquals("Unsaved changes", NotebookAutosaveStatus.UNSAVED.label()) }
}

package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookSaveStateTest {
  @Test fun saveStatesHaveClearLabels() {
    assertEquals("Saved", NotebookSaveState.SAVED.label())
    assertEquals("Unsaved changes", NotebookSaveState.DIRTY.label())
    assertEquals("Save failed", NotebookSaveState.FAILED.label())
  }
}

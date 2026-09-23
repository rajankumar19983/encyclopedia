package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookUndoStateTest {
  @Test fun availabilityReflectsHistoryCounts() {
    assertFalse(NotebookUndoState(0, 0).canUndo)
    assertTrue(NotebookUndoState(1, 0).canUndo)
    assertTrue(NotebookUndoState(0, 2).canRedo)
  }
}

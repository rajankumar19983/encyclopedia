package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookSaveDebounceTest {
  @Test fun dirtyPageUsesDebounce() = assertEquals(750L, notebookAutosaveDelay(true))
  @Test fun cleanPageNeedsNoSaveDelay() = assertEquals(0L, notebookAutosaveDelay(false))
}

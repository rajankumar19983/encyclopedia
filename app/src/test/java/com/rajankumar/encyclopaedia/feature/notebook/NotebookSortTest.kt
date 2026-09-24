package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookSortTest {
  @Test fun labelsSortOptions() { assertEquals("Recently updated", NotebookSort.RECENT.label()); assertEquals("Oldest updated", NotebookSort.OLDEST.label()); assertEquals("Title A–Z", NotebookSort.TITLE.label()) }
}

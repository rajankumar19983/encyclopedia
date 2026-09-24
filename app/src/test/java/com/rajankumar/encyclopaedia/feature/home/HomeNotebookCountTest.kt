package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeNotebookCountTest {
  @Test fun labelsNotebookCounts() { assertEquals("No notebook pages", homeNotebookCountLabel(0)); assertEquals("1 notebook page", homeNotebookCountLabel(1)); assertEquals("9 notebook pages", homeNotebookCountLabel(9)) }
}

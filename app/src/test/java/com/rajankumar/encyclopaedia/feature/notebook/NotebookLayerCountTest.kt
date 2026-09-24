package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookLayerCountTest {
  @Test fun labelsLayerCounts() { assertEquals("No layers", notebookLayerCountLabel(0)); assertEquals("1 layer", notebookLayerCountLabel(1)); assertEquals("3 layers", notebookLayerCountLabel(3)) }
}

package com.rajankumar.encyclopaedia.feature.notebook

import com.rajankumar.encyclopaedia.data.local.NotebookLayerEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookLayerNamesTest {
  private val layers = listOf(
    NotebookLayerEntity(id="writing", pageId="page", name="Writing", sortOrder=0),
    NotebookLayerEntity(id="notes", pageId="page", name="Exam Notes", sortOrder=1)
  )

  @Test fun whitespace_isNormalized() {
    assertEquals("Exam Notes", normalizedLayerName("  Exam   Notes  "))
  }

  @Test fun blankName_isRejected() {
    assertFalse(isLayerNameAvailable(layers, "   "))
  }

  @Test fun duplicateName_isRejectedIgnoringCase() {
    assertFalse(isLayerNameAvailable(layers, "writing"))
  }

  @Test fun duplicateName_isRejectedAfterWhitespaceNormalization() {
    assertFalse(isLayerNameAvailable(layers, " Exam    Notes "))
  }

  @Test fun currentLayerName_isAllowedDuringRename() {
    assertTrue(isLayerNameAvailable(layers, "writing", excludingLayerId="writing"))
  }

  @Test fun distinctName_isAllowed() {
    assertTrue(isLayerNameAvailable(layers, "Diagrams"))
  }
}

package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class ImportOptionPreviewTest {
  @Test fun labelsVariableOptionListsWithoutAssumingFourChoices() {
    val draft = EditableImportDraft("Choose", listOf("One", "Two", "Three", "Four", "Five", "Six"), "F")
    val preview = draft.optionPreview()
    assertEquals(6, preview.size)
    assertEquals("F", preview.last().label)
    assertEquals("Six", preview.last().text)
  }
}

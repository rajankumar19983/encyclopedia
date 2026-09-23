package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EditableImportValidationTest {
  @Test fun cleanEditedDraftCanSave() {
    val result = EditableImportDraft("What is RAM?", listOf("Memory", "Storage"), "1").validateForSave()
    assertTrue(result.canSave)
    assertEquals("A", result.normalizedAnswer)
  }

  @Test fun editedDevanagariCannotSave() {
    val result = EditableImportDraft("What is RAM? रैम", listOf("Memory", "Storage"), "A").validateForSave()
    assertFalse(result.canSave)
    assertTrue(result.issues.any { it.contains("Devanagari") })
  }
}

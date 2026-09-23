package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EditableImportOptionLimitTest {
  @Test fun sixOptionsCanSaveWhenOtherwiseValid() {
    val draft = EditableImportDraft("Choose the correct protocol from these options", List(6) { "Option ${it + 1}" }, "F")
    assertTrue(draft.validateForSave().canSave)
  }

  @Test fun sevenOptionsCannotSave() {
    val draft = EditableImportDraft("Choose the correct protocol from these options", List(7) { "Option ${it + 1}" }, "A")
    assertFalse(draft.validateForSave().canSave)
  }
}

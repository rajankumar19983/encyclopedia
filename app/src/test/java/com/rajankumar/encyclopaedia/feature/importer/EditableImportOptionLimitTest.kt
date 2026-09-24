package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EditableImportOptionLimitTest {
  @Test fun fourOptionsCanSaveWhenOtherwiseValid() {
    val draft = EditableImportDraft("Choose the correct protocol from these options", List(4) { "Option ${it + 1}" }, "D")
    assertTrue(draft.validateForSave().canSave)
  }

  @Test fun sixOptionsCanSaveWhenOtherwiseValid() {
    val draft = EditableImportDraft("Choose the correct protocol from these options", List(6) { "Option ${it + 1}" }, "F")
    assertTrue(draft.validateForSave().canSave)
  }

  @Test fun sevenOptionsAreRejectedByEditableImport() {
    val draft = EditableImportDraft("Choose the correct protocol from these options", List(7) { "Option ${it + 1}" }, "A")
    assertFalse(draft.validateForSave().canSave)
  }
}

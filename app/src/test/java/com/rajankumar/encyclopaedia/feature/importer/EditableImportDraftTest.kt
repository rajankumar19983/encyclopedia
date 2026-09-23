package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EditableImportDraftTest {
  @Test fun supportsMoreThanSixOptions() {
    val draft = EditableImportDraft("Choose one", listOf("A", "B", "C", "D", "E", "F", "G"), "G")
    assertTrue(draft.isValid())
  }

  @Test fun rejectsMissingOrOutOfRangeAnswer() {
    assertFalse(EditableImportDraft("Choose one", listOf("A", "B"), "C").isValid())
    assertFalse(EditableImportDraft("", listOf("A", "B"), "A").isValid())
  }
}

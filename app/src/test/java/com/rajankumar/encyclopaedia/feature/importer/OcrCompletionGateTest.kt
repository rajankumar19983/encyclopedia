package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrCompletionGateTest {
  @Test fun fourToSixOptionsAreAccepted() {
    for (count in 4..6) {
      val draft = EditableImportDraft("Which option correctly answers this printed question?", List(count) { "Option ${it + 1}" }, "A")
      assertTrue("Expected $count options to be accepted", draft.validateForSave().canSave)
    }
  }

  @Test fun fewerThanFourOptionsAreRejected() {
    val draft = EditableImportDraft("Which option correctly answers this printed question?", listOf("One", "Two", "Three"), "A")
    assertFalse(draft.validateForSave().canSave)
  }

  @Test fun moreThanSixOptionsAreRejected() {
    val draft = EditableImportDraft("Which option correctly answers this printed question?", List(7) { "Option ${it + 1}" }, "A")
    assertFalse(draft.validateForSave().canSave)
  }

  @Test fun devanagariCannotPassSaveGate() {
    val draft = EditableImportDraft("यह printed question must be reviewed", listOf("One", "Two", "Three", "Four"), "A")
    assertFalse(draft.validateForSave().canSave)
  }
}

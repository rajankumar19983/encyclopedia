package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrApprovalGateTest {
  @Test fun validDraftStillRequiresChecklist() {
    val validation = EditableImportDraft("Valid question?", listOf("One", "Two"), "A").validateForSave()
    assertFalse(evaluateOcrApproval(validation, OcrReviewChecklistState()).allowed)
  }

  @Test fun validDraftWithRequiredChecksCanBeApproved() {
    val validation = EditableImportDraft("Valid question?", listOf("One", "Two"), "A").validateForSave()
    val items = ocrReviewChecklist()
    var state = OcrReviewChecklistState()
    items.indices.filter { items[it].required }.forEach { state = state.toggle(it) }
    assertTrue(evaluateOcrApproval(validation, state, items).allowed)
  }
}

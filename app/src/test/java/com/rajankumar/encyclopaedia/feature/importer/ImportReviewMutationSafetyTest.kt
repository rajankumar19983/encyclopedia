package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ImportReviewMutationSafetyTest {
  private val parsed = ParsedQuestionDraft("Original question", listOf("One", "Two"), "A")
  private val metadata = OcrSourceMetadata(examName = "DSSSB", year = 2024)
  private val checked = OcrReviewChecklistState(ocrReviewChecklist().indices.toSet())

  @Test
  fun editingQuestionInvalidatesAllPriorVerification() {
    val draft = pendingDraft().updateReviewedContent { it.copy(question = "Corrected question") }

    assertEquals("Corrected question", draft.edit.question)
    assertTrue(draft.review.checklist.checked.isEmpty())
    assertEquals(OcrDraftSaveStatus.READY, draft.review.saveStatus)
  }

  @Test
  fun editingSourceInvalidatesOnlySourceVerification() {
    val draft = pendingDraft().updateReviewedSource("DSSSB TGT CS • 2023")

    assertFalse(OCR_SOURCE_CHECKLIST_INDEX in draft.review.checklist.checked)
    assertEquals(setOf(0, 1, 2, 4), draft.review.checklist.checked)
  }

  @Test
  fun savingAndResolvedDraftsCannotBeEdited() {
    val saving = pendingDraft().copy(
      review = pendingDraft().review.copy(saveStatus = OcrDraftSaveStatus.SAVING),
    )
    val approved = pendingDraft().copy(
      review = pendingDraft().review.copy(decision = OcrReviewDecision.APPROVED),
    )

    assertEquals("Original question", saving.updateReviewedContent { it.copy(question = "Changed") }.edit.question)
    assertEquals("Original question", approved.updateReviewedContent { it.copy(question = "Changed") }.edit.question)
    assertFalse(saving.editable)
    assertFalse(approved.editable)
  }

  @Test
  fun pendingDraftRemainsEditable() {
    assertTrue(pendingDraft().editable)
  }

  @Test
  fun rejectedDraftCanBeRestoredWithoutLosingCorrections() {
    val rejected = pendingDraft().copy(
      edit = pendingDraft().edit.copy(question = "Corrected before rejection"),
      review = pendingDraft().review.copy(
        decision = OcrReviewDecision.REJECTED,
        saveStatus = OcrDraftSaveStatus.FAILED,
      ),
    )

    val restored = rejected.restoreForReview()

    assertEquals(OcrReviewDecision.PENDING, restored.review.decision)
    assertEquals(OcrDraftSaveStatus.READY, restored.review.saveStatus)
    assertEquals("Corrected before rejection", restored.edit.question)
    assertTrue(restored.editable)
  }

  private fun pendingDraft() = ReviewDraft(
    parsed = parsed,
    source = "PDF",
    metadata = metadata,
    review = OcrDraftReviewState(
      checklist = checked,
      saveStatus = OcrDraftSaveStatus.FAILED,
    ),
  )
}

package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class ImportReviewDraftStateTest {
  private val metadata = OcrSourceMetadata(null, null, null, null)

  @Test
  fun editedContentIsOwnedByDraftState() {
    val original = ReviewDraft(
      parsed = ParsedQuestionDraft("Original question", listOf("One", "Two"), "A"),
      source = "SCAN",
      metadata = metadata,
    )

    val updated = listOf(original).updateDraft(0) {
      it.copy(edit = it.edit.copy(
        question = "Corrected question",
        optionsText = "First\nSecond\nThird",
        answer = "C",
      ))
    }.single()

    assertEquals("Corrected question", updated.edit.question)
    assertEquals(listOf("First", "Second", "Third"), updated.edit.editable().cleanedOptions)
    assertEquals("C", updated.edit.answer)
  }

  @Test
  fun updatingOneDraftDoesNotChangeItsNeighbors() {
    val drafts = listOf(
      ReviewDraft(ParsedQuestionDraft("First question", listOf("A", "B")), "SCAN", metadata),
      ReviewDraft(ParsedQuestionDraft("Second question", listOf("C", "D")), "SCAN", metadata),
    )

    val updated = drafts.updateDraft(1) {
      it.copy(edit = it.edit.copy(question = "Edited second question"))
    }

    assertEquals("First question", updated[0].edit.question)
    assertEquals("Edited second question", updated[1].edit.question)
  }
}

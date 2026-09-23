package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrDraftEditStateTest {
  @Test fun parsedDraftBecomesEditableWithoutLosingOptions() {
    val state = ParsedQuestionDraft("Question?", listOf("One", "Two", "Three"), "3").toEditState()
    assertEquals(3, state.editable().cleanedOptions.size)
    assertTrue(state.validation().canSave)
    assertEquals("C", state.validation().normalizedAnswer)
  }
}

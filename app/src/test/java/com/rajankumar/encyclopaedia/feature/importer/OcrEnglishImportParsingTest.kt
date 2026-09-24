package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrEnglishImportParsingTest {
  @Test fun hindiContentNeverReachesParsedDraft() {
    val result = parseEnglishOcrQuestions("1. Which is volatile memory? अस्थिर मेमोरी\nA. RAM रैम\nB. ROM रोम\nC. SSD\nD. HDD\nAnswer: A")
    assertTrue(result.removedDevanagari)
    val draft = result.drafts.single()
    assertFalse(OcrEnglishTextFilter.containsDevanagari(draft.questionText))
    assertTrue(draft.options.none(OcrEnglishTextFilter::containsDevanagari))
    assertEquals("A", draft.correctAnswer)
  }
}

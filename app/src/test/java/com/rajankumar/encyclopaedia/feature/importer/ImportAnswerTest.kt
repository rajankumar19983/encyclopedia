package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ImportAnswerTest {
  @Test fun acceptsLetterAndNumericAnswers() {
    assertEquals(6, importedAnswerIndex("G", 8))
    assertEquals(6, importedAnswerIndex("7", 8))
    assertEquals("G", normalizedImportedAnswer("7", 8))
  }

  @Test fun rejectsAnswersOutsideAvailableOptions() {
    assertNull(importedAnswerIndex("G", 6))
    assertNull(importedAnswerIndex("0", 6))
    assertNull(importedAnswerIndex("AA", 6))
  }
}

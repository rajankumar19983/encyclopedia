package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrImportOutcomeTest {
  @Test fun duplicatesAreExcludedFromSavedCount() {
    val outcome = OcrImportOutcome(approved = 5, rejected = 2, duplicates = 1)
    assertEquals(4, outcome.saved)
    assertTrue(outcome.message().contains("4 saved"))
  }
}

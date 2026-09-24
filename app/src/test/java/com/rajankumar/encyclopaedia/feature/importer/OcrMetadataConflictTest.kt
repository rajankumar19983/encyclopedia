package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrMetadataConflictTest {
  @Test fun conflictingExamHeadersAreFlaggedInsteadOfSilentlyChoosingOne() {
    val conflicts = detectMetadataConflicts(listOf(
      OcrPageText(1, "DSSSB 2025"),
      OcrPageText(2, "BPSC 2025")
    ))
    val exam = conflicts.single { it.field == "exam" }
    assertEquals(setOf("DSSSB", "BPSC"), exam.values)
  }

  @Test fun matchingMetadataHasNoConflict() {
    val conflicts = detectMetadataConflicts(listOf(
      OcrPageText(1, "DSSSB 2025"),
      OcrPageText(2, "DSSSB questions")
    ))
    assertTrue(conflicts.isEmpty())
  }
}

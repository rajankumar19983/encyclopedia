package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrImportPreparationTest {
  @Test fun preparesEnglishDraftsWhileKeepingRemovalAudit() {
    val raw = "DSSSB 2024\n1. What is DNS?\nउत्तर डीएनएस\nA. Domain Name System\nB. Data Name Service\nAnswer: A"
    val prepared = prepareOcrImport(raw)
    assertEquals("DSSSB", prepared.metadata.examName)
    assertEquals(2024, prepared.metadata.year)
    assertTrue(prepared.sanitized.removedLines.any { it.reason == "contains Devanagari" })
    assertFalse(prepared.sanitized.text.contains("उत्तर"))
    assertEquals(1, prepared.drafts.size)
  }
}

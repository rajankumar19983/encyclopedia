package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrImportSessionTest {
  @Test fun sessionUsesEnglishParserAndCarriesExamMetadata() {
    val session = prepareOcrImportSession("DSSSB TGT Computer Science\n1. Which memory is volatile? अस्थिर\nA. RAM रैम\nB. ROM\nAnswer: A")
    assertEquals("DSSSB TGT Computer Science", session.examName)
    assertTrue(session.removedDevanagari)
    assertFalse(session.drafts.single().languageAudit().questionHasDevanagari)
  }
}

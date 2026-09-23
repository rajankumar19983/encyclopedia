package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrDraftAuditTest {
  @Test fun auditPreservesSixOptionQuestionAndFlagsAnswerConflict() {
    val draft = ParsedQuestionDraft("Choose one", List(6) { "Option $it" }, "F")
    val audit = auditOcrDraft(draft, listOf("Answer: F", "Correct answer: C"))
    assertEquals(OcrOptionCountQuality.NORMAL, audit.optionCountQuality)
    assertTrue(audit.answerConflict.hasConflict)
    assertTrue(audit.warnings.any { it.contains("Conflicting printed answer") })
  }
}

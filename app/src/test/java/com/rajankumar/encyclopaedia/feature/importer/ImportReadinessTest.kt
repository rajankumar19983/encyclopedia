package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class ImportReadinessTest {
  @Test fun uncertainDraftRequiresAttention() {
    val draft = ParsedQuestionDraft("What is RAM?", listOf("Memory", "Storage"), warnings = listOf("Correct answer was not confidently detected."))
    assertEquals(ImportReadiness.NEEDS_ATTENTION, draft.importReadiness())
  }

  @Test fun completeCleanDraftIsReadyForReviewNotAutoImport() {
    val draft = ParsedQuestionDraft("What is RAM?", listOf("Memory", "Storage"), correctAnswer = "A")
    assertEquals(ImportReadiness.READY_FOR_REVIEW, draft.importReadiness())
  }

  @Test fun devanagariDraftRequiresAttentionEvenWhenStructurallyComplete() {
    val draft = ParsedQuestionDraft("What is RAM? रैम", listOf("Memory", "Storage"), correctAnswer = "A")
    assertEquals(ImportReadiness.NEEDS_ATTENTION, draft.importReadiness())
  }
}

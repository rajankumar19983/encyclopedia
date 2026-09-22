package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class OcrReviewSummaryTest {
  @Test
  fun blocksBatchConfirmationWhenAnyDraftNeedsAttention() {
    val drafts = listOf(
      ParsedQuestionDraft("Question one?", listOf("A1", "B1"), "A"),
      ParsedQuestionDraft("Question two?", listOf("A2", "B2"), null, warnings = listOf("Answer missing"))
    )
    val summary = drafts.reviewSummary()
    assertEquals(2, summary.total)
    assertEquals(1, summary.ready)
    assertEquals(1, summary.needsAttention)
    assertFalse(summary.canConfirmAll)
  }
}

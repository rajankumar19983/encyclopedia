package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrImportReadinessSummaryTest {
  @Test fun countsReadyAndAttentionDrafts() {
    val drafts = listOf(
      ParsedQuestionDraft("Valid?", listOf("Yes", "No"), "A"),
      ParsedQuestionDraft("Missing answer?", listOf("Yes", "No")),
    )
    val summary = summarizeImportReadiness(drafts)
    assertEquals(2, summary.total)
    assertEquals(1, summary.ready)
    assertEquals(1, summary.needsAttention)
  }
}

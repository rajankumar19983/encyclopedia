package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertTrue
import org.junit.Test

class OcrDraftQualityTest {
  @Test fun flagsDevanagariInQuestion() {
    val draft = ParsedQuestionDraft("What is RAM? रैम", listOf("Memory", "Storage")).withQualityWarnings()
    assertTrue(draft.warnings.any { it.contains("Devanagari") })
  }

  @Test fun flagsSuspiciousOptionWithoutDiscardingIt() {
    val draft = ParsedQuestionDraft("Which option is correct?", listOf("Alph\uFFFD", "Beta")).withQualityWarnings()
    assertTrue(draft.options.size == 2)
    assertTrue(draft.warnings.any { it.contains("Option A") })
  }
}

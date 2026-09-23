package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class OcrDraftFingerprintTest {
  @Test fun ignoresCaseSpacingAndPunctuation() {
    val first = ParsedQuestionDraft("What is RAM?", listOf("Random Access Memory", "ROM"))
    val second = ParsedQuestionDraft(" WHAT IS RAM ", listOf("random-access memory", "rom"))
    assertEquals(first.fingerprint(), second.fingerprint())
  }

  @Test fun optionChangesAlterFingerprint() {
    val first = ParsedQuestionDraft("Question", listOf("One", "Two"))
    val second = ParsedQuestionDraft("Question", listOf("One", "Three"))
    assertNotEquals(first.fingerprint(), second.fingerprint())
  }
}

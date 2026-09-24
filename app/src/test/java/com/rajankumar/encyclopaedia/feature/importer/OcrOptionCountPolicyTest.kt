package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrOptionCountPolicyTest {
  @Test fun fiveAndSixOptionsAreSupported() {
    assertTrue(ParsedQuestionDraft("Question text", List(5) { "Option $it" }).optionCountPolicy().supported)
    assertTrue(ParsedQuestionDraft("Question text", List(6) { "Option $it" }).optionCountPolicy().supported)
  }

  @Test fun sevenOptionsRequireReviewInsteadOfSilentAcceptance() {
    assertFalse(ParsedQuestionDraft("Question text", List(7) { "Option $it" }).optionCountPolicy().supported)
  }
}

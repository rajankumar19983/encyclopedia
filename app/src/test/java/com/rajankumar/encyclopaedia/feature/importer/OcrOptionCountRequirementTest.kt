package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrOptionCountRequirementTest {
  @Test fun supportsFourFiveAndSixOptions() {
    for (count in 4..6) {
      val draft = ParsedQuestionDraft("Valid question text", List(count) { "Option $it" })
      assertTrue("$count options should be supported", draft.optionCountPolicy().supported)
    }
  }

  @Test fun rejectsMoreThanSixOptions() {
    val draft = ParsedQuestionDraft("Valid question text", List(7) { "Option $it" })
    assertFalse(draft.optionCountPolicy().supported)
  }
}

package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrImportPolicyTest {
  @Test fun cleanReviewedDraftCanBeConfirmed() {
    val draft = ParsedQuestionDraft("Which protocol resolves domain names?", listOf("DNS", "HTTP", "FTP"), "A")
    assertTrue(draft.importPolicy().canConfirm)
  }

  @Test fun uncertainDraftCannotBeSilentlyConfirmed() {
    val draft = ParsedQuestionDraft("Which protocol?", listOf("DNS", "HTTP"))
    assertFalse(draft.importPolicy().canConfirm)
  }

  @Test fun devanagariBlocksConfirmation() {
    val draft = ParsedQuestionDraft("Which protocol? प्रश्न", listOf("DNS", "HTTP"), "A")
    assertFalse(draft.importPolicy().canConfirm)
  }
}

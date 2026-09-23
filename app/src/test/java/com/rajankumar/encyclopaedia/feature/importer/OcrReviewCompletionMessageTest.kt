package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertTrue
import org.junit.Test

class OcrReviewCompletionMessageTest {
  @Test fun pendingMessageUsesOneBasedPositions() {
    val message = OcrReviewCompletion(false, listOf(0, 3)).message()
    assertTrue(message.contains("1, 4"))
  }
}

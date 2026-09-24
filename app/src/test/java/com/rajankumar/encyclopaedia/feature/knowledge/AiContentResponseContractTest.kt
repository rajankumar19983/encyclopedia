package com.rajankumar.encyclopaedia.feature.knowledge

import org.junit.Assert.assertTrue
import org.junit.Test

class AiContentResponseContractTest {
  @Test
  fun contractRequiresStructuredReviewableEnglishContent() {
    val prompt = AiContentResponseContract.promptFor(AiContentRequest("Operating Systems"))

    assertTrue(prompt.contains("Return only one JSON object"))
    assertTrue(prompt.contains("children recursively"))
    assertTrue(prompt.contains("do not claim content is a PYQ"))
    assertTrue(prompt.contains("use English", ignoreCase = true))
    assertTrue(prompt.contains("Operating Systems"))
  }
}

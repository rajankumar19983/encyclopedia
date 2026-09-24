package com.rajankumar.encyclopaedia.feature.knowledge

import org.junit.Assert.assertTrue
import org.junit.Test

class AiContentPromptBuilderTest {
  @Test
  fun promptContainsTopicAndReviewConstraint() {
    val prompt = AiContentPromptBuilder.build(
      AiContentRequest(topic = "Computer Networks", depth = AiContentDepth.DEEP)
    )

    assertTrue(prompt.contains("Computer Networks"))
    assertTrue(prompt.contains("reviewed before it is saved"))
    assertTrue(prompt.contains("in-depth", ignoreCase = true))
  }
}

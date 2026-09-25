package com.rajankumar.encyclopaedia.feature.knowledge

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AiContentPromptDepthTest {
  @Test
  fun `quick depth asks for compact coverage`() {
    val prompt = AiContentPromptBuilder.build(
      AiContentRequest(topic = "Operating systems", depth = AiContentDepth.QUICK),
    )

    assertTrue(prompt.contains("compact outline"))
  }

  @Test
  fun `deep depth asks for advanced and confused concepts`() {
    val prompt = AiContentPromptBuilder.build(
      AiContentRequest(topic = "DBMS", depth = AiContentDepth.DEEP),
    )

    assertTrue(prompt.contains("advanced and easily confused concepts"))
  }

  @Test
  fun `lessons can be explicitly excluded`() {
    val prompt = AiContentPromptBuilder.build(
      AiContentRequest(topic = "Networks", includeLessons = false),
    )

    assertTrue(prompt.contains("Do not generate lesson bodies"))
    assertFalse(prompt.contains("include concise but complete permanent lesson content"))
  }

  @Test
  fun `topic is trimmed before prompt interpolation`() {
    val prompt = AiContentPromptBuilder.build(AiContentRequest(topic = "  Algorithms  "))

    assertTrue(prompt.contains("Build structured study content for: Algorithms"))
    assertFalse(prompt.contains("for:   Algorithms"))
  }
}

package com.rajankumar.encyclopaedia.feature.teacher

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OpenAiTeacherResponderTest {
  @Test
  fun modelResolverProvidesFallbackOrderForResponder() {
    val resolver = OpenAiModelResolver()
    val available = listOf(
      OpenAiModel("gpt-next-mini"),
      OpenAiModel("gpt-next")
    )

    assertEquals(
      listOf("gpt-next", "gpt-next-mini"),
      resolver.candidates(available)
    )
  }

  @Test
  fun unavailableSavedModelFallsBackInsteadOfBecomingVersionError() {
    val resolver = OpenAiModelResolver()
    val available = listOf(OpenAiModel("gpt-new-generation"))

    val candidates = resolver.candidates(available, "gpt-old-retired")

    assertEquals(listOf("gpt-new-generation"), candidates)
    assertTrue("gpt-old-retired" !in candidates)
  }
}

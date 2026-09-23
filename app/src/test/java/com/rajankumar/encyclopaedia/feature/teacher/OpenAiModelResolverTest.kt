package com.rajankumar.encyclopaedia.feature.teacher

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class OpenAiModelResolverTest {
  private val resolver = OpenAiModelResolver()

  @Test
  fun automaticModeDoesNotDependOnHardcodedModelVersion() {
    val models = listOf(
      OpenAiModel("gpt-future-mini"),
      OpenAiModel("gpt-future"),
      OpenAiModel("legacy-model", supportsResponses = false)
    )

    val candidates = resolver.candidates(models)

    assertEquals("gpt-future", candidates.first())
    assertFalse(candidates.contains("legacy-model"))
  }

  @Test
  fun explicitAvailablePreferenceIsTriedFirstWithFallbacks() {
    val models = listOf(OpenAiModel("gpt-new"), OpenAiModel("gpt-new-mini"))

    assertEquals(
      listOf("gpt-new-mini", "gpt-new"),
      resolver.candidates(models, "gpt-new-mini")
    )
  }

  @Test
  fun retiredPreferenceFallsBackToCurrentlyAvailableModels() {
    val models = listOf(OpenAiModel("gpt-next"), OpenAiModel("gpt-next-mini"))

    val candidates = resolver.candidates(models, "gpt-retired")

    assertEquals("gpt-next", candidates.first())
    assertFalse(candidates.contains("gpt-retired"))
  }
}

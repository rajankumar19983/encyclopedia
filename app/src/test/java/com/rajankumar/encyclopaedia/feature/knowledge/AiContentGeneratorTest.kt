package com.rajankumar.encyclopaedia.feature.knowledge

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AiContentGeneratorTest {
  @Test
  fun parsesSuccessfulProviderResponse() = runBlocking {
    val provider = AiProvider { AiGenerationResult.Success("{\"title\":\"Networks\",\"lessons\":[],\"children\":[]}") }
    val result = AiContentGenerator(provider).generate("Create a Networks topic")
    assertTrue(result is AiContentGenerationResult.Success)
    assertEquals("Networks", (result as AiContentGenerationResult.Success).proposal.root.title)
  }

  @Test
  fun requestGenerationUsesStructuredResponseContract() = runBlocking {
    var capturedRequest: AiGenerationRequest? = null
    val provider = AiProvider { request ->
      capturedRequest = request
      AiGenerationResult.Success("{\"root\":{\"title\":\"Operating Systems\",\"lessons\":[],\"children\":[]}}")
    }

    val result = AiContentGenerator(provider).generate(
      AiContentRequest(
        topic = "Operating Systems",
        depth = AiContentDepth.DEEP,
        includeLessons = true,
      ),
    )

    assertTrue(result is AiContentGenerationResult.Success)
    val prompt = capturedRequest?.prompt.orEmpty()
    assertTrue(prompt.contains("Return only one JSON object"))
    assertTrue(prompt.contains("\"root\""))
    assertTrue(prompt.contains("children recursively"))
    assertTrue(prompt.contains("advanced and easily confused concepts"))
    assertTrue(prompt.contains("include concise but complete permanent lesson content"))
  }

  @Test
  fun preservesProviderFailure() = runBlocking {
    val provider = AiProvider { AiGenerationResult.Failure(AiProviderError.RateLimited) }
    val result = AiContentGenerator(provider).generate("Create DBMS")
    assertTrue(result is AiContentGenerationResult.ProviderFailure)
  }

  @Test
  fun rejectsBlankPromptBeforeProviderCall() = runBlocking {
    var called = false
    val provider = AiProvider {
      called = true
      AiGenerationResult.Success("{}")
    }
    val result = AiContentGenerator(provider).generate("   ")
    assertTrue(result is AiContentGenerationResult.Failure)
    assertFalse(called)
  }
}

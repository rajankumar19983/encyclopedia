package com.rajankumar.encyclopaedia.feature.knowledge

import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OpenAiRequestFactoryTest {
  @Test
  fun `request targets Responses API with bearer authorization`() {
    val request = OpenAiRequestFactory.create(
      apiKey = "test-key",
      request = AiGenerationRequest("Explain paging", "test-model"),
    )

    assertEquals("https://api.openai.com/v1/responses", request.url.toString())
    assertEquals("Bearer test-key", request.header("Authorization"))
    assertEquals("application/json", request.header("Content-Type"))
    assertEquals("POST", request.method)
  }

  @Test
  fun `request body contains selected model and prompt`() {
    val request = OpenAiRequestFactory.create(
      apiKey = "test-key",
      request = AiGenerationRequest("Explain normalization", "test-model"),
    )
    val buffer = okio.Buffer()
    request.body!!.writeTo(buffer)
    val json = JSONObject(buffer.readUtf8())

    assertEquals("test-model", json.getString("model"))
    assertEquals("Explain normalization", json.getString("input"))
    assertTrue(request.body!!.contentType().toString().startsWith("application/json"))
  }
}

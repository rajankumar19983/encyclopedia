package com.rajankumar.encyclopaedia.feature.knowledge

import org.junit.Assert.assertEquals
import org.junit.Test

class AiProviderErrorMessageTest {
  @Test
  fun `invalid response preserves provider detail`() {
    val error = AiProviderError.InvalidResponse("Response contained no output text")

    assertEquals("Response contained no output text", error.message)
  }

  @Test
  fun `network and http errors preserve diagnostic detail`() {
    val network = AiProviderError.Network("connection reset")
    val http = AiProviderError.Http(500, "server unavailable")

    assertEquals("connection reset", network.message)
    assertEquals(500, http.statusCode)
    assertEquals("server unavailable", http.message)
  }
}

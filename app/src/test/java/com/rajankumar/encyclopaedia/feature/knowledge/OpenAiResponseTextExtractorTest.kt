package com.rajankumar.encyclopaedia.feature.knowledge

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class OpenAiResponseTextExtractorTest {
  @Test
  fun extractsOutputText() {
    val raw = """{"output":[{"content":[{"type":"output_text","text":"{\\"title\\":\\"DBMS\\"}"}]}]}"""
    assertEquals("{\\"title\\":\\"DBMS\\"}", OpenAiResponseTextExtractor.extract(raw))
  }

  @Test
  fun joinsMultipleTextParts() {
    val raw = """{"output":[{"content":[{"type":"output_text","text":"one"},{"type":"output_text","text":"two"}]}]}"""
    assertEquals("one\ntwo", OpenAiResponseTextExtractor.extract(raw))
  }

  @Test
  fun rejectsMalformedResponse() {
    assertNull(OpenAiResponseTextExtractor.extract("not-json"))
  }
}

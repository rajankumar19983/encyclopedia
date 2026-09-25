package com.rajankumar.encyclopaedia.feature.knowledge

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AiContentResponseParserEdgeCaseTest {
  @Test
  fun `parser accepts JSON surrounded by prose`() {
    val raw = """
      Here is the requested structure.
      {"root":{"title":"OS","children":[]}}
      Review it before saving.
    """.trimIndent()

    val result = AiContentResponseParser.parse(raw)

    assertTrue(result is AiContentParseResult.Success)
    assertEquals("OS", (result as AiContentParseResult.Success).proposal.root.title)
  }

  @Test
  fun `parser rejects response without JSON object`() {
    val result = AiContentResponseParser.parse("No structured content available")

    assertTrue(result is AiContentParseResult.Failure)
    assertEquals(
      "AI response did not contain a JSON object.",
      (result as AiContentParseResult.Failure).reason,
    )
  }

  @Test
  fun `parser ignores non object lesson entries`() {
    val raw = """{"root":{"title":"DBMS","lessons":["bad",{"title":"Keys","content":"Candidate and primary keys"}]}}"""

    val result = AiContentResponseParser.parse(raw) as AiContentParseResult.Success

    assertEquals(1, result.proposal.root.lessons.size)
    assertEquals("Keys", result.proposal.root.lessons.single().title)
  }

  @Test
  fun `parser validates empty root title`() {
    val result = AiContentResponseParser.parse("""{"root":{"title":""}}""")

    assertTrue(result is AiContentParseResult.Failure)
    assertTrue((result as AiContentParseResult.Failure).reason.contains("empty title"))
  }
}

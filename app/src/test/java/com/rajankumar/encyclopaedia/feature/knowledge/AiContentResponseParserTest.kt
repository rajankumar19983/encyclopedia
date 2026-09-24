package com.rajankumar.encyclopaedia.feature.knowledge

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AiContentResponseParserTest {
  @Test
  fun parsesNestedKnowledgeAndLessons() {
    val raw = """
      {"root":{"title":"DBMS","description":"Database systems","lessons":[{"title":"Overview","content":"Database fundamentals"}],"children":[{"title":"Normalization","lessons":[],"children":[]}]}}
    """.trimIndent()

    val result = AiContentResponseParser.parse(raw)
    assertTrue(result is AiContentParseResult.Success)
    val proposal = (result as AiContentParseResult.Success).proposal
    assertEquals("DBMS", proposal.root.title)
    assertEquals("Overview", proposal.root.lessons.single().title)
    assertEquals("Normalization", proposal.root.children.single().title)
  }

  @Test
  fun acceptsJsonSurroundedByModelNoise() {
    val result = AiContentResponseParser.parse("Here is the result: {\"title\":\"Networks\",\"lessons\":[],\"children\":[]} done")
    assertTrue(result is AiContentParseResult.Success)
  }

  @Test
  fun rejectsNonJsonResponse() {
    val result = AiContentResponseParser.parse("I cannot produce that content.")
    assertTrue(result is AiContentParseResult.Failure)
  }

  @Test
  fun rejectsStructurallyInvalidContent() {
    val result = AiContentResponseParser.parse("{\"root\":{\"title\":\"\",\"lessons\":[{\"title\":\"Broken\",\"content\":\"\"}]}}")
    assertTrue(result is AiContentParseResult.Failure)
  }
}

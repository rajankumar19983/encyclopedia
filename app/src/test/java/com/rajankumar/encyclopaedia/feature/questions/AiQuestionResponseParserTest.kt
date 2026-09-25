package com.rajankumar.encyclopaedia.feature.questions

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AiQuestionResponseParserTest {
  @Test fun parsesValidQuestionBatch() {
    val raw = """
      {"questions":[{"question":"Which register points to the next instruction?","options":["IR","PC","MAR","MDR"],"correctIndex":1,"explanation":"The program counter holds the address of the next instruction.","difficulty":"MEDIUM"}]}
    """.trimIndent()
    val result = AiQuestionResponseParser.parse(raw)
    assertTrue(result is AiQuestionParseResult.Success)
    val proposal = (result as AiQuestionParseResult.Success).proposal
    assertEquals(1, proposal.questions.size)
    assertEquals(1, proposal.questions.single().correctIndex)
    assertEquals(AiQuestionDraftOrigin.AI, proposal.questions.single().origin)
  }

  @Test fun acceptsJsonSurroundedByModelText() {
    val raw = "prefix {\"questions\":[{\"question\":\"Q?\",\"options\":[\"A\",\"B\",\"C\",\"D\"],\"correctIndex\":0,\"explanation\":\"Explanation\",\"difficulty\":\"EASY\"}]} suffix"
    assertTrue(AiQuestionResponseParser.parse(raw) is AiQuestionParseResult.Success)
  }

  @Test fun rejectsInvalidGeneratedShape() {
    val raw = "{\"questions\":[{\"question\":\"Q?\",\"options\":[\"A\",\"B\"],\"correctIndex\":0,\"explanation\":\"Explanation\",\"difficulty\":\"EASY\"}]}"
    assertTrue(AiQuestionResponseParser.parse(raw) is AiQuestionParseResult.Failure)
  }
}

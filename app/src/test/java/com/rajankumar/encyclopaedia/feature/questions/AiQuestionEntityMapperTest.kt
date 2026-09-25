package com.rajankumar.encyclopaedia.feature.questions

import org.junit.Assert.assertEquals
import org.junit.Test

class AiQuestionEntityMapperTest {
  @Test fun mapsApprovedDraftAsAiQuestion() {
    val proposal = AiQuestionProposal(
      listOf(AiQuestionDraft("Question", listOf("One", "Two", "Three", "Four"), 2, "Explanation", "hard")),
    )
    val entity = AiQuestionEntityMapper.map(proposal, now = 123L, idFactory = { "id-1" }).single()
    assertEquals("id-1", entity.id)
    assertEquals("AI", entity.source)
    assertEquals("C", entity.correctAnswer)
    assertEquals("HARD", entity.difficulty)
    assertEquals("One\nTwo\nThree\nFour", entity.options)
  }
}

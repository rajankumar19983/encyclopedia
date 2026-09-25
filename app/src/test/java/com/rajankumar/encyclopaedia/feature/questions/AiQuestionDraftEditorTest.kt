package com.rajankumar.encyclopaedia.feature.questions

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class AiQuestionDraftEditorTest {
  private val first = AiQuestionDraft("First", listOf("A", "B", "C", "D"), 0, "Why", "EASY")
  private val second = AiQuestionDraft("Second", listOf("A", "B", "C", "D"), 1, "Why", "HARD")

  @Test fun updatesOnlyRequestedQuestion() {
    val proposal = updateAiQuestion(AiQuestionProposal(listOf(first, second)), 1) { it.copy(questionText = "Updated") }
    assertEquals("First", proposal.questions[0].questionText)
    assertEquals("Updated", proposal.questions[1].questionText)
  }

  @Test fun removesRequestedQuestion() {
    val proposal = removeAiQuestion(AiQuestionProposal(listOf(first, second)), 0)
    assertEquals(listOf("Second"), proposal.questions.map { it.questionText })
  }

  @Test fun updatesSingleOption() {
    val updated = updateAiQuestionOption(first, 2, "Changed")
    assertEquals("Changed", updated.options[2])
    assertEquals("A", updated.options[0])
  }

  @Test fun manualDraftStartsWithFourOptionsAndUserOrigin() {
    val draft = newManualAiQuestionDraft()
    assertEquals(4, draft.options.size)
    assertEquals(AiQuestionDraftOrigin.USER, draft.origin)
    assertEquals("MEDIUM", draft.difficulty)
  }

  @Test fun appendsManualQuestionWithoutChangingGeneratedOrigins() {
    val manual = newManualAiQuestionDraft().copy(
      questionText = "Manual",
      options = listOf("A", "B", "C", "D"),
      explanation = "Why",
    )
    val proposal = appendAiQuestion(AiQuestionProposal(listOf(first)), manual)

    assertEquals(listOf("First", "Manual"), proposal.questions.map { it.questionText })
    assertEquals(AiQuestionDraftOrigin.AI, proposal.questions[0].origin)
    assertEquals(AiQuestionDraftOrigin.USER, proposal.questions[1].origin)
  }

  @Test fun rejectsAppendAtProposalLimit() {
    val full = AiQuestionProposal(List(AI_QUESTION_PROPOSAL_MAX_SIZE) { first })
    assertThrows(IllegalArgumentException::class.java) {
      appendAiQuestion(full, second)
    }
  }
}

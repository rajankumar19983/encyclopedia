package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AiQuestionApprovalCheckTest {
  private fun draft(text: String = "Which register stores the next instruction address?") = AiQuestionDraft(
    questionText = text,
    options = listOf("MAR", "PC", "IR", "SP"),
    correctIndex = 1,
    explanation = "The program counter stores the address of the next instruction.",
    difficulty = "MEDIUM",
  )

  private fun stored(text: String) = QuestionEntity(
    id = "existing",
    questionText = text,
    options = "MAR\nPC\nIR\nSP",
    correctAnswer = "B",
  )

  @Test
  fun validUniqueProposalIsApprovable() {
    val result = checkAiQuestionApproval(AiQuestionProposal(listOf(draft())), emptyList())
    assertTrue(result.isApprovable)
  }

  @Test
  fun existingDuplicateBlocksApproval() {
    val proposal = AiQuestionProposal(listOf(draft()))
    val result = checkAiQuestionApproval(
      proposal,
      listOf(stored("Which register stores the next instruction address ?")),
    )

    assertFalse(result.isApprovable)
    assertTrue(result.validation.isValid)
    assertTrue(result.duplicateConflicts.isNotEmpty())
  }

  @Test
  fun structuralValidationStillBlocksApprovalWithoutDuplicates() {
    val proposal = AiQuestionProposal(listOf(draft().copy(options = listOf("PC", "IR"))))
    val result = checkAiQuestionApproval(proposal, emptyList())

    assertFalse(result.isApprovable)
    assertFalse(result.validation.isValid)
  }
}

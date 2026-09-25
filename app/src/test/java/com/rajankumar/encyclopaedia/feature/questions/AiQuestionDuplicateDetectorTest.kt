package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AiQuestionDuplicateDetectorTest {
  private fun draft(text: String) = AiQuestionDraft(
    questionText = text,
    options = listOf("One", "Two", "Three", "Four"),
    correctIndex = 0,
    explanation = "Explanation",
    difficulty = "MEDIUM",
  )

  private fun stored(id: String, text: String) = QuestionEntity(
    id = id,
    questionText = text,
    options = "One\nTwo\nThree\nFour",
    correctAnswer = "A",
  )

  @Test
  fun detectsExistingQuestionDespiteCaseSpacingAndPunctuation() {
    val proposal = AiQuestionProposal(listOf(draft("What is RAM ?")))
    val conflicts = findAiQuestionDuplicateConflicts(
      proposal,
      listOf(stored("stored-1", "WHAT is RAM?")),
    )

    assertEquals(1, conflicts.size)
    assertEquals(0, conflicts.single().proposalIndex)
    assertEquals("stored-1", conflicts.single().existingQuestionId)
  }

  @Test
  fun leavesDifferentQuestionsUnblocked() {
    val proposal = AiQuestionProposal(listOf(draft("What is ROM?")))
    val conflicts = findAiQuestionDuplicateConflicts(
      proposal,
      listOf(stored("stored-1", "What is RAM?")),
    )

    assertTrue(conflicts.isEmpty())
  }

  @Test
  fun reportsEachGeneratedQuestionThatAlreadyExists() {
    val proposal = AiQuestionProposal(listOf(draft("What is RAM?"), draft("What is ROM?")))
    val conflicts = findAiQuestionDuplicateConflicts(
      proposal,
      listOf(
        stored("ram", "What is RAM?"),
        stored("rom", "What is ROM?"),
      ),
    )

    assertEquals(listOf(0, 1), conflicts.map { it.proposalIndex })
  }
}

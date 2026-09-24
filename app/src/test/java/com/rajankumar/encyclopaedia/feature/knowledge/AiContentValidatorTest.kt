package com.rajankumar.encyclopaedia.feature.knowledge

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AiContentValidatorTest {
  @Test
  fun acceptsValidProposal() {
    val proposal = AiContentProposal(
      AiKnowledgeDraft(
        title = "Operating Systems",
        lessons = listOf(AiLessonDraft("Processes", "A process is a program in execution."))
      )
    )

    assertTrue(AiContentValidator.validate(proposal).isValid)
  }

  @Test
  fun rejectsBlankTitlesAndLessons() {
    val proposal = AiContentProposal(
      AiKnowledgeDraft(
        title = "",
        lessons = listOf(AiLessonDraft("", ""))
      )
    )

    val result = AiContentValidator.validate(proposal)
    assertFalse(result.isValid)
    assertTrue(result.errors.isNotEmpty())
  }
}

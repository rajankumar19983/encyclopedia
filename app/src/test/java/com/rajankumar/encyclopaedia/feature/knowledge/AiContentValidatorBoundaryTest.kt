package com.rajankumar.encyclopaedia.feature.knowledge

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AiContentValidatorBoundaryTest {
  @Test
  fun `valid proposal passes validation`() {
    val proposal = AiContentProposal(
      root = AiKnowledgeDraft(
        title = "Computer science",
        lessons = listOf(AiLessonDraft("Overview", "Permanent study content")),
        children = listOf(AiKnowledgeDraft(title = "Operating systems")),
      ),
    )

    assertTrue(AiContentValidator.validate(proposal).isValid)
  }

  @Test
  fun `blank lesson content is rejected`() {
    val proposal = AiContentProposal(
      root = AiKnowledgeDraft(
        title = "Networks",
        lessons = listOf(AiLessonDraft("TCP", "   ")),
      ),
    )

    val validation = AiContentValidator.validate(proposal)

    assertFalse(validation.isValid)
    assertTrue(validation.errors.any { it.contains("empty content") })
  }

  @Test
  fun `hierarchy deeper than maximum is rejected`() {
    fun node(depth: Int): AiKnowledgeDraft = AiKnowledgeDraft(
      title = "Level $depth",
      children = if (depth < 7) listOf(node(depth + 1)) else emptyList(),
    )

    val validation = AiContentValidator.validate(AiContentProposal(node(1)))

    assertFalse(validation.isValid)
    assertTrue(validation.errors.any { it.contains("maximum depth") })
  }

  @Test
  fun `proposal exceeding node limit is rejected`() {
    val children = (1..250).map { AiKnowledgeDraft(title = "Node $it") }
    val proposal = AiContentProposal(AiKnowledgeDraft(title = "Root", children = children))

    val validation = AiContentValidator.validate(proposal)

    assertFalse(validation.isValid)
    assertTrue(validation.errors.any { it.contains("maximum is 250") })
  }
}

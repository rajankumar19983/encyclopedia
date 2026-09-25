package com.rajankumar.encyclopaedia.feature.knowledge

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AiGenerationUiStateTest {
  @Test
  fun `generation requires a non blank topic`() {
    assertFalse(AiGenerationUiState(topic = "").canGenerate)
    assertFalse(AiGenerationUiState(topic = "   ").canGenerate)
    assertTrue(AiGenerationUiState(topic = "Operating systems").canGenerate)
  }

  @Test
  fun `generation is disabled while a request is active`() {
    val state = AiGenerationUiState(
      topic = "Computer networks",
      isGenerating = true,
    )

    assertFalse(state.canGenerate)
  }

  @Test
  fun `has proposal reflects generated draft presence`() {
    assertFalse(AiGenerationUiState().hasProposal)

    val proposal = AiContentProposal(
      root = AiKnowledgeDraft(title = "Database systems"),
    )
    assertTrue(AiGenerationUiState(proposal = proposal).hasProposal)
  }

  @Test
  fun `review state can retain its generation destination`() {
    val state = AiGenerationUiState(
      destinationNodeId = "node-42",
      destinationLabel = "Operating systems",
    )

    assertEquals("node-42", state.destinationNodeId)
    assertEquals("Operating systems", state.destinationLabel)
  }
}

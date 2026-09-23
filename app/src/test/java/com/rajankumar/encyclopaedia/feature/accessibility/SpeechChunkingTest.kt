package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SpeechChunkingTest {
  @Test fun blankTextProducesNoChunks() = assertTrue(speechChunks("  ").isEmpty())
  @Test fun shortTextStaysTogether() = assertEquals(listOf("One sentence."), speechChunks("One sentence."))
  @Test fun longTextSplitsAtSentences() = assertEquals(2, speechChunks("First sentence. Second sentence.", 18).size)
}
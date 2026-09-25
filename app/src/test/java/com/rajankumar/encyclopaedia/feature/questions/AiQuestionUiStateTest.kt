package com.rajankumar.encyclopaedia.feature.questions

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AiQuestionUiStateTest {
  @Test fun blankTopicCannotGenerate() = assertFalse(AiQuestionUiState().canGenerate)

  @Test fun validTopicCanGenerate() = assertTrue(AiQuestionUiState(topic = "DBMS").canGenerate)

  @Test fun generationLocksGenerateAction() = assertFalse(AiQuestionUiState(topic = "DBMS", isGenerating = true).canGenerate)
}

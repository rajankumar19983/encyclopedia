package com.rajankumar.encyclopaedia.feature.questions

import org.junit.Assert.assertTrue
import org.junit.Test

class AiQuestionResponseContractTest {
  @Test fun contractRequiresStructuredReviewableQuestions() {
    val prompt = AiQuestionResponseContract.promptFor(AiQuestionRequest("Operating systems", 15, AiQuestionDifficulty.HARD))
    assertTrue(prompt.contains("15"))
    assertTrue(prompt.contains("Operating systems"))
    assertTrue(prompt.contains("correctIndex"))
    assertTrue(prompt.contains("4 to 6"))
    assertTrue(prompt.contains("Do not claim any generated question is a previous-year question"))
  }
}

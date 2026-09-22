package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import org.junit.Assert.assertTrue
import org.junit.Test

class PracticeVariableOptionsTest {
  @Test fun acceptsSevenOptionQuestion() {
    val question = QuestionEntity(
      id = "q1",
      questionText = "Choose the seventh option",
      options = listOf("One", "Two", "Three", "Four", "Five", "Six", "Seven").joinToString("\n"),
      correctAnswer = "G"
    )
    assertTrue(question.validateForPractice().valid)
  }
}

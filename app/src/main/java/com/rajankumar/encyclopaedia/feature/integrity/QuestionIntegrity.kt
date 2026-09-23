package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

fun QuestionEntity.hasValidQuestionFields(): Boolean {
  val parsedOptions = options.lines().map(String::trim).filter(String::isNotBlank)
  return id.isNotBlank() && questionText.isNotBlank() && parsedOptions.size >= 2 &&
    correctAnswer.isNotBlank() && source.isNotBlank() && difficulty.isNotBlank()
}

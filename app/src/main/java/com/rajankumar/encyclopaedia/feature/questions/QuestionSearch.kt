package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

fun List<QuestionEntity>.searchQuestions(query: String): List<QuestionEntity> {
  val needle = query.trim()
  if (needle.isBlank()) return this
  return filter { question ->
    question.questionText.contains(needle, ignoreCase = true) ||
      question.options.contains(needle, ignoreCase = true) ||
      question.explanation.orEmpty().contains(needle, ignoreCase = true)
  }
}

package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

fun List<QuestionEntity>.filterQuestions(source: String? = null, difficulty: String? = null): List<QuestionEntity> = filter { question ->
  (source.isNullOrBlank() || question.source.equals(source, ignoreCase = true)) &&
    (difficulty.isNullOrBlank() || question.difficulty.equals(difficulty, ignoreCase = true))
}

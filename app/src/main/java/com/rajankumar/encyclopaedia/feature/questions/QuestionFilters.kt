package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

fun List<QuestionEntity>.filterQuestions(
  source: String? = null,
  difficulty: String? = null,
): List<QuestionEntity> {
  val normalizedSource = source?.trim()?.uppercase()?.takeIf(String::isNotEmpty)
  val normalizedDifficulty = difficulty?.trim()?.takeIf(String::isNotEmpty)?.let(::normalizeDifficulty)

  return filter { question ->
    (normalizedSource == null || question.source.trim().uppercase() == normalizedSource) &&
      (normalizedDifficulty == null || normalizeDifficulty(question.difficulty) == normalizedDifficulty)
  }
}

package com.rajankumar.encyclopaedia.feature.performance

fun List<WeakQuestion>.searchWeakQuestions(query: String): List<WeakQuestion> =
  if (query.isBlank()) this else filter { it.question.questionText.contains(query.trim(), ignoreCase = true) }

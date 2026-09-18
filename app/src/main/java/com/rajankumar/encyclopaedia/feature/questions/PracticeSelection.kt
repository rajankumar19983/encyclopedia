package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

fun selectPracticeQuestions(source: List<QuestionEntity>, limit: Int): List<QuestionEntity> = source
  .asSequence()
  .filter { it.validateForPractice().valid }
  .shuffled()
  .take(limit.coerceAtLeast(1))
  .toList()

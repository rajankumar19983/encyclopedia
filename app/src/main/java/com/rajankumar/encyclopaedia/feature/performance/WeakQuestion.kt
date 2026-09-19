package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

data class WeakQuestion(
  val question: QuestionEntity,
  val attempts: Int,
  val mistakes: Int
) {
  val accuracy: Int get() = if (attempts == 0) 0 else ((attempts - mistakes).coerceAtLeast(0) * 100 / attempts)
}

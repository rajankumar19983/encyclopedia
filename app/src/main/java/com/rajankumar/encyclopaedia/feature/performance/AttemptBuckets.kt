package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity

data class AttemptBucket(val label: String, val attempts: Int, val correct: Int) {
  val accuracy: Int get() = if (attempts == 0) 0 else correct * 100 / attempts
}

fun List<QuestionAttemptEntity>.chunkedPerformance(size: Int = 10): List<AttemptBucket> =
  sortedBy { it.attemptedAt }.chunked(size.coerceAtLeast(1)).mapIndexed { index, group ->
    AttemptBucket("${index * size + 1}–${index * size + group.size}", group.size, group.count { it.isCorrect })
  }

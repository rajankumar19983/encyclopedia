package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity

data class MistakeFrequency(val questionId: String, val mistakes: Int)

fun List<QuestionAttemptEntity>.mistakeFrequencies(): List<MistakeFrequency> =
  filterNot { it.isCorrect }
    .groupingBy { it.questionId }
    .eachCount()
    .map { MistakeFrequency(it.key, it.value) }
    .sortedByDescending { it.mistakes }

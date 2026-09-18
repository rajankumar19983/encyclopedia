package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

fun List<PracticeAnswerReview>.questionsForRetry(): List<QuestionEntity> =
  incorrectOnly().map { it.question }.distinctBy { it.id }

package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity

fun QuestionAttemptEntity.hasValidAttemptFields(): Boolean =
  id.isNotBlank() && questionId.isNotBlank() && sessionId.isNotBlank() &&
    selectedAnswer.isNotBlank() && timeTakenMs >= 0L && attemptedAt > 0L

package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

fun List<QuestionEntity>.applyQuestionBankFilters(state: QuestionBankFilterState): List<QuestionEntity> =
  searchQuestions(state.query).filterQuestions(state.source, state.difficulty)

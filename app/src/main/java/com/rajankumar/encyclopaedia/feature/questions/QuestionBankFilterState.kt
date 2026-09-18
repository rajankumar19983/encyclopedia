package com.rajankumar.encyclopaedia.feature.questions

data class QuestionBankFilterState(
  val query: String = "",
  val source: String? = null,
  val difficulty: String? = null
)

package com.rajankumar.encyclopaedia.feature.questions

data class QuestionBankFilterState(
  val query: String = "",
  val source: String? = null,
  val difficulty: String? = null,
  val topicId: String? = null,
) {
  val hasActiveFilters: Boolean
    get() = query.isNotBlank() || source != null || difficulty != null || topicId != null
}

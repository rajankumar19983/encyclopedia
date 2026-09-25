package com.rajankumar.encyclopaedia.feature.questions

data class PracticeSessionConfig(
  val mode: PracticeMode = PracticeMode.RANDOM,
  val questionCount: Int = PracticeConfig.defaultSessionSize,
  val query: String = "",
  val source: String? = null,
  val difficulty: String? = null,
  val topicId: String? = null,
) {
  val safeQuestionCount: Int
    get() = questionCount.coerceIn(1, PracticeConstants.maxSessionQuestions)

  val hasFilters: Boolean
    get() = query.isNotBlank() || !source.isNullOrBlank() || !difficulty.isNullOrBlank() || !topicId.isNullOrBlank()

  fun toQuestionBankFilterState(): QuestionBankFilterState = QuestionBankFilterState(
    query = query,
    source = source,
    difficulty = difficulty,
    topicId = topicId,
  )
}

fun QuestionBankFilterState.toPracticeSessionConfig(
  mode: PracticeMode = PracticeMode.RANDOM,
  questionCount: Int = PracticeConfig.defaultSessionSize,
): PracticeSessionConfig = PracticeSessionConfig(
  mode = mode,
  questionCount = questionCount,
  query = query,
  source = source,
  difficulty = difficulty,
  topicId = topicId,
)

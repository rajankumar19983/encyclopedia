package com.rajankumar.encyclopaedia.feature.questions

data class PracticeSessionConfig(
  val mode: PracticeMode = PracticeMode.RANDOM,
  val questionCount: Int = PracticeConfig.defaultSessionSize
) {
  val safeQuestionCount: Int get() = questionCount.coerceIn(1, PracticeConstants.maxSessionQuestions)
}

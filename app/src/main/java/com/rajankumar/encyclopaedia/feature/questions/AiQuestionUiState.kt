package com.rajankumar.encyclopaedia.feature.questions

data class AiQuestionUiState(
  val topic: String = "",
  val count: Int = 10,
  val difficulty: AiQuestionDifficulty = AiQuestionDifficulty.MIXED,
  val destinationTopicId: String? = null,
  val destinationLabel: String? = null,
  val isGenerating: Boolean = false,
  val proposal: AiQuestionProposal? = null,
  val errorMessage: String? = null,
) {
  val canGenerate: Boolean
    get() = topic.isNotBlank() && count in 1..30 && !isGenerating
}

package com.rajankumar.encyclopaedia.feature.knowledge

data class AiGenerationUiState(
  val topic: String = "",
  val depth: AiContentDepth = AiContentDepth.STANDARD,
  val includeLessons: Boolean = true,
  val isGenerating: Boolean = false,
  val proposal: AiContentProposal? = null,
  val errorMessage: String? = null,
) {
  val canGenerate: Boolean
    get() = topic.isNotBlank() && !isGenerating

  val hasProposal: Boolean
    get() = proposal != null
}

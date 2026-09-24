package com.rajankumar.encyclopaedia.feature.knowledge

interface AiProvider {
  suspend fun generate(request: AiGenerationRequest): AiGenerationResult
}

data class AiGenerationRequest(
  val prompt: String,
  val model: String,
)

sealed interface AiGenerationResult {
  data class Success(val text: String) : AiGenerationResult
  data class Failure(val error: AiProviderError) : AiGenerationResult
}

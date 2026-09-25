package com.rajankumar.encyclopaedia.feature.knowledge

class AiContentGenerator(
  private val provider: AiProvider,
  private val model: String = "gpt-5.6-luna",
) {
  suspend fun generate(request: AiContentRequest): AiContentGenerationResult =
    generate(AiContentResponseContract.promptFor(request))

  suspend fun generate(prompt: String): AiContentGenerationResult {
    if (prompt.isBlank()) return AiContentGenerationResult.Failure("Prompt cannot be blank")
    return when (val result = provider.generate(AiGenerationRequest(prompt.trim(), model))) {
      is AiGenerationResult.Success -> when (val parsed = AiContentResponseParser.parse(result.text)) {
        is AiContentParseResult.Success -> AiContentGenerationResult.Success(parsed.proposal)
        is AiContentParseResult.Failure -> AiContentGenerationResult.Failure(parsed.reason)
      }
      is AiGenerationResult.Failure -> AiContentGenerationResult.ProviderFailure(result.error)
    }
  }
}

sealed interface AiContentGenerationResult {
  data class Success(val proposal: AiContentProposal) : AiContentGenerationResult
  data class Failure(val reason: String) : AiContentGenerationResult
  data class ProviderFailure(val error: AiProviderError) : AiContentGenerationResult
}

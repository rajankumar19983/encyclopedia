package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.feature.knowledge.AiGenerationRequest
import com.rajankumar.encyclopaedia.feature.knowledge.AiGenerationResult
import com.rajankumar.encyclopaedia.feature.knowledge.AiProvider
import com.rajankumar.encyclopaedia.feature.knowledge.AiProviderError

class AiQuestionGenerator(
  private val provider: AiProvider,
  private val model: String = "gpt-5.6-luna",
) {
  suspend fun generate(request: AiQuestionRequest): AiQuestionGenerationResult {
    val prompt = runCatching { AiQuestionResponseContract.promptFor(request) }
      .getOrElse { return AiQuestionGenerationResult.Failure(it.message ?: "Invalid generation request") }
    return when (val result = provider.generate(AiGenerationRequest(prompt, model))) {
      is AiGenerationResult.Success -> when (val parsed = AiQuestionResponseParser.parse(result.text)) {
        is AiQuestionParseResult.Success -> AiQuestionGenerationResult.Success(parsed.proposal)
        is AiQuestionParseResult.Failure -> AiQuestionGenerationResult.Failure(parsed.reason)
      }
      is AiGenerationResult.Failure -> AiQuestionGenerationResult.ProviderFailure(result.error)
    }
  }
}

sealed interface AiQuestionGenerationResult {
  data class Success(val proposal: AiQuestionProposal) : AiQuestionGenerationResult
  data class Failure(val reason: String) : AiQuestionGenerationResult
  data class ProviderFailure(val error: AiProviderError) : AiQuestionGenerationResult
}

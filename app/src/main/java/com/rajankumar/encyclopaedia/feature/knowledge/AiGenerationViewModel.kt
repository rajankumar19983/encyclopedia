package com.rajankumar.encyclopaedia.feature.knowledge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AiGenerationViewModel(
  private val generator: AiContentGenerator,
) : ViewModel() {
  private val _uiState = MutableStateFlow(AiGenerationUiState())
  val uiState: StateFlow<AiGenerationUiState> = _uiState.asStateFlow()

  fun setTopic(topic: String) {
    _uiState.update { it.copy(topic = topic, errorMessage = null) }
  }

  fun setDepth(depth: AiContentDepth) {
    _uiState.update { it.copy(depth = depth, errorMessage = null) }
  }

  fun setIncludeLessons(include: Boolean) {
    _uiState.update { it.copy(includeLessons = include, errorMessage = null) }
  }

  fun generate(parentNodeId: String? = null) {
    val state = _uiState.value
    if (!state.canGenerate) return

    val request = AiContentRequest(
      topic = state.topic.trim(),
      parentNodeId = parentNodeId,
      depth = state.depth,
      includeLessons = state.includeLessons,
    )

    _uiState.update { it.copy(isGenerating = true, proposal = null, errorMessage = null) }
    viewModelScope.launch {
      val prompt = AiContentPromptBuilder.build(request)
      when (val result = generator.generate(prompt)) {
        is AiContentGenerationResult.Success -> _uiState.update {
          it.copy(isGenerating = false, proposal = result.proposal)
        }
        is AiContentGenerationResult.Failure -> _uiState.update {
          it.copy(isGenerating = false, errorMessage = result.reason)
        }
        is AiContentGenerationResult.ProviderFailure -> _uiState.update {
          it.copy(isGenerating = false, errorMessage = result.error.toUserMessage())
        }
      }
    }
  }

  fun updateProposal(proposal: AiContentProposal) {
    _uiState.update { it.copy(proposal = proposal, errorMessage = null) }
  }

  fun clearProposal() {
    _uiState.update { it.copy(proposal = null) }
  }

  fun clearError() {
    _uiState.update { it.copy(errorMessage = null) }
  }

  private fun AiProviderError.toUserMessage(): String = when (this) {
    AiProviderError.MissingApiKey -> "Add your OpenAI API key before generating content."
    AiProviderError.Unauthorized -> "The saved OpenAI API key was rejected."
    AiProviderError.RateLimited -> "OpenAI rate limit reached. Try again shortly."
    is AiProviderError.Network -> message ?: "Network request failed."
    is AiProviderError.Http -> message ?: "OpenAI request failed with HTTP $statusCode."
    is AiProviderError.InvalidResponse -> message ?: "OpenAI returned an invalid response."
  }
}

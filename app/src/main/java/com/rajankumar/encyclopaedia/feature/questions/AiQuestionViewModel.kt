package com.rajankumar.encyclopaedia.feature.questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajankumar.encyclopaedia.feature.knowledge.AiErrorMessages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AiQuestionViewModel(
  private val generator: AiQuestionGenerator,
) : ViewModel() {
  private val _uiState = MutableStateFlow(AiQuestionUiState())
  val uiState: StateFlow<AiQuestionUiState> = _uiState.asStateFlow()

  fun setTopic(value: String) = _uiState.update { it.copy(topic = value, errorMessage = null) }
  fun setCount(value: Int) = _uiState.update { it.copy(count = value.coerceIn(1, 30), errorMessage = null) }
  fun setDifficulty(value: AiQuestionDifficulty) = _uiState.update { it.copy(difficulty = value, errorMessage = null) }
  fun clearError() = _uiState.update { it.copy(errorMessage = null) }

  fun updateProposal(proposal: AiQuestionProposal) {
    _uiState.update { it.copy(proposal = proposal, errorMessage = null) }
  }

  fun clearProposal() {
    _uiState.update {
      it.copy(
        proposal = null,
        destinationTopicId = null,
        destinationLabel = null,
        errorMessage = null,
      )
    }
  }

  fun generate(destinationTopicId: String?, destinationLabel: String?) {
    val state = _uiState.value
    if (!state.canGenerate) {
      _uiState.update { it.copy(errorMessage = "Enter a topic before generating questions.") }
      return
    }
    _uiState.update {
      it.copy(
        isGenerating = true,
        errorMessage = null,
        destinationTopicId = destinationTopicId,
        destinationLabel = destinationLabel,
      )
    }
    launchGeneration(AiQuestionRequest(state.topic.trim(), state.count, state.difficulty))
  }

  fun regenerate() {
    val state = _uiState.value
    if (state.topic.isBlank() || state.isGenerating) return
    _uiState.update { it.copy(isGenerating = true, errorMessage = null) }
    launchGeneration(AiQuestionRequest(state.topic.trim(), state.count, state.difficulty))
  }

  private fun launchGeneration(request: AiQuestionRequest) {
    val existingProposal = _uiState.value.proposal
    viewModelScope.launch {
      when (val result = generator.generate(request)) {
        is AiQuestionGenerationResult.Success -> _uiState.update {
          it.copy(isGenerating = false, proposal = result.proposal, errorMessage = null)
        }
        is AiQuestionGenerationResult.Failure -> _uiState.update {
          it.copy(isGenerating = false, proposal = existingProposal, errorMessage = result.reason)
        }
        is AiQuestionGenerationResult.ProviderFailure -> _uiState.update {
          it.copy(isGenerating = false, proposal = existingProposal, errorMessage = AiErrorMessages.forError(result.error))
        }
      }
    }
  }
}
